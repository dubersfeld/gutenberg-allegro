package com.dub.gutenberg.domain;


public class AddressOperations {

	private String userId;
	private Address address;
	private ProfileOperations op;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ProfileOperations getOp() {
		return op;
	}

	public void setOp(ProfileOperations op) {
		this.op = op;
	}


	
}