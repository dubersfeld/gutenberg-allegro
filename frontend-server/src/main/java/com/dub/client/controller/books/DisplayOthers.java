package com.dub.client.controller.books;

import java.util.List;

import com.dub.client.domain.Book;

public class DisplayOthers {
		
	private Book book;
	private List<Book> otherBooks;
		
	public DisplayOthers(Book book, List<Book> otherBooks) {
		this.book = book;
		this.otherBooks = otherBooks;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public List<Book> getOtherBooks() {
		return otherBooks;
	}

	public void setOtherBooks(List<Book> otherBooks) {
		this.otherBooks = otherBooks;
	}
}
