package com.dub.gutenberg.repository;

import java.io.IOException;
import java.util.Optional;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.dub.gutenberg.config.ServiceConfig;
import com.dub.gutenberg.domain.Book;
import com.dub.gutenberg.exceptions.BookNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BookRepository {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static final String BOOKS = "gutenberg-books";
	public static final String TYPE = "_doc";
	
	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ServiceConfig serviceConfig;
	
	/** 
	 * private method that connects to book server
	 * */
	public Optional<Book> getBookById(String bookId) {
			
		String booksURL
			= serviceConfig.getBaseBooksURL() + "/bookById/" + bookId;
	
		Book book = null;
		
		ResponseEntity<Book> response 
			= restTemplate.getForEntity(
					booksURL, Book.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			book = response.getBody();
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
