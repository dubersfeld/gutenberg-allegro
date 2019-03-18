package com.dub.client.domain;

import java.util.List;

/** 
 *Encapsulating class used for cart edition 
 * */
public class EditCart {
	
	private String orderId;
	private List<Item> items;
	
	public EditCart() {}
	
	public EditCart(String orderId, List<Item> items) {
		this.orderId = orderId;
		this.items = items;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
		

}
