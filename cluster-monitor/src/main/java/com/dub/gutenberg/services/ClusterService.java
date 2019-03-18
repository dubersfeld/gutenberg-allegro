package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.List;

import com.dub.gutenberg.cluster.State;
import com.dub.gutenberg.cluster.StateDisplay;

public interface ClusterService {

	State getState(String url);
		
	StateDisplay getStateDisplay(String url);
	
	String getShards(String url, String index);
	
	String startNode(String ip) throws IOException;
	
	String stopNode(String ip) throws IOException;
	
	List<String> getExclusions(String url) throws IOException;
	
	String excludeNode(String ip) throws IOException;
	
	String includeNode(String ip) throws IOException;
}
