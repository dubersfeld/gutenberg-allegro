package com.dub.client.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import com.dub.client.controller.config.ServiceConfig;
import com.dub.client.domain.Address;
import com.dub.client.domain.EditCart;
import com.dub.client.domain.Item;
import com.dub.client.domain.Order;
import com.dub.client.domain.PaymentMethod;
import com.dub.client.exceptions.OrderNotFoundException;

@Service
public class OrderServiceImpl implements OrderService {

	private static final String UPDATE_ORDER = "/updateOrder"; 
	private static final String CREATE_ORDER = "/createOrder"; 
	private static final String EDIT_CART = "/editCart"; 
	private static final String ORDER_BY_ID = "/orderById/"; 
	private static final String ADD_BOOK_TO_ORDER = "/addBookToOrder"; 
	private static final String GET_ACTIVE_ORDER = "/getActiveOrder"; 
	private static final String CHECKOUT_ORDER = "/checkoutOrder"; 
	private static final String SET_ORDER_STATE = "/setOrderState"; 
	
	private String baseOrdersUrl;
	
	@PostConstruct 
	public void init() {
		baseOrdersUrl = serviceConfig.getBaseOrdersURL();
	}
	
	
	
	@Autowired
	private RestOperations restTemplate;
		
	//@Autowired
	//private BookService bookService;
	
	@Autowired
	private ServiceConfig serviceConfig;
			
	@Override
	public Order saveOrder(Order order) {
		
		String orderURI = baseOrdersUrl + UPDATE_ORDER;
		
		try {
			ResponseEntity<Order> response 
			= restTemplate.postForEntity(orderURI, order, Order.class);
			
			Order newOrder = response.getBody();
		
			return newOrder;
		} catch (HttpClientErrorException e) {
			throw new RuntimeException();
		}
	}
	
	@Override
	public Order createOrder(Order order) {
		
		//String orderURI = "http://localhost:8083/createOrder";
		String orderURI = baseOrdersUrl + CREATE_ORDER;
		
		
		ResponseEntity<Order> response 
		= restTemplate.postForEntity(orderURI, order, Order.class);

		Order newOrder = response.getBody();
		
		return newOrder;
	}

	@Override
	public Order addBookToOrder(String orderId, String bookId) {
				
		// call order server
		MultiValueMap<String, Object> map 
							= new LinkedMultiValueMap<>();
		map.add("orderId", orderId);
		map.add("bookId", bookId);
				
		List<MediaType> amt = new ArrayList<>(); 
		amt.add(MediaType.APPLICATION_JSON);   
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(amt);// JSON expected from resource server
						    	
		HttpEntity<MultiValueMap<String, Object>> requestEntity = 
		    		new HttpEntity<MultiValueMap<String, Object>>(map, headers);
		  
		String orderURI = baseOrdersUrl + ADD_BOOK_TO_ORDER;    
	
		try {
			ResponseEntity<Order> response = restTemplate.exchange(
		    			orderURI, HttpMethod.POST, requestEntity, Order.class);

			Order newOrder = response.getBody();  
		
			return newOrder;
		} catch (HttpClientErrorException e) {
			throw new RuntimeException();
		}
	}

	
	@Override
	public Optional<Order> getActiveOrder(String userId) {
		
		String orderURI = baseOrdersUrl + GET_ACTIVE_ORDER;
			
		try {
			ResponseEntity<?> response 
			= restTemplate.postForEntity(orderURI, userId, Order.class);
	
			if (response.getStatusCode() == HttpStatus.OK) {				
				return Optional.of((Order)response.getBody());
			} else {	
				return Optional.empty();
			}
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				return Optional.empty();
			} else {
				throw new RuntimeException();
			}	
		}
	}

	@Override
	public Order checkoutOrder(String orderId) {
				
		//String orderURI = "http://localhost:8083/checkoutOrder";
		
		String orderURI = baseOrdersUrl + CHECKOUT_ORDER;
		
		try {
			ResponseEntity<Order> response 
			= restTemplate.postForEntity(orderURI, orderId, Order.class);
						
			return response.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				throw new OrderNotFoundException();
			} else {
				throw new RuntimeException();
			}	
		}
		
	}

	
	@Override
	public Order setCart(String orderId) {
		
		MultiValueMap<String, Object> map 
						= new LinkedMultiValueMap<>();
		map.add("orderId", orderId);
		map.add("state", Order.State.CART);

		List<MediaType> amt = new ArrayList<>();
		amt.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(amt);// JSON expected from resource server

		HttpEntity<MultiValueMap<String, Object>> requestEntity = 
				new HttpEntity<MultiValueMap<String, Object>>(map, headers);

		//String orderURI = "http://localhost:8083/setOrderState";
		String orderURI = baseOrdersUrl + SET_ORDER_STATE;
			
		try {
			ResponseEntity<Order> response = restTemplate.exchange(
	    			orderURI, HttpMethod.POST, requestEntity, Order.class);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				throw new OrderNotFoundException();
			} else {
				throw new RuntimeException();
			}	
		}
	}

	@Override
	public Order getOrderById(String orderId) {
		
		//String orderURI = "http://localhost:8083/orderById/" + orderId;
		String orderURI = baseOrdersUrl + ORDER_BY_ID + orderId;
		
		ResponseEntity<Order> response 
		= restTemplate.getForEntity(orderURI, Order.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			throw new OrderNotFoundException();
		}
	}

	@Override
	public Order editOrder(String orderId, List<Item> items) {
		
		//String orderURI = "http://localhost:8083/editCart";
		String orderURI = baseOrdersUrl + EDIT_CART;
		
		// encapsulation
		EditCart editCart = new EditCart(orderId, items);
			
		try {
			ResponseEntity<Order> response 
			= restTemplate.postForEntity(orderURI, editCart, Order.class);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				throw new OrderNotFoundException();
			} else {
				throw new RuntimeException();
			}	
		}
	}
	
	@Override
	public Order setOrderState(String orderId, Order.State state) {
		
		MultiValueMap<String, Object> map 
						= new LinkedMultiValueMap<>();
		map.add("orderId", orderId);
		map.add("state", stateToString(state));

		List<MediaType> amt = new ArrayList<>();
		amt.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(amt);// JSON expected from resource server

		HttpEntity<MultiValueMap<String, Object>> requestEntity = 
				new HttpEntity<MultiValueMap<String, Object>>(map, headers);

		//String orderURI = "http://localhost:8083/setOrderState";
		String orderURI = baseOrdersUrl + SET_ORDER_STATE;
		
		try {
			ResponseEntity<Order> response = restTemplate.exchange(
	    			orderURI, HttpMethod.POST, requestEntity, Order.class);
	    		
			return response.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				throw new OrderNotFoundException();
			} else {
				throw new RuntimeException();
			}	
		}
	}
	
	private String stateToString(Order.State state) {
		
		String stateStr;
		
		switch (state) {
			case CART:
				stateStr = "CART";
				break;
			case PRE_SHIPPING:
				stateStr = "PRE_SHIPPING";
				break;
			case SHIPPED:
				stateStr = "SHIPPED";
				break;
			case PRE_AUTHORIZE:
				stateStr = "PRE_AUTHORIZE";
				break;
			default:
				stateStr = "CART";
		}
		return stateStr;
	}

	@Override
	public Order finalizeOrder(Order order, Address shippingAddress, PaymentMethod payMeth) {
		
		order.setDate(new Date());
		order.setState(Order.State.PRE_SHIPPING);
		order.setPaymentMethod(payMeth);
		order.setShippingAddress(shippingAddress);
		
		return order;
	}
}
