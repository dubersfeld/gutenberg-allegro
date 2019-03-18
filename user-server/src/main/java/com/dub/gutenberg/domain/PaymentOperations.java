package com.dub.gutenberg.domain;

public class PaymentOperations {
	
	private String userId;
	private PaymentMethod paymentMethod;
	private ProfileOperations op;
	
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
	public ProfileOperations getOp() {
		return op;
	}
	public void setOp(ProfileOperations op) {
		this.op = op;
	}
	
	

}