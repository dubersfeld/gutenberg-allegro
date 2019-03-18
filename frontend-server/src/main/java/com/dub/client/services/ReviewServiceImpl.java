package com.dub.client.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.dub.client.controller.config.ServiceConfig;
//import com.dub.client.controller.config.ServiceConfig;
import com.dub.client.domain.Review;
import com.dub.client.domain.ReviewVote;


@Service
public class ReviewServiceImpl implements ReviewService {
			
	public static final String CREATE_REVIEW = "/createReview";
	public static final String REVIEWS_BY_BOOK_ID = "/reviewsByBookId/";
	public static final String BOOK_RATING = "/bookRating/";
	public static final String REVIEW_BY_ID = "/reviewById/";
	public static final String REVIEWS_BY_USER_ID = "/reviewsByUserId";
	public static final String ADD_VOTE = "/addVote/";
	

	@Autowired
	ServiceConfig serviceConfig;
	
	
	@Autowired
	private RestOperations restTemplate;
		
	@Override
	public Review createReview(Review review) {
			
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> amt = new ArrayList<>();
		amt.add(MediaType.APPLICATION_JSON);
		headers.setAccept(amt);
				
		String reviewsURI = serviceConfig.getBaseReviewsURL() + CREATE_REVIEW;
		//String reviewsURI = "http://localhost:8082" + "/createReview";
		
		ResponseEntity<Review> response 
		= restTemplate.postForEntity(reviewsURI, review, Review.class);
		
		return review;
	}


	@Override
	public List<Review> getReviewsByBookId(String bookId, String sortBy) {

		//String reviewsURI = "http://localhost:8082/reviewsByBookId/" + bookId
		//		+ "/sort/" + sortBy;
	
		String reviewsURI = serviceConfig.getBaseReviewsURL() + REVIEWS_BY_BOOK_ID + bookId
				+ "/sort/" + sortBy;
				
		ResponseEntity<List<Review>> response 
		= restTemplate.exchange(
			reviewsURI, HttpMethod.POST, null, new ParameterizedTypeReference<List<Review>>(){});
	
		List<Review> reviews = response.getBody();//.getReviews();
											
		return reviews;
	}

	@Override
	public Optional<Double> getBookRating(String bookId) {
			
		//String reviewsURI = "http://localhost:8082/bookRating/" + bookId;
		String reviewsURI = serviceConfig.getBaseReviewsURL() + BOOK_RATING + bookId;
			
		ResponseEntity<Double> response 
		= restTemplate.getForEntity(reviewsURI, Double.class);
	
		if (response.getStatusCode() == HttpStatus.OK) {
			return Optional.of(response.getBody());
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public boolean hasVoted(String reviewId, String userId) {
		
		//String reviewURI = "http://localhost:8082/reviewById/" + reviewId;
		String reviewURI = serviceConfig.getBaseReviewsURL() + REVIEW_BY_ID + reviewId;
			
		
		ResponseEntity<Review> response 
		= restTemplate.getForEntity(reviewURI, Review.class);
		
		Review review = response.getBody();
		Set<String> voterIds = review.getVoterIds();
		
		return voterIds.contains(userId);
	}

	@Override
	public void voteHelpful(String reviewId, String userId, boolean helpful) {
		
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> amt = new ArrayList<>();
		amt.add(MediaType.APPLICATION_JSON);
		headers.setAccept(amt);
			
		ReviewVote reviewVote = new ReviewVote();
		reviewVote.setHelpful(helpful);
		reviewVote.setUserId(userId);
		
		//Boolean help = Boolean.valueOf(helpful);
				
		String reviewURI = serviceConfig.getBaseReviewsURL() 
				+ ADD_VOTE + reviewId;
				
		// actual POST
		restTemplate.postForEntity(
			reviewURI, reviewVote, Boolean.class);
			
	}


	@Override
	public List<Review> getReviewsByUserId(String userId) {
		
		//String reviewsURI = "http://localhost:8082" + "/reviewsByUserId";
		String reviewsURI = serviceConfig.getBaseReviewsURL() + REVIEWS_BY_USER_ID;
					
		HttpEntity<String> request 
					= new HttpEntity<>(userId, null);
		
		// actual POST		
		ResponseEntity<List<Review>> response 
		= restTemplate.exchange(
			reviewsURI, HttpMethod.POST, request, new ParameterizedTypeReference<List<Review>>(){});
	
		return response.getBody();
	}
}
