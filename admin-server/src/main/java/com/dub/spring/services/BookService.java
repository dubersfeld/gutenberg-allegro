package com.dub.spring.services;

import java.io.IOException;

import com.dub.spring.domain.Book;

public interface BookService {
	
	Book setBookPrice(String bookId, int price) throws IOException;
}