package com.dub.client.domain;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.dub.spring.domain.UserAuthority;

public class MyUser {

	private String id;
	
	private String username;
	
	private String hashedPassword;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	
	private Set<UserAuthority> authorities;
	
	private List<Address> addresses;
	private List<PaymentMethod> paymentMethods;
	
	private int mainPayMeth;
	private int mainShippingAddress;
	
	public Set<UserAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<UserAuthority> authorities) {
		this.authorities = authorities;
	}

	public MyUser() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
		this.authorities = new HashSet<>();
		this.authorities.add(new UserAuthority("ROLE_USER"));
		this.addresses = new ArrayList<>();
		this.paymentMethods = new ArrayList<>();
		this.mainPayMeth = 0;
		this.mainShippingAddress = 0;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public int getMainPayMeth() {
		return mainPayMeth;
	}

	public void setMainPayMeth(int mainPayMeth) {
		this.mainPayMeth = mainPayMeth;
	}

	public int getMainShippingAddress() {
		return mainShippingAddress;
	}

	public void setMainShippingAddress(int mainShippingAddress) {
		this.mainShippingAddress = mainShippingAddress;
	}


	
		
}
