package com.dub.client.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import com.dub.client.controller.config.ServiceConfig;
import com.dub.client.domain.Book;
import com.dub.client.domain.BookSearch;
import com.dub.client.domain.BookWebList;
import com.dub.client.domain.Review;
import com.dub.client.domain.UserAndReviewedBooks;
import com.dub.client.exceptions.BookNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class BookServiceImpl implements BookService {

	
	private static final String BOOKS = "/books/";
	private static final String BOOK_BY_ID = "/bookById/";
	private static final String BOOKS_BOUGHT_WITH = "/booksBoughtWith/";
	private static final String SORT = "/sort/";
	private static final String OUT_LIMIT = "/outLimit/";
	private static final String BOOKS_NOT_REVIEWED = "/getBooksNotReviewed";
	
	String baseBooksUrl;
	String baseOrdersUrl;
	
	@Autowired
	private RestOperations restTemplate;
		
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ServiceConfig serviceConfig;

	@PostConstruct 
	public void init() {
		baseBooksUrl = serviceConfig.getBaseBooksURL();
		baseOrdersUrl = serviceConfig.getBaseOrdersURL();
	}
	
	@Override
	public Book getBookBySlug(String slug) {
					
		try {
			String booksURI 
			= baseBooksUrl + "/books/" + slug;
				
			ResponseEntity<Book> response 
			= restTemplate.getForEntity(booksURI, Book.class);
			
			return response.getBody();
		} catch (Exception e) {
			System.out.println("Exception caught" + e);
			throw new BookNotFoundException();
		}
	}

	
	@Override
	public Book getBookById(String bookId) {
	
		Book book;
		
			String booksURI 
			= baseBooksUrl + BOOK_BY_ID + bookId;
			
			
			try {
				ResponseEntity<Book> response 
				= restTemplate.getForEntity(booksURI, Book.class);
					
				book = response.getBody(); 
			
				return book;
			} catch (HttpClientErrorException e) {
				if (e.getRawStatusCode() == 404) {
					throw new BookNotFoundException(); 
				} else {
					throw new RuntimeException();
				}
			}
		
	}

	
	@Override
	public List<Book> allBooksByCategory(String categorySlug, String sortBy) {
			
		String booksURI = baseBooksUrl + BOOKS
			+ categorySlug + SORT + sortBy;
				
		ResponseEntity<List<Book>> response 
			= restTemplate.exchange(
				booksURI, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>(){});
				
		List<Book> books = response.getBody();
		 	
		return books;
	}
	

	@Override
	public List<Book> getBooksBoughtWith(String bookId, int outLimit) {
		
		String booksURI = baseBooksUrl + BOOKS_BOUGHT_WITH 
				+ bookId + OUT_LIMIT + outLimit;
				 
		ResponseEntity<List<Book>> response 
			= restTemplate.exchange(
				booksURI, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>(){});
		
		List<Book> outBooks = response.getBody();
		return outBooks;	
	}
	


	@Override
	public List<Book> getBooksNotReviewed(String userId, int outLimit) throws ParseException {
			
		/** 
		 * first step: find all books 
		 * already reviewed by user referenced by userId 
		 * */
			
		List<Review> reviews = reviewService.getReviewsByUserId(userId);
				
		List<String> reviewedBookIds = new ArrayList<>();
		
		for (Review review : reviews) {
			reviewedBookIds.add(review.getBookId().toString());
		}
			
		/** 
		 * second step: find all books 
		 * recently bought by user referenced by userId 
		 * that were not reviewed by user yet.
		 * this step is implemented on order server side
		 * we need to post a List<String> to order server
		 * it is easier here to post an encapsulating object
		 * the helper class is named UserAndReviews
		 * */
		
		UserAndReviewedBooks userAndReviewedBooks 
		= new UserAndReviewedBooks(
								userId, 
								reviewedBookIds, 
								outLimit);
		
		String ordersURI = baseOrdersUrl + BOOKS_NOT_REVIEWED;
					
		List<MediaType> amt = new ArrayList<>(); 
		amt.add(MediaType.APPLICATION_JSON);   
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(amt);// JSON expected from resource server
		
		HttpEntity<UserAndReviewedBooks> requestEntity = 
	    		new HttpEntity<UserAndReviewedBooks>(userAndReviewedBooks, headers);
				
		ResponseEntity<BookWebList> response 
				= restTemplate.exchange(
					ordersURI, HttpMethod.POST, requestEntity, BookWebList.class);
			
		List<Book> booksToReview = new ArrayList<>();
		for (String bookId : response.getBody().getBookIds()) {	
			Book book = this.getBookById(bookId); 
			booksToReview.add(book);
		}
		
		return booksToReview;
	}

	@Override
	public List<Book> searchBookByTitle(String searchString) {
				
		String booksURI = baseBooksUrl + "/searchByTitle";
		
		return this.searchBook(searchString, booksURI);
	}
	
	
	@Override
	public List<Book> searchBookByDescription(String searchString) {
				
		String booksURI = baseBooksUrl + "/searchByDescription";
		
		return this.searchBook(searchString, booksURI);
	}
	
	@Override
	public List<Book> searchBookByTags(String searchString) {
				
		String booksURI = baseBooksUrl + "/searchByTags";
		
		return this.searchBook(searchString, booksURI);
	}
	
	
	private List<Book> searchBook(String searchString, String booksURI) {
					
		List<MediaType> amt = new ArrayList<>(); 
		amt.add(MediaType.APPLICATION_JSON);   
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(amt);// JSON expected from resource server
		
		BookSearch bookSearch = new BookSearch();
		bookSearch.setSearchString(searchString);
		
		HttpEntity<BookSearch> requestEntity = 
	    		new HttpEntity<BookSearch>(bookSearch, headers);
		
		ResponseEntity<List<Book>> response 
			= restTemplate.exchange(
					booksURI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Book>>(){});
	
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			List<Book> books = response.getBody();
					
			return books;	
		} else {
			throw new RuntimeException();
		}		
	}

}
