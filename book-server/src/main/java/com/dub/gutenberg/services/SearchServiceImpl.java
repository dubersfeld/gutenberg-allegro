package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.dub.gutenberg.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class SearchServiceImpl implements SearchService {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static final String BOOKS = "gutenberg-books";
	public static final String ORDERS = "gutenberg-orders";
	public static final String TYPE = "_doc";
	
	@Autowired
	private RestHighLevelClient client;
	
	@Override
	public List<Book> searchByTitle(String searchString) throws IOException {

		SearchResponse response 
		= myResponseBuilder("title", searchString);
		
		return doGetResponse(response);		
	}

	@Override
	public List<Book> searchByDescription(String searchString) throws IOException {

		SearchResponse response 
		= myResponseBuilder("description", searchString);
		
		return doGetResponse(response);	
	}
	
	@Override
	public List<Book> searchByTags(String searchString) throws IOException {

		SearchResponse response 
		= myResponseBuilder("tags", searchString);
		
		return doGetResponse(response);	
	}

	private List<Book> doGetResponse(SearchResponse response) {
		
		List<Book> books = new ArrayList<>();
		
		SearchHits hits = response.getHits();

		Book book;
		//long totalHits = hits.getTotalHits();

		SearchHit[] searchHits = hits.getHits();

		for (SearchHit hit : searchHits) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			book = objectMapper.convertValue(sourceAsMap, Book.class);
			book.setId(hit.getId());
			books.add(book);
		}
		
		return books;
	}
	
	private SearchResponse myResponseBuilder(String field, String searchString) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
			
		searchSourceBuilder.query(
				QueryBuilders.matchQuery(field, searchString));
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(BOOKS);
		
		SearchResponse response 
			= client.search(searchRequest, RequestOptions.DEFAULT);
		
		return response;
	} 
	
}
