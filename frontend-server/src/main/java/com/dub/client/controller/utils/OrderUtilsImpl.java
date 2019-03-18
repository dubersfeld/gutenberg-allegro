package com.dub.client.controller.utils;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dub.client.domain.MyUser;
import com.dub.client.domain.Order;
import com.dub.client.services.OrderService;

@Component
public class OrderUtilsImpl implements OrderUtils {


	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Override
	public String getActiveOrderId(HttpSession session) {
		
		// first get logged user
		MyUser user = userUtils.getLoggedUser(session);
				
		if (session.getAttribute("activeOrderId") == null) {
			Order order = null;
			
			// first try to retrieve a persisted order from DB
			Optional<Order> ord = orderService.getActiveOrder(user.getId());
			
			if (!ord.isPresent()) {// no persisted active order found, create a new order
			
				order = new Order();
				order.setState(Order.State.CART);
				order.setUserId(user.getId());
				
				// initial creation
				order = orderService.createOrder(order);	
			} else {
				// a persisted active order was found
				order = ord.get();
			}
			
			session.setAttribute("activeOrderId", order.getId());
		}

		return (String) session.getAttribute("activeOrderId");
	}

	@Override
	public void invalidActiveOrderId(HttpSession session) {
		if (session.getAttribute("activeOrderId") != null) {
			session.setAttribute("activeOrderId", null);
		}
	}
}