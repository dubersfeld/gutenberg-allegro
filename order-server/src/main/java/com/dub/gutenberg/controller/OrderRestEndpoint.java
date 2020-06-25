package com.dub.gutenberg.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dub.gutenberg.domain.EditCart;
import com.dub.gutenberg.domain.Order;
import com.dub.gutenberg.domain.OrderState;
import com.dub.gutenberg.domain.UserAndReviewedBooks;
import com.dub.gutenberg.exceptions.OrderNotFoundException;
import com.dub.gutenberg.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class OrderRestEndpoint {
	
	@Autowired 
	private OrderService orderService;
	
	//@Autowired 
	//private ObjectMapper objectMapper;
	
	@RequestMapping(
			value = "/getBooksNotReviewed",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookWebList> getBookNotReviewed(
							@RequestBody UserAndReviewedBooks userAndReviewedBooks) {
	
		try {
			List<String> booksToReview = orderService.getBooksNotReviewed(userAndReviewedBooks);
			BookWebList books = new BookWebList(booksToReview);
			return new ResponseEntity<BookWebList>(books, HttpStatus.OK);
		} catch (ParseException e) {
			return new ResponseEntity<BookWebList>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<BookWebList>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	

	@RequestMapping(
			value = "/updateOrder",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> saveOrder(
									@RequestBody Order order) {
		
		Order newOrder = null;
		
		
		try {
			newOrder = orderService.saveOrder(order, false);// no creation here	
			
			return new ResponseEntity<Order>(newOrder, HttpStatus.OK);	
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);	
		}						
	}
	
	@RequestMapping(
			value = "/checkoutOrder",
			method = RequestMethod.POST)
	public ResponseEntity<Order> checkOrder(
							@RequestBody String orderId) {
	
		try {
			Order newOrder = orderService.checkoutOrder(orderId);
			
			return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(
			value = "/setOrderState",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> setOrderState(
			@RequestParam("orderId") String orderId,
			@RequestParam("state") String state) {
		
		try {			
			Order newOrder = orderService.setOrderState(orderId, stringToState(state));
			return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@RequestMapping(
			value = "/getActiveOrder",
			method = RequestMethod.POST)
	public ResponseEntity<Order> getActiveOrder(@RequestBody String userId) {
		
		try {
			Order order = orderService.getActiveOrder(userId);	
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/editCart",
			method = RequestMethod.POST) 
	public ResponseEntity<Order> editCart(@RequestBody EditCart editCart) {
		
		try {			
			Order order = orderService.editCart(editCart);
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
			
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/addBookToOrder",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> addBookToOrder(
								@RequestParam("orderId") String orderId,
								@RequestParam("bookId") String bookId) {
		
		try {
			Order order = orderService.addBookToOrder(orderId, bookId);	
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		} catch (Exception e) {		
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(
			value = "/createOrder",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		// order does not exist yet	
		Order newOrder;
		try {
			newOrder = orderService.saveOrder(order, true);
			return new ResponseEntity<Order>(newOrder, HttpStatus.CREATED);
			
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
									
	}
	
	
	// really used?
	@RequestMapping(
			value = "/orderById",
			method = RequestMethod.POST,
			consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Order> postOrderById(@RequestBody String orderId) {
	
		try {
			Order order = orderService.getOrderById(orderId);	
				return new ResponseEntity<Order>(order, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
				return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	// really used?
	@RequestMapping(value = "/orderById/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable("orderId") String orderId) {

		try {
			Order order = orderService.getOrderById(orderId);	
			return new ResponseEntity<Order>(order, HttpStatus.OK);			
		} catch (OrderNotFoundException e) {		
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);		
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	

	private OrderState stringToState(String stateStr) {
		
		OrderState state;
		
		switch (stateStr) {
		case "CART":
			state = OrderState.CART;
			break;
		case "PRE_SHIPPING":
			state = OrderState.PRE_SHIPPING;
			break;
		case "SHIPPED":
			state = OrderState.SHIPPED;
			break;
		case "PRE_AUTHORIZE":
			state = OrderState.PRE_AUTHORIZE;
			break;
		default:
			state = OrderState.CART;
		}
		
		return state;
	}

}
