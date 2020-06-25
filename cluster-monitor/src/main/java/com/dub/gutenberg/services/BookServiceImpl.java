package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.gutenberg.domain.Book;
import com.dub.gutenberg.domain.Category;
import com.dub.gutenberg.domain.Item;
import com.dub.gutenberg.exceptions.BookNotFoundException;
import com.dub.gutenberg.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class BookServiceImpl implements BookService {

private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static final String BOOKS = "gutenberg-books";
	public static final String ORDERS = "gutenberg-orders";
	public static final String TYPE = "_doc";
	
	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	@Override
	public Book getBookBySlug(String slug) throws IOException {
		
		Book book = null;	
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.termQuery("slug.keyword", slug));
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(BOOKS);
		
		SearchResponse response 
				= client.search(searchRequest, RequestOptions.DEFAULT);
			
		SearchHits hits = response.getHits();
	
		long totalHits = hits.getTotalHits().value;
		
		SearchHit[] searchHits = hits.getHits();

		if (totalHits == 1) {

			Map<String, Object> sourceAsMap = searchHits[0].getSourceAsMap();
	
			book = objectMapper.convertValue(sourceAsMap, Book.class);
			book.setId(searchHits[0].getId());	  
	
			return book;
		} else {
			throw new BookNotFoundException();
		}	
	}


	@Override
	public Book getBookById(String bookId) throws IOException {
			
		Optional<Book> book = bookRepository.getBookById(bookId);
		
		if (book.isPresent()) {
			return book.get();
		} else {
			throw new BookNotFoundException();
		}
	}


	/** 
	 * Yet another search
	 * @throws IOException 
	 * */
	@Override
	public List<Book> allBooksByCategory(String categorySlug, String sortBy) throws IOException {
				
		List<Book> books = new ArrayList<>();
		
		// find category id
		Category cat = categoryService.getCategory(categorySlug);
					
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		searchSourceBuilder.query(QueryBuilders.termQuery("categoryId.keyword", cat.getId()));
			
		searchSourceBuilder.sort(new FieldSortBuilder("title.keyword").order(SortOrder.ASC));
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(BOOKS);
					
		SearchResponse response 
					= client.search(searchRequest, RequestOptions.DEFAULT);
	
		SearchHits hits = response.getHits();

		SearchHit[] searchHits = hits.getHits();
			
		for (SearchHit hit : searchHits) {
		
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
				
			Book book = objectMapper.convertValue(sourceAsMap, Book.class);
			book.setId(hit.getId());
									
			books.add(book);
		}		
		
		return books;
	}

	/** 
	 * Reengineering here, use denormalization
	 * */
	@Override
	public List<Book> getBooksBoughtWith(String bookId, int outLimit) throws IOException {
		
		Optional<Book> doc = bookRepository.getBookById(bookId);
		
		if (!doc.isPresent()) {
			throw new BookNotFoundException();
		}
		
		Book book = doc.get();
		
		List<Item> items = book.getBoughtWith();
		List<Book> books = new ArrayList<>();
		
		// sort items by decreasing quantity
		Collections.sort(items, new Comparator<Item>() {
			
			@Override
			public int compare(Item o1, Item o2) {
				if (o1.getQuantity() > o2.getQuantity()) {
					return -1;
				} else if (
						o1.getQuantity() < o2.getQuantity()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
			
		int count = 0;
		for (Item item : items) {
			Optional<Book> bb = bookRepository.getBookById(item.getBookId());
			bb.ifPresent(thisBook -> books.add(thisBook));
			bb.orElseThrow(() -> new BookNotFoundException());
			if (count++ >= outLimit) break;
		}
		
		return books;
	}
}