package com.dub.spring.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.spring.domain.Book;
import com.dub.spring.exceptions.BookNotFoundException;
import com.dub.spring.repository.BookRepository;


@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
		

	@Override
	public Book setBookPrice(String bookId, int price) throws IOException {
		Optional<Book> book = bookRepository.getBookById(bookId);
			
		if (book.isPresent()) {
			Book thisBook = book.get();
			thisBook.setPrice(price); 
			return bookRepository.doUpdateBook(book.get());
	
		} else {
			throw new BookNotFoundException();
		}
		
	}
}