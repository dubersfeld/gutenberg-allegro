package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.List;

import com.dub.gutenberg.domain.Book;

public interface SearchService {
	
	public List<Book> searchByDescription(String searchString) throws IOException;
	public List<Book> searchByTags(String searchString) throws IOException;
	public List<Book> searchByTitle(String searchString) throws IOException;
}
