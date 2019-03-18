package com.dub.client.controller.utils;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.dub.client.domain.AccountCreation;
import com.dub.client.domain.Address;
import com.dub.client.domain.MyUser;
import com.dub.client.domain.PaymentMethod;
import com.dub.client.services.UserService;

public interface UserUtils {

	public MyUser getLoggedUser(HttpSession session);
	
	public void setLoggedUser(HttpSession session, MyUser user);
	
	public MyUser reload(HttpSession session);
	
	/*
	public MyUser createUser(String username, String password, 
			List<Address> addresses, List<PaymentMethod> paymentMethods,
			UserService userService);
	*/
	public MyUser createUser(AccountCreation account);
}