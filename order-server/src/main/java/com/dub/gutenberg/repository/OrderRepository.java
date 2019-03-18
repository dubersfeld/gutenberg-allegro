package com.dub.gutenberg.repository;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dub.gutenberg.domain.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderRepository {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static final String BOOKS = "gutenberg-books";
	public static final String ORDERS = "gutenberg-orders";
	public static final String TYPE = "_doc";
	
	@Autowired
	private RestHighLevelClient client;
	
	
	@PostConstruct
	public void init() {
		objectMapper.setDateFormat(sdf);
	}
	
	public Optional<Order> findOrderById(String orderId) throws IOException {

		GetRequest getRequest = new GetRequest(ORDERS);
		getRequest.type(TYPE);     
	 	getRequest.id(orderId);
	 	
	 	GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
	 	 		
	 	Order order = null;
	 	if (getResponse != null && getResponse.getSourceAsMap() != null) {
	 		order = objectMapper.convertValue(getResponse.getSourceAsMap(), Order.class);	    	
	 		order.setId(orderId);	 	
	 	}
		
	 	return Optional.ofNullable(order);	
	}
	
	
	public Order doUpdateOrder(Order oldOrder) throws IOException {
		
		// update Order ES document
		UpdateRequest updateRequest = new UpdateRequest(ORDERS, TYPE, oldOrder.getId())
									                .fetchSource(true);
									                						    
		String userJson = objectMapper.writeValueAsString(oldOrder);
							        
		updateRequest.doc(userJson, XContentType.JSON);		        
		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
									        	       
		Order updatedOrder = 
				objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Order.class);
		
		return updatedOrder;
	
	}

}
