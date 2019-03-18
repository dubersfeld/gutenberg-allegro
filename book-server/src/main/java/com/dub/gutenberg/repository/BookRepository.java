package com.dub.gutenberg.repository;

import java.io.IOException;
import java.util.Optional;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.gutenberg.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookRepository {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static final String BOOKS = "gutenberg-books";
	public static final String ORDERS = "gutenberg-orders";
	public static final String TYPE = "_doc";
	
	@Autowired
	private RestHighLevelClient client;
	

	public Optional<Book> getBookById(String bookId) throws IOException {
			
		Book book = null;	
		
		GetRequest getRequest = new GetRequest(BOOKS);
		getRequest.type(TYPE);     
	 	getRequest.id(bookId);
	 	
	 	GetResponse getResponse = null;
	 	 		
	 	getResponse = client.get(getRequest, RequestOptions.DEFAULT);
	 	
	 	if (getResponse != null && getResponse.getSourceAsMap() != null) {
	 		book = objectMapper.convertValue(getResponse.getSourceAsMap(), Book.class);	    	
	 		book.setId(bookId);
	 	} 	
	 	    
	 	return Optional.ofNullable(book);
	}
}
