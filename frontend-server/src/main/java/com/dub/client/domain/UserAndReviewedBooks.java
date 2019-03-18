package com.dub.client.domain;


import java.util.List;

// encapsulating helper class
public class UserAndReviewedBooks {
	
	private String userId;
	private List<String> reviewedBookIds;
	private int outLimit;
	
	public UserAndReviewedBooks() {
		
	}

	public UserAndReviewedBooks(
							String userId, 
							List<String> reviewedBookIds,
							int outLimit) {
		this.userId = userId;
		this.reviewedBookIds = reviewedBookIds;	
		this.outLimit = outLimit;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getReviewedBookIds() {
		return reviewedBookIds;
	}

	public void setReviewedBookIds(List<String> reviewedBookIds) {
		this.reviewedBookIds = reviewedBookIds;
	}

	public int getOutLimit() {
		return outLimit;
	}

	public void setOutLimit(int outLimit) {
		this.outLimit = outLimit;
	}
	
}
