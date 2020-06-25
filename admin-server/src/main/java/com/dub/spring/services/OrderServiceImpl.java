package com.dub.spring.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.spring.domain.Order;
import com.dub.spring.domain.OrderState;
import com.dub.spring.exceptions.OrderNotFoundException;
import com.dub.spring.repository.OrderRepository;


@Service
public class OrderServiceImpl implements OrderService {

	@Autowired 
	private OrderRepository orderRepository;
		
	@Override
	public void setShipped(String orderId) throws IOException {
		
		Order order;
		
		Optional<Order> doc = orderRepository.findOrderById(orderId);
	
		if (doc.isPresent()) {
			order = doc.get();
			if (order.getState().equals(OrderState.PRE_SHIPPING))
			order.setState(OrderState.SHIPPED);
			
			orderRepository.doUpdateOrder(order);		
		} else {
			throw new OrderNotFoundException();
		}
	}
}
