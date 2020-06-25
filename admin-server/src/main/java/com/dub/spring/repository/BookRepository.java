package com.dub.spring.repository;

import java.io.IOException;
import java.util.Optional;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.dub.spring.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BookRepository {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static final String BOOKS = "gutenberg-books";
	public static final String TYPE = "_doc";
	
	@Autowired
	private RestHighLevelClient client;
	
	/** 
	 * private method that connects to book server
	 * @throws IOException 
	 * */
	public Optional<Book> getBookById(String bookId) throws IOException {
	
		Book book = null;	
		
		GetRequest getRequest = new GetRequest(BOOKS);
		getRequest.type(TYPE);     
	 	getRequest.id(bookId);
	 	
	 	GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
	 	
	 	if (getResponse != null && getResponse.getSourceAsMap() != null) {
	 			book = objectMapper.convertValue(getResponse.getSourceAsMap(), Book.class);	    	
	 			book.setId(bookId);
	 	} 	
	 		    
	 	return Optional.ofNullable(book);
	}
		
	
	public Book doUpdateBook(Book book) throws IOException {
		
		// update Order ES document
		UpdateRequest updateRequest = new UpdateRequest(BOOKS, TYPE, book.getId())
										                .fetchSource(true);
										       	    						    
		String userJson = objectMapper.writeValueAsString(book);
								        
		updateRequest.doc(userJson, XContentType.JSON);		        
		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
										        	       
		Book updatedBook = 
					objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Book.class);
			
		return updatedBook;	
	}
}
