package com.dub.gutenberg.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** 
 * A given user can write only one review for the same book
 * */
// POJO, not document
public class Review {
	
	private String id;
	private String bookId;
	private String text;
	private int rating;
	private String userId;
	private Set<String> voterIds;
	private int helpfulVotes;
	
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
	public Set<String> getVoterIds() {
		return voterIds;
	}
	public void setVoterIds(Set<String> voterIds) {
		this.voterIds = voterIds;
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
	public int getHelpfulVotes() {
		return helpfulVotes;
	}
	public void setHelpfulVotes(int helpfulVotes) {
		this.helpfulVotes = helpfulVotes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}