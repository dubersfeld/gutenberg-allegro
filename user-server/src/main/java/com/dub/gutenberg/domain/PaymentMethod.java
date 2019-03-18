package com.dub.gutenberg.domain;


public class PaymentMethod {
	
	private String cardNumber;
	private String name;
	
	public PaymentMethod() {}
	
	public PaymentMethod(String cardNumber, String name) {
		this.cardNumber = cardNumber;
		this.name = name;
	}
	
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override 
	public boolean equals(Object that) {
		if (!(that instanceof PaymentMethod)) {
			return false;
		} else {
			PaymentMethod thatMethod = (PaymentMethod)that;
				
			return this.cardNumber.equals(thatMethod.cardNumber)
			&& this.name.equals(thatMethod.name);
		}
	}

}
