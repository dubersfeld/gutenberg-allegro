package com.dub.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dub.client.controller.utils.UserUtils;
import com.dub.client.domain.AccountCreation;
import com.dub.client.domain.MyUser;
import com.dub.client.exceptions.DuplicateUserException;
import com.dub.client.services.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserUtils userUtils;//passwordEncoder;
	
	@Autowired UserService userService;
	
	
	@RequestMapping("/register")
	public ModelAndView getRegisterPage(ModelMap model) {
				
		ModelMap params = new ModelMap();
		AccountCreation account = new AccountCreation();
		params.addAttribute("profile", account);
		
		return new ModelAndView("register", params);
	}
	
	@RequestMapping(
			value ="/register",
			method = RequestMethod.POST)
	public String createProfile(@ModelAttribute("profile") AccountCreation account, ModelMap model) {
			
		account.setHashedPassword(passwordEncoder.encode(account.getPassword()));
		
		account.setPassword("");
		
		// create new MyUser
		MyUser user = userUtils.createUser(account);
		
		try {	
			userService.saveUser(user);
	
			return "registerSuccess";
		} catch (DuplicateUserException e) {
			return "registerFailure";
		} catch (Exception e) {
			return "error";
		}
		
	}
	
}
