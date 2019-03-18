package com.dub.gutenberg.services;


import java.io.IOException;
import java.util.List;

import com.dub.gutenberg.domain.Book;


public interface BookService {
	
	List<Book> allBooksByCategory(String categorySlug, String sortBy) throws IOException;
		
	Book getBookBySlug(String slug) throws IOException;
	
	Book getBookById(String bookId) throws IOException;
		
	// more advanced methods
	List<Book> getBooksBoughtWith(String bookId, int limit) throws IOException;
}