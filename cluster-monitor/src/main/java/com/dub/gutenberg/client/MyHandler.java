package com.dub.gutenberg.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.zookeeper.KeeperException;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.dub.gutenberg.cluster.HostsHolder;
import com.dub.gutenberg.cluster.ShardDisplay;
import com.dub.gutenberg.cluster.StateDisplay;
import com.dub.gutenberg.services.ClusterService;
import com.dub.gutenberg.utils.DisplayUtils;
//import com.dub.spring.cluster.DisplayCluster;
//import com.dub.spring.services.ZooKeeperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyHandler extends StompSessionHandlerAdapter {
	
	@Autowired
	private ClusterService clusterService;
	
	@Autowired
	private DisplayUtils displayUtils;
	
	@Autowired
	private HostsHolder hostsHolder;
			
	private ObjectMapper objectMapper = new ObjectMapper();
		
	@Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {     		
			sendState(stompSession);// actual handling here
	}
	
	public void sendState(StompSession stompSession) {
	
		// look for an active node
		String activeHost = "";
		for (String host : hostsHolder.getUp().keySet()) {
			if (hostsHolder.getUp().get(host)) {
				activeHost = host;
				break;
			}
		}
		
		String url = "http://" + activeHost + ":9200";
		
		try {				
			// retrieve all exclusions
			List<String> exclusions = clusterService.getExclusions(url);		
			Map<String, Boolean> excludedIps = hostsHolder.getExcluded();
			for (String ip : excludedIps.keySet()) {
				if (exclusions.contains(ip)) {
					excludedIps.replace(ip, true);
				} else {
					excludedIps.replace(ip, false);
				}
			}
			
			hostsHolder.setExcluded(excludedIps);
			
			StateDisplay display = clusterService.getStateDisplay(url);
			
			String bookShards = clusterService.getShards(url, "gutenberg-books");
			String userShards = clusterService.getShards(url, "gutenberg-users");
			
			List<ShardDisplay> bookShardsList = displayUtils.shards2DisplayList(bookShards);
			List<ShardDisplay> userShardsList = displayUtils.shards2DisplayList(userShards);
			
			display.getShards().put("gutenberg-books", bookShardsList);
			display.getShards().put("gutenberg-users", userShardsList);
				
			stompSession.send("/app/notify", objectMapper.writeValueAsString(display) );
		} catch (Exception e) {
			System.out.println("MyHandler caught exception " + e);
		}
	}
}