package com.dub.gutenberg.domain;



public class BookRating {
	
	private String bookId;
	private Double bookRating;
	

	public Double getBookRating() {
		return bookRating;
	}
	public void setBookRating(Double bookRating) {
		this.bookRating = bookRating;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

}