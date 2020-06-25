package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
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

import com.dub.gutenberg.domain.Address;
import com.dub.gutenberg.domain.MyUser;
import com.dub.gutenberg.domain.PaymentMethod;
import com.dub.gutenberg.exceptions.DuplicateUserException;
import com.dub.gutenberg.exceptions.UserNotFoundException;
import com.dub.gutenberg.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {

	public static final String INDEX = "gutenberg-users";
	public static final String TYPE = "_doc";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public MyUser findById(String userId) throws IOException {
		
		Optional<MyUser> user = userRepo.doFindById(userId);
		
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public MyUser findByUsername(String username) throws IOException {
				
		MyUser user = null;
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		searchSourceBuilder.query(QueryBuilders.termQuery("username.keyword", username));
		//searchSourceBuilder.query(QueryBuilders.matchQuery("username", username));
		searchRequest.source(searchSourceBuilder); 
		searchRequest.indices(INDEX);
	
		SearchResponse response 
		= client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = response.getHits();

		long totalHits = hits.getTotalHits().value;

		SearchHit[] searchHits = hits.getHits();

		if (totalHits == 1) {
			Map<String, Object> sourceAsMap = searchHits[0].getSourceAsMap();
	
			user = objectMapper.convertValue(sourceAsMap, MyUser.class);
			user.setId(searchHits[0].getId());	  		
		} 
		
		return user;// may be null	
	}

	@Override
	public MyUser setPrimaryAddress(String userId, int index) throws IOException {	
		
		// first retrieve user by id
		Optional<MyUser> user = userRepo.doFindById(userId);
		
		if (user.isPresent()) {
			//change primary address
			user.get().setMainShippingAddress(index);
			
			// now update ES document	      
	        return userRepo.doUpdate(user.get());
		} else {
			throw new UserNotFoundException();
		}
		
	}

	@Override
	public MyUser setPrimaryPayment(String userId, int index) throws IOException {	
		// first retrieve user by id
		Optional<MyUser> user = userRepo.doFindById(userId);
				
		if (user.isPresent()) {
			// change primary payment method
			user.get().setMainPayMeth(index);
			
			// update ES document
	        return userRepo.doUpdate(user.get());
		} else {
			throw new UserNotFoundException();
		}
		
	}

	@Override
	public MyUser addAddress(String userId, Address newAddress) throws IOException {	
		// first retrieve user by id
		Optional<MyUser> user = userRepo.doFindById(userId);
		
		if (user.isPresent()) {
			// add new address to list
			List<Address> addresses = user.get().getAddresses();
			addresses.add(newAddress);
							
			// update ES document
	        return userRepo.doUpdate(user.get());
		} else {
			throw new UserNotFoundException();
		}
	
	}

	@Override
	public MyUser addPaymentMethod(String userId, PaymentMethod newPayment) throws IOException {
		// first retrieve user by id
		Optional<MyUser> user = userRepo.doFindById(userId);
		
		if (user.isPresent()) {
			// add new paymentMethod to list
			List<PaymentMethod> payMeths = user.get().getPaymentMethods();
			payMeths.add(newPayment);
					
			// update ES document 	      
			return userRepo.doUpdate(user.get());
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public MyUser deleteAddress(String userId, Address delAddress) throws IOException {	
		// first retrieve user by id
		Optional<MyUser> user = userRepo.doFindById(userId);		  
			
		if (user.isPresent()) {
			// remove address from list
			List<Address> addresses = user.get().getAddresses();
			addresses.remove(delAddress);
		
			// update ES document 	      
	        return userRepo.doUpdate(user.get());
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public MyUser deletePaymentMethod(String userId, PaymentMethod payMeth) throws IOException {
		// first retrieve user by id
		Optional<MyUser> user = userRepo.doFindById(userId);
	
		if (user.isPresent()) {
			// remove payment method from list
			List<PaymentMethod> payMeths = user.get().getPaymentMethods();
			payMeths.remove(payMeth);
					
			// update ES document
	        return userRepo.doUpdate(user.get());//updatedUser;
		} else {
			throw new UserNotFoundException();
		}
	
	}

	@Override
	public String createUser(MyUser user) throws IOException {
		
		// First check if username is already present
		MyUser checkUser = this.findByUsername(user.getUsername());
	
		if (checkUser != null) {
			throw new DuplicateUserException();// username already present
		}
		
		IndexRequest indexRequest = new IndexRequest(INDEX);
		
		Map<String, Object> dataMap = objectMapper.convertValue(user, 
   				new TypeReference<Map<String, Object>>() {});

		indexRequest.source(dataMap);
		
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
	        
	    return response.getId();
	}
	
}
