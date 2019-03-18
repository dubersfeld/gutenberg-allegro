package com.dub.client.controller.users;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dub.client.controller.utils.UserUtils;
import com.dub.client.domain.Address;
import com.dub.client.domain.MyUser;
import com.dub.client.domain.PaymentMethod;
import com.dub.client.services.UserService;


@Controller
public class UserController {
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(
			value = "/changeShippingAddress", 
			method = RequestMethod.GET)
	public ModelAndView preChangeShippingAddress(
			@RequestParam("redirectUrl") String redirect,
			ModelMap model, HttpServletRequest request) {
				
		HttpSession session = request.getSession();
		MyUser user = userUtils.getLoggedUser(session);
		
		List<Address> addresses = user.getAddresses();
		List<SelectableAndIndex> addIndexes = new ArrayList<>();
		
		int index = 0;
		
		for (Address address : addresses) {
			addIndexes.add(new SelectableAndIndex(address, index++));
		}
		
		model.addAttribute("addIndexes", addIndexes);
		model.addAttribute("address", new Address());
		model.addAttribute("indexForm", new IndexForm());
		session.setAttribute("redirect", redirect);
		return new ModelAndView("users/changeShippingAddress");
	}
	
	
	@RequestMapping(
			value = "/selectShippingAddress", 
			method = RequestMethod.POST)
	public ModelAndView changeShippingAddress(
			@Valid @ModelAttribute("indexForm") IndexForm form,
			BindingResult result, ModelMap model,
			HttpServletRequest request) {
		
		if (result.hasErrors()) {
			return new ModelAndView("users/selectShippingAddress", model);
		} else {
			
			HttpSession session = request.getSession();
	
			String redirect = (String) session.getAttribute("redirect");
		
			int index = form.getIndex();
			boolean primary = form.isPrimary();
				
			MyUser user = userUtils.getLoggedUser(session);	
			String username = user.getUsername();
			
			if (primary) {
				userService.setPrimaryAddress(username, index);
				// reloading needed
				user = userUtils.reload(session);
			}
			
			Address shipAdd = user.getAddresses().get(index);
			
			session.setAttribute("shipAdd", shipAdd);
			
			return new ModelAndView("redirect:/" + redirect, model);
		}
		
	}
	
	
	@RequestMapping(value = "/changePaymentMethod", method = RequestMethod.GET)
	public ModelAndView changePaymentMethod(
			@RequestParam("redirectUrl") String redirect,
			ModelMap model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		MyUser user = userUtils.getLoggedUser(session);
	
		List<PaymentMethod> payMeths = user.getPaymentMethods();
		List<SelectableAndIndex> payMethIndexes = new ArrayList<>();
		
		int index = 0;
		
		for (PaymentMethod payMeth : payMeths) {
			payMethIndexes.add(new SelectableAndIndex(payMeth, index++));
		}
		
		session.setAttribute("redirect", redirect);
		model.addAttribute("payMethIndexes", payMethIndexes);
		model.addAttribute("PayMeth", new PaymentMethod());
		model.addAttribute("indexForm", new IndexForm());
		
		return new ModelAndView("users/changePaymentMethod");
	}
	
	
	@RequestMapping(value = "/selectPaymentMethod", method = RequestMethod.POST)
	public ModelAndView changePaymentMethod (
			@Valid @ModelAttribute("indexForm") IndexForm form,
			BindingResult result, ModelMap model,
			HttpServletRequest request) {
		
		if (result.hasErrors()) {
			return new ModelAndView("users/changePaymentMethod", model);
		} else {
			
			HttpSession session = request.getSession();
	
			String redirect = (String)session.getAttribute("redirect");
			int index = form.getIndex();
			boolean primary = form.isPrimary();
			
			MyUser user = userUtils.getLoggedUser(session);
			
			String username = user.getUsername();
			
			if (primary) {
				userService.setPrimaryPaymentMethod(username, index);
				// reloading user needed
				user = userUtils.reload(session);
			}
			
			PaymentMethod payMeth = user.getPaymentMethods().get(index);
			
			session.setAttribute("payMeth", payMeth);
			
			return new ModelAndView("redirect:/" + redirect, model);
		}
		
	}
	
	
	@RequestMapping(value = "/getProfile", method = RequestMethod.GET)
	public String getProfile(Model model, HttpServletRequest request) {
				
		HttpSession session = request.getSession();
		MyUser user = userUtils.getLoggedUser(session);
	
		List<Address> addresses = user.getAddresses();
		List<PaymentMethod> payMeths = user.getPaymentMethods();
		
		model.addAttribute("addresses", addresses);
		model.addAttribute("payMeths", payMeths);
		
		// used for delete only
		model.addAttribute("address", new Address());
		model.addAttribute("payMeth", new PaymentMethod());
		
		return "users/profile";
	}
	
	
	@RequestMapping(value = "/deleteAddress", method = RequestMethod.POST)
	public String deleteAddress(
			@Valid @ModelAttribute("address") Address form,
			HttpServletRequest request)  {
				
		HttpSession session = request.getSession();
		MyUser user = userUtils.getLoggedUser(session);
		
		userService.deleteAddress(user.getUsername(), form);
		// reloading needed
		user = userUtils.reload(session);
		
		return "users/deleteAddressSuccess";
	}
	
	
	@RequestMapping(value = "/addAddress", method = RequestMethod.GET)
	public String getAddressForm(ModelMap model) {
		model.addAttribute("address", new Address());
		return "users/createAddress";
	}
	
	
	@RequestMapping(value = "/addAddress", method = RequestMethod.POST)
	public String postAddress(
			@Valid @ModelAttribute("address") Address form, HttpServletRequest request)  {
				
		HttpSession session = request.getSession();
		MyUser user = userUtils.getLoggedUser(session);
		
		userService.addAddress(user.getUsername(), form);
		// reload needed because user was change on DB
		user = userUtils.reload(session);
						
		return "users/createAddressSuccess";
	}
	
	
	@RequestMapping(value = "/deletePaymentMethod", method = RequestMethod.POST)
	public String deletePaymentMethod(
			@Valid @ModelAttribute("payMeth") PaymentMethod form,
			HttpServletRequest request)  {
			
		HttpSession session = request.getSession();
		MyUser user = userUtils.getLoggedUser(session);
			
		userService.deletePaymentMethod(user.getUsername(), form);
		// reloading needed
		user = userUtils.reload(session);
			
		return "users/deletePaymentMethodSuccess";
	}
	
	
	@RequestMapping(value = "/addPaymentMethod", method = RequestMethod.GET)
	public String getPaymentMethodForm(ModelMap model) {
		model.addAttribute("payMeth", new PaymentMethod());
		return "users/createPaymentMethod";
	}
	
	@RequestMapping(value = "/addPaymentMethod", method = RequestMethod.POST)
	public String postPaymentMethod(
			@Valid @ModelAttribute("payMeth") PaymentMethod form,
			HttpServletRequest request)  {
				
		HttpSession session = request.getSession();
		MyUser user = userUtils.getLoggedUser(session);
			
		userService.addPaymentMethod(user.getUsername(), form);
			
		// reloading needed
		user = userUtils.reload(session);
						
		return "users/createPaymentMethodSuccess";
	}
	
		
	private static class IndexForm {
		
		int index;
		boolean primary = false;

		public int getIndex() {
			return index;
		}

		public boolean isPrimary() {
			return primary;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setPrimary(boolean primary) {
			this.primary = primary;
		}
		
	}
}
	