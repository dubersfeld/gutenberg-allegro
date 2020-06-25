package com.dub.gutenberg.repository;

import java.io.IOException;
import java.util.Optional;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dub.gutenberg.domain.MyUser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserRepository {
	
	public static final String INDEX = "gutenberg-users";
	public static final String TYPE = "_doc";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private RestHighLevelClient client;


	public Optional<MyUser> doFindById(String userId) throws IOException {
		
		MyUser user = null;
		
		GetRequest getRequest = new GetRequest(INDEX);
		getRequest.type(TYPE);     
	 	getRequest.id(userId);
	 	
	 	GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
	 		
	 	if (getResponse != null && getResponse.getSourceAsMap() != null) {
	 		user = objectMapper.convertValue(getResponse.getSourceAsMap(), MyUser.class);	    	
	 		user.setId(userId);
	 	} 
	 	
	 	return Optional.ofNullable(user);
	}
	
	public MyUser doUpdate(MyUser user) throws IOException {
		// update MyUser ES document
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, user.getId())
		                .fetchSource(true).setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); // Fetch Object after its update
		    
		String userJson = objectMapper.writeValueAsString(user);
        
		updateRequest.doc(userJson, XContentType.JSON);		        
		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
		        	       
		MyUser updatedUser = objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), MyUser.class);//.s, toValueType)
		          	        	            	              
		return updatedUser;
	}

}
