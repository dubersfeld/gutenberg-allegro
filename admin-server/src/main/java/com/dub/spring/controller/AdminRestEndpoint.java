package com.dub.spring.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dub.spring.domain.Book;
import com.dub.spring.domain.Order;
import com.dub.spring.exceptions.BookNotFoundException;
import com.dub.spring.exceptions.OrderNotFoundException;
import com.dub.spring.services.BookService;
import com.dub.spring.services.OrderService;

@RestController
public class AdminRestEndpoint {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping(
			value = "/setBookPrice",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> setBookPrice(
			@RequestParam("id") String bookId,
			@RequestParam("price") int price) {
	
		try {
			Book book = bookService.setBookPrice(bookId, price);
			
			return new ResponseEntity<Book>(book, HttpStatus.OK);
		} catch (BookNotFoundException e) {
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	
	@RequestMapping(
			value = "/setOrderShipped",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> setOrderShipped(
						@RequestParam("id") String orderId) {
				
		try {
			orderService.setShipped(orderId);
					
			return new ResponseEntity<Order>(HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
