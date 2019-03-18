package com.dub.gutenberg.cluster;

public class ShardDisplay {
	
	private String index;
	private String shard;
	private String prirep;
	private String state;
	private String docs;
	private String store;
	private String ip;
	private String node;
	private String extra;
	
	public ShardDisplay() {
		
	}
	
	public ShardDisplay(
			String index, String shard, String prirep,
			String state, String docs, String store,
			String ip, String node) {
		this.index = index;
		this.shard = shard;
		this.prirep = prirep;
		this.state = state;
		this.docs = docs;
		this.store = store;
		this.ip = ip;
		this.node = node;
	}
	
	public ShardDisplay(
			String index, String shard, String prirep,
			String state, String docs, String store,
			String ip, String node, String extra) {
		this.index = index;
		this.shard = shard;
		this.prirep = prirep;
		this.state = state;
		this.docs = docs;
		this.store = store;
		this.ip = ip;
		this.node = node;
		this.extra = extra;
	}
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getShard() {
		return shard;
	}
	public void setShard(String shard) {
		this.shard = shard;
	}
	public String getPrirep() {
		return prirep;
	}
	public void setPrirep(String prirep) {
		this.prirep = prirep;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDocs() {
		return docs;
	}
	public void setDocs(String docs) {
		this.docs = docs;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	
	
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		
		return index + " " + shard + " " + prirep + " " + state + " " 
		+ docs + " " + store + " " + ip + " " + node;
	}
}
