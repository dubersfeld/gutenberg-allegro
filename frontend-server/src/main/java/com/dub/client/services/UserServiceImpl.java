package com.dub.client.services;



import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import com.dub.client.controller.config.ServiceConfig;
import com.dub.client.domain.Address;
import com.dub.client.domain.AddressOperation;
import com.dub.client.domain.MyUser;
import com.dub.client.domain.PaymentMethod;
import com.dub.client.domain.PaymentOperation;
import com.dub.client.domain.Primary;
import com.dub.client.domain.ProfileOperations;
import com.dub.client.domain.UserPrincipal;
import com.dub.client.exceptions.UserNotFoundException;



@Service
public class UserServiceImpl implements UserService {

	private static final String USER_BY_NAME = "/userByName/"; 
	private static final String USER_BY_ID = "/findById/"; 
	private static final String DELETE_ADDRESS = "/deleteAddress"; 
	private static final String ADD_ADDRESS = "/addAddress"; 
	private static final String DELETE_PAYMENT_METHOD = "/deletePaymentMethod"; 
	private static final String ADD_PAYMENT_METHOD = "/addPaymentMethod"; 
	private static final String PRIMARY_PAYMENT = "/primaryPayment";
	private static final String PRIMARY_ADDRESS = "/primaryAddress";
	
	
	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ServiceConfig serviceConfig;
	
	private String baseUsersUrl;
	
	@PostConstruct 
	public void init() {
		baseUsersUrl = serviceConfig.getBaseUsersURL();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		MyUser user = this.findByUsername(username);	
        UserPrincipal principal = new UserPrincipal(user);
        
        return principal;   
	}
	
	
	@Override
	public MyUser findByUsername(String username) {
		
		MyUser user = new MyUser();
		user.setUsername("Carol");
			
		String usersURI = baseUsersUrl + USER_BY_NAME + username;
		 
		
		ResponseEntity<MyUser> response 	
		= restTemplate.getForEntity(usersURI, MyUser.class);
	
		MyUser altUser = response.getBody();
			
		return altUser;
	}


	@Override
	public MyUser findById(String userId) {
	
		String usersURI = baseUsersUrl + USER_BY_ID;
		
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, userId, MyUser.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			throw new UserNotFoundException(); 
		}
	}


	@Override
	public MyUser setPrimaryPaymentMethod(String username, int index) {
	
		String usersURI = baseUsersUrl + PRIMARY_PAYMENT;
		 
		Primary primary = new Primary();
		
		primary.setIndex(index);
		primary.setUsername(username);
		
		try {
			
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == 404) {
				throw new UserNotFoundException(); 
			} else {
				throw new RuntimeException(); 
			}
		}
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, primary, MyUser.class);
	
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			throw new UserNotFoundException(); 
		}
	}


	@Override
	public MyUser setPrimaryAddress(String username, int index) {
	
		String usersURI = baseUsersUrl + PRIMARY_ADDRESS;
		 
		Primary primary = new Primary();
		
		primary.setIndex(index);
		primary.setUsername(username);
		
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, primary, MyUser.class);
	
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			throw new UserNotFoundException(); 
		}
		
	}


	@Override
	public void deleteAddress(String username, Address address) {
	
		String usersURI = baseUsersUrl + DELETE_ADDRESS;
			 
		AddressOperation addOp = new AddressOperation();
		
		addOp.setUserId(this.findByUsername(username).getId());
		addOp.setAddress(address);
		addOp.setOp(ProfileOperations.DELETE);
					
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, addOp, MyUser.class);
	
		if (response.getStatusCode() == HttpStatus.OK) {
			return;
		} else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new UserNotFoundException(); 
		} else if (response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
			throw new RuntimeException(); 
		}
	}


	@Override
	public void addAddress(String username, Address newAddress) {
	
		String usersURI = baseUsersUrl + ADD_ADDRESS;
		 
		AddressOperation addOp = new AddressOperation();
		
		addOp.setUserId(this.findByUsername(username).getId());
		addOp.setAddress(newAddress);
		addOp.setOp(ProfileOperations.ADD);
		
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, addOp, MyUser.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			return;
		} else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new UserNotFoundException(); 
		} else if (response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
			throw new RuntimeException(); 
		}
		
	}


	@Override
	public void deletePaymentMethod(String username, PaymentMethod payMeth) {
	
		String usersURI = baseUsersUrl + DELETE_PAYMENT_METHOD;
		 
		PaymentOperation deleteOp = new PaymentOperation();
		
		deleteOp.setUserId(this.findByUsername(username).getId());
		deleteOp.setPaymentMethod(payMeth);
		deleteOp.setOp(ProfileOperations.DELETE);
			
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, deleteOp, MyUser.class);
	
		if (response.getStatusCode() == HttpStatus.OK) {
			return;
		} else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new UserNotFoundException(); 
		} else if (response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
			throw new RuntimeException(); 
		}
		
	}


	@Override
	public void addPaymentMethod(String username, PaymentMethod newPayMeth) {
		
		String usersURI = baseUsersUrl + ADD_PAYMENT_METHOD;
		 
		PaymentOperation addOp = new PaymentOperation();
		
		addOp.setUserId(this.findByUsername(username).getId());
		addOp.setPaymentMethod(newPayMeth);
		addOp.setOp(ProfileOperations.ADD);
					
		ResponseEntity<MyUser> response 
		= restTemplate.postForEntity(usersURI, addOp, MyUser.class);
	
		if (response.getStatusCode() == HttpStatus.OK) {
			return;
		} else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			throw new UserNotFoundException(); 
		} else {
			throw new RuntimeException(); 
		}
		
	}

	@Override
	public MyUser saveUser(MyUser user) {
				
		String usersURI = baseUsersUrl + "/createUser";
			
		ResponseEntity<String> response 
		= restTemplate.postForEntity(usersURI, user, String.class);
	
		return null;
	}
}