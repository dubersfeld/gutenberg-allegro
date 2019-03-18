package com.dub.gutenberg.domain;

import java.util.ArrayList;
import java.util.Date;
//import java.time.Date;
import java.util.List;

public class Order {
	
	private String id;
	
	private String userId;
	private OrderState state;
	private List<Item> lineItems;
	private Address shippingAddress;
	private PaymentMethod paymentMethod;
	private int subtotal;
	private Date date;
	
	public Order() {
		this.lineItems = new ArrayList<>();
		this.shippingAddress = new Address();
		this.paymentMethod = new PaymentMethod();
	}
	
	public Order(Order that) {
		this.id = that.id;
		this.state = that.state;
		this.lineItems = that.lineItems;
		this.shippingAddress = that.shippingAddress;
		this.paymentMethod = that.paymentMethod;
		this.subtotal = that.subtotal;
	}

	
	public int getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public OrderState getState() {
		return state;
	}


	public List<Item> getLineItems() {
		return lineItems;
	}


	public void setLineItems(List<Item> lineItems) {
		this.lineItems = lineItems;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
