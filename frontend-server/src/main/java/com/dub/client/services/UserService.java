package com.dub.client.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.dub.client.domain.Address;
import com.dub.client.domain.MyUser;
import com.dub.client.domain.PaymentMethod;

public interface UserService extends UserDetailsService {

	MyUser findByUsername(String username);
	
	MyUser findById(String userId);
	
	MyUser saveUser(MyUser user);
	
	// more advanced methods
	MyUser setPrimaryPaymentMethod(String username, int index);
	MyUser setPrimaryAddress(String username, int index);
	 	 
	void deleteAddress(String username, Address address);
	void addAddress(String username, Address newAddress);
	 
	void deletePaymentMethod(String username, PaymentMethod payMeth);
	void addPaymentMethod(String username, PaymentMethod payMeth);

	
	
}
