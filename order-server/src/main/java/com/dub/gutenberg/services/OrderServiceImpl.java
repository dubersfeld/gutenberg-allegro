package com.dub.gutenberg.services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.gutenberg.domain.Book;
import com.dub.gutenberg.domain.EditCart;
import com.dub.gutenberg.domain.Item;
import com.dub.gutenberg.domain.Order;
import com.dub.gutenberg.domain.OrderState;
import com.dub.gutenberg.domain.UserAndReviewedBooks;
import com.dub.gutenberg.exceptions.BookNotFoundException;
import com.dub.gutenberg.exceptions.OrderException;
import com.dub.gutenberg.exceptions.OrderNotFoundException;
import com.dub.gutenberg.repository.BookRepository;
import com.dub.gutenberg.repository.OrderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class OrderServiceImpl implements OrderService {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static final String BOOKS = "gutenberg-books";
	public static final String ORDERS = "gutenberg-orders";
	public static final String TYPE = "_doc";
	public static final String RECENT = "2013-01-01 00:00";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String SHIPPED = "SHIPPED";
	public static final String PRE_SHIPPING = "PRE_SHIPPING";
	
	
	/** 
	 * This DateFormat is supported by Elasticsearch 
	 * but not by default Spring message converters 
	 * */
	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private BookRepository bookRepo;
	
	@PostConstruct
	public void init() {
		objectMapper.setDateFormat(sdf);
	}
	
	
	@Override
	public List<String> getBooksNotReviewed(
			UserAndReviewedBooks userAndReviewedBooks) throws ParseException, IOException {
		/** 
		 * Find all books purchased by this user in the past, 
		 * then find all reviews posted by this user and for each review
		 * remove the book from the initial list 
		*/
			
		String userId = userAndReviewedBooks.getUserId();
		
		List<String> reviewedBookIds 
				= userAndReviewedBooks.getReviewedBookIds();
			
		// find all recent orders for userIds that heve already been shipped
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		RangeQueryBuilder date 
				= QueryBuilders
							.rangeQuery("date")
							.gte(RECENT)
							.format(DATE_FORMAT);
		
		searchSourceBuilder.query(
				QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("userId.keyword", userId))
				.must(QueryBuilders.termQuery("state.keyword", SHIPPED))
				.must(date)		
		);
		
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(ORDERS);
			
		SearchResponse response 
			= client.search(searchRequest, RequestOptions.DEFAULT);
	
		SearchHits hits = response.getHits();

		//long totalHits = hits.getTotalHits();
	
		SearchHit[] searchHits = hits.getHits();
		
		Set<String> bookIds = new HashSet<>();
		//bookIds is the set of all purchased books 
		
		for (SearchHit hit : searchHits) {
			Map<String, Object> map = hit.getSourceAsMap();
			Order order = objectMapper.convertValue(map, Order.class);

			for (Item item :  order.getLineItems()) {
				bookIds.add(item.getBookId());
			}
		}
					
		/** 
		 * create a new list with all entries in bookIds 
		 * that are not in reviewedBookIds		
		*/	
		List<String> newList = new ArrayList<>();
		
		for (String bookId : bookIds) {
			if (!reviewedBookIds.contains(bookId)) {
				newList.add(bookId);
			}
		}
				
		return newList;
	}

	@Override
	public Order saveOrder(Order order, boolean creation) throws IOException {
		
		if (creation) {
			// create a new Order
			IndexRequest indexRequest = new IndexRequest(ORDERS);
			
			Map<String, Object> dataMap = objectMapper.convertValue(order, 
	   				new TypeReference<Map<String, Object>>() {});

			indexRequest.source(dataMap);
			
			IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		        
			order.setId(response.getId());
			
		    return order;
		} else {
			//check for presence if not creation
			Optional<Order> doc = orderRepo.findOrderById(order.getId());
			
			if (!doc.isPresent()) { 
				throw new OrderNotFoundException();
			}
		
			this.updateBoughtWith(order);
					
			// update Order ES document
			Order updatedOrder = orderRepo.doUpdateOrder(order);
		
			
			return updatedOrder;
			          	 
		}
		
	}

	@Override
	public Order getOrderById(String orderId) throws IOException {
		
		Optional<Order> order = orderRepo.findOrderById(orderId);
		if (order.isPresent()) {
			return order.get();
		} else {
			throw new OrderNotFoundException();
		}
	}

	@Override
	public Order getActiveOrder(String userId) throws IOException {
	
		Order order = null;	
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		searchSourceBuilder.query(
				QueryBuilders.boolQuery()
					.must(QueryBuilders.termQuery("userId.keyword", userId))
					.mustNot(QueryBuilders.termQuery("state.keyword", PRE_SHIPPING))
					.mustNot(QueryBuilders.termQuery("state.keyword", SHIPPED))
				);
		
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(ORDERS);
		
		SearchResponse response 
				= client.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits hits = response.getHits();
	
		long totalHits = hits.getTotalHits().value;
			
		SearchHit[] searchHits = hits.getHits();
		if (totalHits == 1) {
			Map<String, Object> sourceAsMap = searchHits[0].getSourceAsMap();		
			order = objectMapper.convertValue(sourceAsMap, Order.class);
			order.setId(searchHits[0].getId());	
			return order;	
		} else {
			throw new OrderNotFoundException();
		}
	}

	@Override
	public Order addBookToOrder(String orderId, String bookId) throws IOException {
			
		Optional<Order> order = orderRepo.findOrderById(orderId);
				
		if (!order.isPresent()) {
			throw new OrderNotFoundException();
		}
		
		Order oldOrder = order.get();
		
		List<Item> items = oldOrder.getLineItems();
					
		// check if bookId already present
		boolean present = false;
		
		for (Item item : items) {
			if (item.getBookId().equals(bookId)) {
				present = true;
				item.setQuantity(item.getQuantity()+1);
			}
		}
		if (!present)  {
			// add a new Item
			items.add(new Item(bookId, 1));
		}
			
		oldOrder.setLineItems(items);
				
		// then recalculate
		recalculateTotal(oldOrder);
			
		// update Order ES document		
		Order newOrder = orderRepo.doUpdateOrder(oldOrder);
			
		return newOrder;
	}

	@Override
	public Order editCart(EditCart editCart) throws IOException {
		
		List<Item> items = editCart.getItems();
		String orderId = editCart.getOrderId();
				
		// retrieve order by Id
		Optional<Order> doc = orderRepo.findOrderById(orderId);
		
		if (!doc.isPresent()) {
			throw new OrderNotFoundException();
		}
		Order order = doc.get();
		
		order.setLineItems(items);
		
		// recalculate total
		this.recalculateTotal(order);
		
		Order newOrder = orderRepo.doUpdateOrder(order);
			
		return newOrder;
		
	}

	@Override
	public Order setOrderState(String orderId, OrderState state) throws IOException {
		
		Optional<Order> order = orderRepo.findOrderById(orderId);
		
		if (!order.isPresent()) {
			throw new OrderNotFoundException();
		}
		
		Order oldOrder = order.get();
	
		OrderState oldState = oldOrder.getState();
		
		// legal transitions only
		switch (state) {
		case CART:
			if (oldState.equals(OrderState.SHIPPED) ||
					oldState.equals(OrderState.PRE_SHIPPING)) {
				throw new OrderException();
			}
			break;
		case PRE_AUTHORIZE:
			if (oldState.equals(OrderState.SHIPPED) ||
					oldState.equals(OrderState.PRE_SHIPPING)) {
				throw new OrderException();
			}
			break;
		case PRE_SHIPPING:
			if (oldState.equals(OrderState.SHIPPED)) {
				throw new OrderException();
			}
			break;
		default:
			throw new OrderException();// should not be here
		}
		
		oldOrder.setState(state);
		
		// update Order ES document
		return orderRepo.doUpdateOrder(oldOrder);
	}

	@Override
	public Order checkoutOrder(String orderId) throws IOException {
		
		// retrieve order by Id
		Optional<Order> doc = orderRepo.findOrderById(orderId);
		
		if (!doc.isPresent()) {
			throw new OrderNotFoundException();
		}
		Order order = doc.get();
		
		order.setState(OrderState.PRE_AUTHORIZE);

		return orderRepo.doUpdateOrder(order);
	}

	
	private void recalculateTotal(Order order) throws IOException {
	
		int total = 0;
		
		for (Item item : order.getLineItems()) {
			Optional<Book> book = bookRepo.getBookById(item.getBookId());
					
			if (book.isPresent()) {	
				total += book.get().getPrice() * item.getQuantity();
			} else {
				throw new BookNotFoundException();
			}
		}
		order.setSubtotal(total);// actual update
	}
	
	/** 
	 * This method is called to update all boughtWith fields
	 * in gutenberg-books index
	 * @throws IOException 
	 */
	private void updateBoughtWith(Order order) throws IOException {
		
		/**
		 * An order may have multiple bookIds
		 * */
		
		Set<String> bookIds = new HashSet<>();
		List<Item> items = order.getLineItems();
		
		for (Item item : items) {
			bookIds.add(item.getBookId());
		}
		
		/** 
		 * Step 1: retrieve all past orders from this user 
		 * that have already been placed (state PRE_SHIPPING or SHIPPED)
		 * */
		String userId = order.getUserId();
		
		// use shouldQuery to implement a Boolean OR
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(
				QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("userId.keyword", userId))
				.should(QueryBuilders.termQuery("state.keyword", PRE_SHIPPING))
				.should(QueryBuilders.termQuery("state.keyword", SHIPPED))
				.minimumShouldMatch(1));// required
		
		SearchRequest searchRequest = new SearchRequest();	
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(ORDERS);
		
	
		
		SearchResponse response 
			= client.search(searchRequest, RequestOptions.DEFAULT);
	
		
		SearchHits hits = response.getHits();
		SearchHit[] searchHits = hits.getHits();
				
		// build a Set of all books bought in the past
		Set<String> pastBookIds = new HashSet<>();
		
	
		/** 
		 * loop for each past order of this user 
		 * */
		for (SearchHit hit : searchHits) {
		
			// exclude present order 
			Map<String, Object> map = hit.getSourceAsMap();
				
			Order pastOrder 
				= objectMapper.convertValue(map, Order.class);
			pastOrder.setId(hit.getId());
			
			if (pastOrder.getId().equals(order.getId())) {				
				continue;
			}
		
			List<Item> pastItems = pastOrder.getLineItems();
			
			for (Item item : pastItems) {
				// add unique
				pastBookIds.add(item.getBookId());
			}
		}
			
		/** 
		 * Step 2. Update the boughtWith field in each Book in the pastBookIds set
		 * 
		 * */
	
		// iterate on past books
		for (String pastBookId : pastBookIds) {
			
			// retrieve past book
			Optional<Book> doc = bookRepo.getBookById(pastBookId);
				
			if (!doc.isPresent()) {
				throw new BookNotFoundException();
			}
			
			Book pastBook = doc.get();
			
			List<Item> pastItems = pastBook.getBoughtWith();// may be null
					
			for (String bookId : bookIds) {
				// iterate on books bought in this order
				
				// if same book nothing to do
				if (bookId.equals(pastBook.getId())) {
					continue;
				}
				// check if bookId already present
				boolean present = false;
						
				if (pastItems != null) {
					// iterate on pastItems
					for (Item pastItem : pastItems) {		
						if (pastItem.getBookId().equals(bookId)) {
							present = true;
							pastItem.setQuantity(pastItem.getQuantity()+1);
						}
					}
				} 
					
				if (!present)  {
					// first check if pastItems exists
					if (pastItems == null) {
						pastItems = new ArrayList<>();
					}
					// add a new Item
					pastItems.add(new Item(bookId, 1));
				}	
			}
			
			// update past book
			pastBook.setBoughtWith(pastItems);
			
			bookRepo.doUpdateBook(pastBook);	
		}
	}
}
