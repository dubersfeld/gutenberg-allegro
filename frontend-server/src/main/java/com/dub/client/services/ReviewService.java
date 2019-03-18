package com.dub.client.services;

import java.util.List;
import java.util.Optional;

import com.dub.client.domain.Review;

public interface ReviewService {

	Review createReview(Review review);	
	
	List<Review> getReviewsByUserId(String userId);
	
	List<Review> getReviewsByBookId(
								String bookId, 
								String sortBy);
	
	Optional<Double> getBookRating(String bookId);
	
	void voteHelpful(String reviewId, String userId, boolean helpful);
	boolean hasVoted(String reviewId, String userId);
	
}
