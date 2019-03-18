package com.dub.client.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/** 
 * A given user can write only one review for the same book
 * */

// POJO not document
public class Review {
	
	private String id;
	
	private String bookId;
	private Date date;
	private String title;
	private String text;
	private int rating;
	private String userId;
	private String username;
	private int helpfulVotes;
	private Set<String> voterIds;
	
	public Review() {
		this.voterIds = new HashSet<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getHelpfulVotes() {
		return helpfulVotes;
	}

	public void setHelpfulVotes(int helpfulVotes) {
		this.helpfulVotes = helpfulVotes;
	}

	public Set<String> getVoterIds() {
		return voterIds;
	}

	public void setVoterIds(Set<String> voterIds) {
		this.voterIds = voterIds;
	}
	
}
