package com.dub.client.domain;

import java.io.Serializable;
//import java.time.Date;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/** 
 * In the frontend all database details should be hidden 
 * */
// POJO, not document
public class Order implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2623188979726019146L;

	private String id;
	
	private String userId;
	
	private State state;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
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


	public static enum State {
		CART,
		PRE_AUTHORIZE,
		
		PRE_SHIPPING,
		SHIPPED
	}

}
