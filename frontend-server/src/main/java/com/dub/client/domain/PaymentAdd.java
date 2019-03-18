package com.dub.client.domain;

public class PaymentAdd {

	private String userId;
	private PaymentMethod paymentMethod;
	
	public PaymentAdd() {
		
	}
	
	public PaymentAdd(String userId, PaymentMethod paymentMethod) {
		this.userId = userId;
		this.paymentMethod = paymentMethod;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	

}
