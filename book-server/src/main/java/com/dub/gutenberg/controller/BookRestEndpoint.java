package com.dub.gutenberg.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dub.gutenberg.domain.Book;
import com.dub.gutenberg.domain.BookSearch;
import com.dub.gutenberg.exceptions.BookNotFoundException;
import com.dub.gutenberg.services.BookService;
import com.dub.gutenberg.services.SearchService;


@RestController
public class BookRestEndpoint {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(
			value = "/searchByTitle",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> searchByTitle(@RequestBody BookSearch bookSearch) {
		List<Book> books = null;
		try {
			books = searchService.searchByTitle(bookSearch.getSearchString());
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/searchByDescription",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> searchByDescription(@RequestBody BookSearch bookSearch) {
		List<Book> books = null;
		try {
			books = searchService.searchByDescription(bookSearch.getSearchString());
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/searchByTags",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> searchByTags(@RequestBody BookSearch bookSearch) {
		List<Book> books = null;
		try {
			books = searchService.searchByTags(bookSearch.getSearchString());
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/** 
	 * To complete
	 * */
	@RequestMapping(
			value = "/booksBoughtWith/{bookId}/outLimit/{outLimit}",
			method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getBooksBoughtWith(
			@PathVariable("bookId") String bookId,
			@PathVariable("outLimit") int outLimit) {
		try {
			List<Book> books = bookService.getBooksBoughtWith(bookId, outLimit);
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	
		} catch (Exception e) {
			System.out.println("Exception caught " + e);
			return new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	
	@RequestMapping("/books/{bookSlug}")
	public ResponseEntity<Book> getBookBySlug(@PathVariable("bookSlug") String bookSlug) {
		try {
			Book book = bookService.getBookBySlug(bookSlug);	
			return new ResponseEntity<Book>(book, HttpStatus.OK);
		} catch (BookNotFoundException e) {
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping("/bookById/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") String bookId) {
			
		try {
			Book book = bookService.getBookById(bookId);
			return new ResponseEntity<Book>(book, HttpStatus.OK);	
		} catch (BookNotFoundException e) {
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);		
		}	
	}

	@RequestMapping(
			value = "/books/{categorySlug}/sort/{sortBy}",
			produces = MediaType.APPLICATION_JSON_VALUE)
			
	public ResponseEntity<List<Book>> allBooksByCategory(
				@PathVariable("categorySlug") String categorySlug,
				@PathVariable() String sortBy) {
		
		System.err.printf("/books/%s/sort/%s begin\n", categorySlug, sortBy);
		List<Book> books;
		try {
			books = bookService.allBooksByCategory(categorySlug, sortBy);
			return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
	
		} catch (Exception e) {
			return new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}	
}