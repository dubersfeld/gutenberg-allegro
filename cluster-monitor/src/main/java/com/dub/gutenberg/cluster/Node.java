package com.dub.gutenberg.cluster;

import java.io.Serializable;

public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String transport_address;
	private boolean master;
	private boolean active;
	private boolean excluded;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTransport_address() {
		return transport_address;
	}
	public void setTransport_address(String transport_address) {
		this.transport_address = transport_address;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isExcluded() {
		return excluded;
	}
	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}
	
}
