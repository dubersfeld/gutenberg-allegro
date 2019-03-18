package com.dub.gutenberg.cluster;

import java.io.Serializable;
import java.util.Map;

public class State implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String cluster_name;
	int compressed_size_in_bytes;
	String cluster_uuid;
	int version;
	String state_uuid;
	String master_node;
	
	Map<String, Node> nodes;
	
	public String getCluster_name() {
		return cluster_name;
	}
	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}
	public int getCompressed_size_in_bytes() {
		return compressed_size_in_bytes;
	}
	public void setCompressed_size_in_bytes(int compressed_size_in_bytes) {
		this.compressed_size_in_bytes = compressed_size_in_bytes;
	}
	public String getCluster_uuid() {
		return cluster_uuid;
	}
	public void setCluster_uuid(String cluster_uuid) {
		this.cluster_uuid = cluster_uuid;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getState_uuid() {
		return state_uuid;
	}
	public void setState_uuid(String state_uuid) {
		this.state_uuid = state_uuid;
	}
	public String getMaster_node() {
		return master_node;
	}
	public void setMaster_node(String master_node) {
		this.master_node = master_node;
	}
	
	public Map<String, Node> getNodes() {
		return nodes;
	}
	public void setNodes(Map<String, Node> nodes) {
		this.nodes = nodes;
	}	
	
	
}
