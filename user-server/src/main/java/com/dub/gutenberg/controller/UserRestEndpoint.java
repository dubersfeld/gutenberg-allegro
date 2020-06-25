package com.dub.gutenberg.controller;

import java.io.IOException;

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

import com.dub.gutenberg.domain.AddressOperations;
import com.dub.gutenberg.domain.MyUser;
import com.dub.gutenberg.domain.PaymentOperations;
import com.dub.gutenberg.domain.Primary;
import com.dub.gutenberg.domain.ProfileOperations;
import com.dub.gutenberg.exceptions.DuplicateUserException;
import com.dub.gutenberg.exceptions.UserNotFoundException;
import com.dub.gutenberg.services.UserService;


@RestController
public class UserRestEndpoint {

	@Value("${baseUsersUrl}")
	private String baseUsersUrl; //= "http://localhost:8084/findById/";
	
	@Autowired 
	private UserService userService;
	
	@RequestMapping(
			value = "/createUser",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createUser(
								@RequestBody MyUser user) {	
			
		try {
			String newUser = userService.createUser(user);
			return new ResponseEntity<String>(baseUsersUrl + newUser, HttpStatus.OK);	
		} catch (DuplicateUserException e) {
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		} catch (IOException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@RequestMapping(
			value = "/findById",
			method = RequestMethod.POST) 
	public ResponseEntity<MyUser> findUserById(
			@RequestBody String userId) {
		
		try {
			MyUser user = userService.findById(userId);		
			return new ResponseEntity<MyUser>(user, HttpStatus.OK);				
		} catch (UserNotFoundException e) {
				return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);			
		} catch (Exception e) {
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
	}
	
	@RequestMapping(
			value = "/deletePaymentMethod",
			method = RequestMethod.POST)
	public ResponseEntity<MyUser> deletePaymentMethod(
				@RequestBody PaymentOperations paymentDelete) {
				
		if (!paymentDelete.getOp().equals(ProfileOperations.DELETE)) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_ACCEPTABLE);
		}
			
		try {
			MyUser user = userService.deletePaymentMethod(
										paymentDelete.getUserId(), 
										paymentDelete.getPaymentMethod());			
			return new ResponseEntity<MyUser>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			System.out.println("/deletePaymentMethod user not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	@RequestMapping(
		value = "/deleteAddress",
		method = RequestMethod.POST)
	public ResponseEntity<MyUser> deleteAddress(
			@RequestBody AddressOperations addressDelete) {
		
		if (!addressDelete.getOp().equals(ProfileOperations.DELETE)) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		try {
			MyUser user = userService.deleteAddress(
									addressDelete.getUserId(), 
									addressDelete.getAddress());
			return new ResponseEntity<MyUser>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
	
	@RequestMapping(
			value = "/addPaymentMethod",
			method = RequestMethod.POST)
	public ResponseEntity<MyUser> addPayment(
			@RequestBody PaymentOperations paymentAdd) {
		
		if (!paymentAdd.getOp().equals(ProfileOperations.ADD)) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		try {
			MyUser user = userService.addPaymentMethod(
								paymentAdd.getUserId(), 
								paymentAdd.getPaymentMethod());
			return new ResponseEntity<MyUser>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(
			value = "/addAddress",
			method = RequestMethod.POST)
	public ResponseEntity<MyUser> addAddress(
			@RequestBody AddressOperations addressAdd) {
			
		if (!addressAdd.getOp().equals(ProfileOperations.ADD)) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		try {
			MyUser user = userService.addAddress(
								addressAdd.getUserId(), 
								addressAdd.getAddress());
			return new ResponseEntity<MyUser>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
	}
	
	@RequestMapping(
			value = "/primaryPayment",
			method = RequestMethod.POST)
	public ResponseEntity<MyUser> setPrimaryPayment(
			@RequestBody Primary primary) {
		
		try {
			MyUser enclume = userService.findByUsername(primary.getUsername());
			MyUser user = userService.setPrimaryPayment(enclume.getId(), primary.getIndex());
			return new ResponseEntity<MyUser>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/primaryAddress",
			method = RequestMethod.POST)
	public ResponseEntity<MyUser> setPrimaryAddress(
			@RequestBody Primary primary) {
		
		try {
			MyUser user = userService.findByUsername(primary.getUsername());
			MyUser updatedUser = userService.setPrimaryAddress(user.getId(), primary.getIndex());
			return new ResponseEntity<MyUser>(updatedUser, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@RequestMapping("/userByName/{username}")
	public ResponseEntity<MyUser> getUserByName(
			@PathVariable("username") String username) {
		
		try {
			MyUser user = userService.findByUsername(username);
			if (user != null) {
				return new ResponseEntity<MyUser>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<MyUser>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}

}
