package com.dub.gutenberg.cluster;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;

import com.dub.gutenberg.client.MyHandler;
import com.dub.gutenberg.client.StompClient;
import com.dub.gutenberg.listeners.StompConnectEventListener;

public class ClusterMonitor implements Runnable {

	private List<String> allocatedURLs = new ArrayList<>();
	
	private long DELAY = 10000L;
		
	@Autowired 
	private RestOperations restTemplate;
	
	private HostsHolder hostsHolder;
	
	@Autowired 
	private MyHandler myHandler;
	
	@Autowired 
	private StompClient stompClient;
	
	private StompConnectEventListener stompConnectEventListener;
	
	
	public ClusterMonitor(StompConnectEventListener stompConnectEventListener,
			HostsHolder hostsHolder) {
		this.stompConnectEventListener = stompConnectEventListener;
		this.hostsHolder = hostsHolder;
		
		for (String host : hostsHolder.getHosts()) {
			allocatedURLs.add("http://" + host + ":9200");
		}
	}
	
	public void run() {

		while (this.stompConnectEventListener != null 
				&& this.stompConnectEventListener.isEnable()) {
			
			// check cluster
			for (String host : hostsHolder.getHosts()) {
				String stateURI = "http://" + host + ":9200/_cluster/state";
				System.out.println("ClusterMonitor for "
						+ host);
				try {
					ResponseEntity<State> response 
					= restTemplate.getForEntity(stateURI, State.class);
		
					if (response.getStatusCode().equals(HttpStatus.OK)) {
						hostsHolder.getUp().put(host, true);
					} else {
						hostsHolder.getUp().put(host, false);
					}
				} catch (ResourceAccessException e) {
					hostsHolder.getUp().put(host, false);
				} catch (HttpServerErrorException e) {
					hostsHolder.getUp().put(host, false);
				
				} catch (Exception e) {
					hostsHolder.getUp().put(host, false);
				}
			}
					
			// publish here	
			if (stompClient.getStompSession() != null) {				
				myHandler.sendState(stompClient.getStompSession());	
			}
				
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
