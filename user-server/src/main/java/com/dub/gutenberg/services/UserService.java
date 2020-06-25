package com.dub.gutenberg.services;

import java.io.IOException;

import com.dub.gutenberg.domain.Address;
import com.dub.gutenberg.domain.MyUser;
import com.dub.gutenberg.domain.PaymentMethod;

public interface UserService {
 
	MyUser findById(String userId) throws IOException;	
	MyUser findByUsername(String username) throws IOException;

	// all profile changes
	MyUser setPrimaryAddress(String userId, int index) throws IOException;		
	MyUser setPrimaryPayment(String userId, int index) throws IOException;
	MyUser addAddress(String userId, Address newAddress) throws IOException;
	MyUser addPaymentMethod(String userId, PaymentMethod newPayment) throws IOException ;
	MyUser deleteAddress(String userId, Address delAddress) throws IOException;
	MyUser deletePaymentMethod(String userId, PaymentMethod payMeth) throws IOException;
	
	// new user registration
	String createUser(MyUser user) throws IOException;
	 
}