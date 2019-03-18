package com.dub.gutenberg.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateDisplay {

	private List<String> hosts = new ArrayList<>();
	private List<Node> nodes = new ArrayList<>();
	
	// shards as String
	private Map<String, List<ShardDisplay>> shards = new HashMap<>();
	
	private Map<String, Boolean> exclusions = new HashMap<>();
	
		
	public List<String> getHosts() {
		return hosts;
	}
	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Map<String, List<ShardDisplay>> getShards() {
		return shards;
	}

	public void setShards(Map<String, List<ShardDisplay>> shards) {
		this.shards = shards;
	}

	public Map<String, Boolean> getExclusions() {
		return exclusions;
	}

	public void setExclusions(Map<String, Boolean> exclusions) {
		this.exclusions = exclusions;
	}	
	
}
