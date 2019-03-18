package com.dub.gutenberg.domain;

import javax.validation.constraints.NotNull;

/** Not a document */
public class Address {
	
	@NotNull
	private String street = "";
	@NotNull
	private String city = "";
	private String zip = "";
	@NotNull
	private String state = "";
	@NotNull
	private String country = "";
	
	public Address() {}
	
	public Address(String street, String city, String zip, 
			String state, String country) {
		this.street = street;
		this.city = city;
		this.zip = zip;
		this.state = state != null ? state : "";
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
	
	@Override
	public boolean equals(Object that) {
		if (!(that instanceof Address)) {
			return false;
		} else {
			Address thatAddress = (Address)that;
			return this.street.equals(thatAddress.street)
			&& this.city.equals(thatAddress.city)
			&& this.zip.equals(thatAddress.zip)
			&& this.state.equals(thatAddress.state)
			&& this.country.equals(thatAddress.country);
		}
	}
}

