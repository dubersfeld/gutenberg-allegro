package com.dub.client.controller.books;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dub.client.domain.Book;
import com.dub.client.domain.BookSearch;
import com.dub.client.services.BookService;

@Controller
public class SearchController {
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping(
			value = "/search",
			method = RequestMethod.POST
			)
	public ModelAndView searchBook(@ModelAttribute("bookSearch") BookSearch bookSearch) {
		
		List<Book> booksT = bookService.searchBookByTitle(bookSearch.getSearchString());
		List<Book> booksD = bookService.searchBookByDescription(bookSearch.getSearchString());
		List<Book> booksTags = bookService.searchBookByTags(bookSearch.getSearchString());
		
		// Here I want unique books
		
		Set<String> bookIds = new HashSet<>();
		
		List<Book> books = new ArrayList<>();
		List<Book> allBooks = new ArrayList<>();
		allBooks.addAll(booksT);
		allBooks.addAll(booksD);
		allBooks.addAll(booksTags);
		
		// build a list of unique books
		for (Book book : allBooks) {
			if (bookIds.add(book.getId())) {
				books.add(book);
			}
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("books", books);
		
		return new ModelAndView("books/searchResults", params);
	}

}
