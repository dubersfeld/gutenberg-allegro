package com.dub.client.domain;

public class ReviewVote {

	private String userId;
	private boolean helpful;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isHelpful() {
		return helpful;
	}
	public void setHelpful(boolean helpful) {
		this.helpful = helpful;
	}
	
}
