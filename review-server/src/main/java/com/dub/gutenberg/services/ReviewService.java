package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.dub.gutenberg.domain.Review;


public interface ReviewService {

	String createReview(Review review) throws IOException;	
	
	Review getReviewById(String reviewId) throws IOException;	
	
	List<Review> getReviewsByUserId(String userId) throws IOException;
	
	List<Review> getReviewByBookId(
								String bookId, 
								String sortBy) throws IOException;
	
	Optional<Double> getBookRating(String bookId) throws IOException;
	
	boolean voteHelpful(String reviewId, String userId, boolean helpful) throws IOException;
	
}