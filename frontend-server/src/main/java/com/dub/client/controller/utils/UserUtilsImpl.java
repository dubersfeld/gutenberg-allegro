package com.dub.client.controller.utils;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dub.client.domain.AccountCreation;
import com.dub.client.domain.Address;
import com.dub.client.domain.MyUser;
import com.dub.client.domain.PaymentMethod;
import com.dub.client.domain.UserAuthority;
import com.dub.client.services.UserService;

@Component
public class UserUtilsImpl implements UserUtils {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(UserUtils.class);
		

	@Autowired
	private UserService userService;
	
	@Override
	public MyUser getLoggedUser(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (session.getAttribute("loggedUser") != null) {
			return (MyUser)session.getAttribute("loggedUser");
		} else {
			MyUser user = userService.findByUsername(auth.getName());
			session.setAttribute("loggedUser", user);
			return user;
		}
	}
	
	@Override
	public MyUser reload(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		logger.warn("reload user " + auth.getName());
		MyUser user = userService.findByUsername(auth.getName());
		session.setAttribute("loggedUser", user);
		return user;
		
	}
	
	
	/*
	@Override
	public MyUser createUser(String username, String password, 
			List<Address> addresses, List<PaymentMethod> paymentMethods,
			UserService userService) {
		
		MyUser user = new MyUser();
		user.setUsername(username);
		user.setEnabled(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setAccountNonExpired(true);
		
		user.setAddresses(addresses);

		user.setPaymentMethods(paymentMethods);
		
		return userService.saveUser(user, password);
	}
*/
	
	@Override
	public void setLoggedUser(HttpSession session, MyUser user) {
		session.setAttribute("loggedUser", user);
		
	}

	@Override
	public MyUser createUser(AccountCreation account) {
			
		Address address = new Address(
								account.getStreet(),
								account.getCity(),
								account.getZip(),
								account.getState(),
								account.getCountry());
			
		PaymentMethod payMeth = new PaymentMethod(
								account.getCardNumber(), 
								account.getNameOnCard());
			
			
		UserAuthority authority = new UserAuthority("ROLE_USER");
			
			
		MyUser user = new MyUser();
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setAddresses(Collections.singletonList(address));
		user.setAuthorities(Collections.singleton(authority));
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setHashedPassword(account.getHashedPassword());
		user.setMainPayMeth(0);
		user.setMainShippingAddress(0);
		user.setPaymentMethods(Collections.singletonList(payMeth));
		user.setUsername(account.getUsername());
			
		return user;
		 
	}
}