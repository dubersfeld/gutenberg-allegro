package com.dub.gutenberg.domain;


public class Item {
	
	private String bookId;
	private int quantity;
	
	public Item() {}
	
	public Item(Item that) {
		this.bookId = that.bookId;
		this.quantity = that.quantity;
	}
	
	public Item(String bookId, int quantity) {
		this.bookId = bookId;
		this.quantity = quantity; 
	}
	
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
