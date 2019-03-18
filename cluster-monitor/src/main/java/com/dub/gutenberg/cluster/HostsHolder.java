package com.dub.gutenberg.cluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dub.gutenberg.config.ClusterConfig;

public class HostsHolder {
		
	private final List<String> hosts;
	private final Map<String, Boolean> up = new HashMap<>();
	private Map<String, Boolean> excluded = new HashMap<>();
	
	private final Map<String, String> containers = new HashMap<>();
	private final Map<String, String> volumes = new HashMap<>();
	
	
	public HostsHolder(ClusterConfig clusterConfig) {
		
		this.hosts = clusterConfig.getIpList();
			
		for (int i = 0; i < hosts.size(); i++) {
			containers.put(hosts.get(i), clusterConfig.getContainerList().get(i));
			volumes.put(hosts.get(i), clusterConfig.getVolumeList().get(i));
			up.put(hosts.get(i), true);
			excluded.put(hosts.get(i), false);
		}
	}

	public List<String> getHosts() {
		return hosts;
	}

	public Map<String, Boolean> getUp() {
		return up;
	}

	public Map<String, String> getContainers() {
		return containers;
	}

	public Map<String, String> getVolumes() {
		return volumes;
	}

	public Map<String, Boolean> getExcluded() {
		return excluded;
	}
	
	public void setExcluded(Map<String, Boolean> excluded) {
		this.excluded = excluded;
	}	
}
