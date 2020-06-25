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
		
		System.out.println("Fucking /authorizePayment begin");
	
		if (result.hasErrors()) {
			return "payments/authorize";
		} else {
			
			System.out.println("Fucking /authorizePayment sator");
					
			HttpSession session = request.getSession();
				
			String orderId = orderUtils.getActiveOrderId(session);
				
			System.out.println("Fucking /authorizePayment arepo");
			
			int totalSave = (int)session.getAttribute("total");
			
			System.out.println("Fucking /authorizePayment tenet");
			
			Order order = orderService.getOrderById(orderId);
			
			System.out.println("Fucking /authorizePayment opera");
			
			// change state to PRE_AUTHORIZE
			if (order.getState() == Order.State.CART) {
				order = orderService.checkoutOrder(order.getId());
			}
						
			System.out.println("Fucking /authorizePayment rotas");
					
			// recalculate totals
			try {
				
				
				System.out.println("Fucking /authorizePayment tiens");
		
				// recalculate total
				order = orderService.getOrderById(orderId);
					
				System.out.println("Fucking /authorizePayment fume");
				
				if (totalSave == order.getSubtotal()) {
						
					System.out.println("Fucking /authorizePayment Assa");
					
					Payment payment = new Payment();
					payment.setAmount(order.getSubtotal()/100.0);
					payment.setCardNumber(form.getCardNumber());
					payment.setCardName(form.getName());
							
					boolean paymentSuccess = paymentService.authorizePayment(payment);
						
					System.out.println("Fucking /authorizePayment c'est du");
				
					
					// transition order state to CART in case of payment failure
					if (!paymentSuccess) {
						System.out.println("Fucking /authorizePayment Traore");
						order = orderService
								.setCart(order.getId());
					}
					
					System.out.println("Fucking /authorizePayment French");
					session.setAttribute("paymentSuccess", paymentSuccess);
					// redirect to OrderController 
					System.out.println("Fucking /authorizePayment ad patres");
					
					return "redirect:/payment";
				} else  {	
					orderService.setCart(orderId);
					System.out.println("Fucking /authorizePayment ad libitum");
					
					return "redirect:/getCart";
				}
			} catch (RuntimeException e) {	
				System.out.println("Exception caught " + e);	
				return "error";
			}
		}
	}
}

