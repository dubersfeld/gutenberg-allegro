package com.dub.gutenberg.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dub.gutenberg.domain.Review;
import com.dub.gutenberg.exceptions.ReviewNotFoundException;
import com.dub.gutenberg.services.ReviewService;

//import com.dub.gutenberg.domain.String;

@RestController
public class ReviewRestEndpoint {
	
	@Value("${baseReviewsUrl}")
	private String baseReviewsURL;
	
	
	@Autowired
	private ReviewService reviewService;
	

	@RequestMapping(
			value = "/bookRating/{bookId}",
			method = RequestMethod.GET)
	public ResponseEntity<Double> getBookRating(
			@PathVariable("bookId") String bookId) {
		
		Optional<Double> rating;
		try {
			rating = reviewService.getBookRating(bookId);
			if (rating.isPresent()) {
				return new ResponseEntity<Double>(rating.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<Double>(HttpStatus.NO_CONTENT);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Double>(HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	// a user can vote only once
	@RequestMapping(
			value = "/addVote/{reviewId}", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addVote(
					@PathVariable("reviewId") String reviewId,
					@RequestBody ReviewVote reviewVote) {
		try {
			boolean success = reviewService.voteHelpful(reviewId, reviewVote.getUserId(), reviewVote.isHelpful());
			if (success) {
				return ResponseEntity.ok(null);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
				
			} 
		} catch (IOException e) {	
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(
			value = "/createReview", 
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createReview(@RequestBody Review review) throws URISyntaxException {
		
		String newReview;
		try {
			newReview = reviewService.createReview(review);
			URI location = new URI(baseReviewsURL + "/reviewById/" + newReview);
			return ResponseEntity.created(location).body(null);
		
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(value = "/reviewById/{reviewId}")
	public ResponseEntity<Review> getReviewById(
					@PathVariable("reviewId") String reviewId) {
		
		try {
			Review review = reviewService.getReviewById(reviewId);
			return new ResponseEntity<Review>(review, HttpStatus.OK);		
		} catch (ReviewNotFoundException e) {
			return new ResponseEntity<Review>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			return new ResponseEntity<Review>(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	
	@RequestMapping(value = "/reviewsByBookId/{bookId}/sort/{sortBy}")
	public ResponseEntity<List<Review>> getReviewsByBookId(
					@PathVariable("bookId") String bookId,
					@PathVariable("sortBy") String sortBy) {
			
		try {		
			List<Review> reviews = reviewService.getReviewByBookId(bookId, sortBy);	
			return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Review>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(
			value = "/reviewsByUserId",
			method = RequestMethod.POST)
	public ResponseEntity<List<Review>> getReviewsByUserId(
					@RequestBody String userId) {
			
		List<Review> reviews;
		try {
			reviews = reviewService.getReviewsByUserId(userId);
			return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Review>>(HttpStatus.INTERNAL_SERVER_ERROR);	
		}
	}
	
	
}
