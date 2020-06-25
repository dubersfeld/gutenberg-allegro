package com.dub.client.controller.payments;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dub.client.controller.utils.OrderUtils;
import com.dub.client.domain.Order;
import com.dub.client.domain.Payment;
import com.dub.client.domain.PaymentMethod;
import com.dub.client.services.OrderService;
import com.dub.client.services.PaymentService;



@Controller
public class PaymentController {
		
	@Autowired
	private OrderService orderService;
		
	@Autowired
	private OrderUtils orderUtils;
	
	@Autowired 
	private PaymentService paymentService;
	
	@RequestMapping(value = "/authorizePayment", method = RequestMethod.POST)
	public String authorizePayment(
			@Valid @ModelAttribute("paymentForm") PaymentMethod form,
			BindingResult result, ModelMap model,
			HttpServletRequest request) throws InterruptedException {
		
		if (result.hasErrors()) {
			return "payments/authorize";
		} else {
									
			HttpSession session = request.getSession();
				
			String orderId = orderUtils.getActiveOrderId(session);
				
			int totalSave = (int)session.getAttribute("total");
			
			Order order = orderService.getOrderById(orderId);
			
			// change state to PRE_AUTHORIZE
			if (order.getState() == Order.State.CART) {
				order = orderService.checkoutOrder(order.getId());
			}
									
			// recalculate totals
			try {
				// recalculate total
				order = orderService.getOrderById(orderId);
					
				if (totalSave == order.getSubtotal()) {
						
					Payment payment = new Payment();
					payment.setAmount(order.getSubtotal()/100.0);
					payment.setCardNumber(form.getCardNumber());
					payment.setCardName(form.getName());
							
					boolean paymentSuccess = paymentService.authorizePayment(payment);
						
					// transition order state to CART in case of payment failure
					if (!paymentSuccess) {						
						order = orderService
								.setCart(order.getId());
					}
					
					session.setAttribute("paymentSuccess", paymentSuccess);
					// redirect to OrderController 
					
					return "redirect:/payment";
				} else  {	
					orderService.setCart(orderId);
					
					return "redirect:/getCart";
				}
			} catch (RuntimeException e) {	
				System.out.println("Exception caught " + e);	
				return "error";
			}
		}
	}
}

