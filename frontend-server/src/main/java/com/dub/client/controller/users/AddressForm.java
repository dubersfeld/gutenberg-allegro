package com.dub.client.controller.users;

public class AddressForm {

	private String street;
	private String city;
	private String zip;
	private String state;
	private String country;
	
	public AddressForm() {}
	
	public AddressForm(String street, String city, String zip, 
			String state, String country) {
		this.street = street;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.country = country;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

}