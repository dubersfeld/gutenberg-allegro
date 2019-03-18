package com.dub.client.domain;

public class RatingOutput {
	
	private int rating;
	
	private int count;// number of ratings

	

	
	public int getRating() {
		return rating;
	}




	public void setRating(int rating) {
		this.rating = rating;
	}







	public int getCount() {
		return count;
	}




	public void setCount(int count) {
		this.count = count;
	}




	public String toString()  {
		return rating + " " + count;
	}
	
}
