package com.dub.client.services;

import org.springframework.security.access.prepost.PreAuthorize;

import com.dub.client.domain.Payment;

@PreAuthorize("isFullyAuthenticated()")
public interface PaymentService {

	public boolean authorizePayment(Payment payment); 
}
