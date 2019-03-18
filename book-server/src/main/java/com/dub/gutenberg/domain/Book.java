package com.dub.gutenberg.domain;


import java.util.ArrayList;
import java.util.List;


public class Book {

	private String id;
	
	private String slug;
	private String title;
	private String publisher;
	private List<String> authors;
	private String description;
	private int price;
	private List<String> tags;
	
	private String categoryId;
	private List<Integer> ratings;
	private List<Item> boughtWith;
	
	public List<Integer> getRatings() {
		return ratings;
	}

	public void setRatings(List<Integer> ratings) {
		this.ratings = ratings;
	}

	public Book() {
		this.tags = new ArrayList<>();
		this.authors = new ArrayList<>();
		this.ratings = new ArrayList<>();
		this.boughtWith = new ArrayList<>();
	}
	
	public Book(Book that) {
		this.id = that.id;
		this.slug = that.slug;
		
		this.title = that.title;
		this.publisher = that.publisher;
		this.authors = that.authors;
		this.description = that.description;
		this.price = that.price;
		this.tags = that.tags;
	
		this.ratings = that.ratings;
		this.boughtWith = that.boughtWith;
	}

	

	public List<Item> getBoughtWith() {
		return boughtWith;
	}

	public void setBoughtWith(List<Item> boughtWith) {
		this.boughtWith = boughtWith;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}