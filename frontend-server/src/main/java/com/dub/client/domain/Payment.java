package com.dub.client.domain;

public class Payment {
	
	private String cardNumber;
	private String cardName;
	private double amount;
	
	public Payment() {}
			
	public Payment(String cardNumber, String cardName, 
				double amount) {
		this.cardNumber = cardNumber;
		this.cardName = cardName;
		this.amount = amount;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
