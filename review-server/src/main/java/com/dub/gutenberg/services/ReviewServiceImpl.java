package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.gutenberg.domain.Review;
import com.dub.gutenberg.exceptions.ReviewNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReviewServiceImpl implements ReviewService{

	public static final String INDEX = "gutenberg-reviews";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private RestHighLevelClient client;

	@Override
	public String createReview(Review review) throws IOException {
		
		IndexRequest indexRequest = new IndexRequest(INDEX);
			
		Map<String, Object> dataMap = objectMapper.convertValue(review, 
   				new TypeReference<Map<String, Object>>() {});

		indexRequest.source(dataMap);
		indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
				
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
	    
	    return response.getId();	
	}

	@Override
	public Review getReviewById(String reviewId) throws IOException {
		
		return this.doGetReviewById(reviewId);	
	}

	@Override
	public List<Review> getReviewsByUserId(String userId) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.termQuery("userId.keyword", userId));
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(INDEX);
	
		SearchResponse response 
		= client.search(searchRequest, RequestOptions.DEFAULT);

		return doGetReviewList(response);
	}

	@Override
	public List<Review> getReviewByBookId(String bookId, String sortBy) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder
			.query(QueryBuilders.termQuery("bookId.keyword", bookId))
			.sort(sortBy, SortOrder.DESC);
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(INDEX);

		SearchResponse response 
			= client.search(searchRequest, RequestOptions.DEFAULT);

		return doGetReviewList(response);

	}

	@Override
	public Optional<Double> getBookRating(String bookId) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest();
					
		AvgAggregationBuilder aggregation 
				= AggregationBuilders
					.avg("book_rating")
					.field("rating");
				
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder
				.query(QueryBuilders.termQuery("bookId.keyword", bookId))
				.aggregation(aggregation);
		
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(INDEX);
	
		SearchResponse response = client
				.search(searchRequest, RequestOptions.DEFAULT);
		
		Avg avg = response.getAggregations().get("book_rating");
		
		Double rating = null;
		if (avg != null) {
			rating = avg.getValue();
		}
	
		return Optional.ofNullable(rating);	
	}

	@Override
	public boolean voteHelpful(String reviewId, String userId, boolean helpful) throws IOException {
		
		// UpdateRequest here
		Review review = doGetReviewById(reviewId);
		
		review.setHelpfulVotes(review.getHelpfulVotes() + (helpful? 1 : 0));
		Set<String> voterIds = review.getVoterIds();
		
		boolean success = voterIds.add(userId);
		if (success) {
				
			review.setVoterIds(voterIds);
			
			this.doUpdate(review);
		}
		
		return success;
		
	}

	
	private Review doGetReviewById(String reviewId) throws IOException {
		
		Review review = null;
		
		GetRequest getRequest = new GetRequest(INDEX);
		//getRequest.type(TYPE);     
	 	getRequest.id(reviewId);
	 	
	 	GetResponse getResponse = null;
	 		
	 	getResponse = client.get(getRequest, RequestOptions.DEFAULT);
  
	 	if (getResponse != null && getResponse.getSourceAsMap() != null) {
	 		review = objectMapper.convertValue(getResponse.getSourceAsMap(), Review.class);	    	
	 		review.setId(reviewId);
		    return review;
	 	} else {
	 		throw new ReviewNotFoundException();
	 	}
	}
	
	private Review doUpdate(Review review) throws IOException {
		// update Review ES document
		UpdateRequest updateRequest = new UpdateRequest(INDEX, review.getId())
		                .fetchSource(true); // Fetch Object after its update
		    
		String reviewJson = objectMapper.writeValueAsString(review);
        
		updateRequest.doc(reviewJson, XContentType.JSON);		        
		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
		        	       
		Review updatedReview = objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Review.class);//.s, toValueType)
		          	        	            	              
		return updatedReview;
	}
	
	private List<Review> doGetReviewList(SearchResponse response) throws JsonProcessingException {

		List<Review> reviews = new ArrayList<>();
		SearchHits hits = response.getHits();

		//long totalHits = hits.getTotalHits();
	
		SearchHit[] searchHits = hits.getHits();

		for (SearchHit hit : searchHits) {
	
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			Review review = objectMapper.convertValue(sourceAsMap, Review.class);
			review.setId(hit.getId());		
			reviews.add(review);
		}		
		
		return reviews;
	}

}
