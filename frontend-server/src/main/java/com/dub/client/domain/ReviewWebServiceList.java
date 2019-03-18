package com.dub.client.domain;

import java.util.List;

public class ReviewWebServiceList {

	private List<Review> reviews;
	
	public ReviewWebServiceList() {
		
	}
	
	public ReviewWebServiceList(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	
	
	
}
