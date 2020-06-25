package com.dub.gutenberg.services;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.gutenberg.domain.Category;
import com.dub.gutenberg.exceptions.CategoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class CategoryServiceImpl implements CategoryService {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private RestHighLevelClient client;
	
	
	@Override
	public List<Category> getAllCategories() throws IOException {
		
		List<Category> categories = new ArrayList<>();	
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices("gutenberg-categories");
		SearchResponse response 
				= client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = response.getHits();
		
		//long totalHits = hits.getTotalHits();

		SearchHit[] searchHits = hits.getHits();
		
		for (SearchHit hit : searchHits) {

			Map<String, Object> sourceAsMap = hit.getSourceAsMap();		
			Category cat = objectMapper.convertValue(sourceAsMap, Category.class);
			categories.add(cat);	
		}
		
		return categories;
	}


	@Override
	public Category getCategory(String categorySlug) throws IOException {

		Category cat = null;
		
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		searchSourceBuilder.query(QueryBuilders.termQuery("slug.keyword", categorySlug));
		
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices("gutenberg-categories");
		SearchResponse response 
				= client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits().value;
	
		SearchHit[] searchHits = hits.getHits();
		
		if (totalHits == 1) {
			
			Map<String, Object> sourceAsMap = searchHits[0].getSourceAsMap();
			
			cat = objectMapper.convertValue(sourceAsMap, Category.class);
			cat.setId(searchHits[0].getId());
	
			return cat;		
		} else {
			throw new CategoryNotFoundException();
		}
	}
}