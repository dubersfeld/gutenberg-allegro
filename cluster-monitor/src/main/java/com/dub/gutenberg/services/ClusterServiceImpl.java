package com.dub.gutenberg.services;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.dub.gutenberg.cluster.HostsHolder;
import com.dub.gutenberg.cluster.State;
import com.dub.gutenberg.cluster.StateDisplay;
import com.dub.gutenberg.utils.DisplayUtils;
import com.dub.gutenberg.utils.StreamGobbler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ClusterServiceImpl implements ClusterService {
	
	@Value("${clientUrl}")
	private String clientUrl;	
	
	@Autowired
	private HostsHolder hostsHolder;
	
	@Autowired 
	private RestOperations restTemplate;
	
	@Autowired 
	private DisplayUtils displayUtils;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public State getState(String url) {
	
		String stateURI = url + "/_cluster/state";
		
		ResponseEntity<State> response 
			= restTemplate.getForEntity(stateURI, State.class);
				
		return response.getBody();	
	}

	@Override
	public StateDisplay getStateDisplay(String url) {
	
		State state = this.getState(url);
		
		StateDisplay display = displayUtils.state2Display(state);
		
		return display;
	}

	@Override
	public String getShards(String url, String index) {
		String shardsURI = url + "/_cat/shards/" + index;
	
		ResponseEntity<String> response 
			= restTemplate.getForEntity(shardsURI, String.class);
			
		return response.getBody();	
	}

	@Override
	public String startNode(String ip) throws IOException {
		
		ProcessBuilder builder = new ProcessBuilder();
		
		String launchCommand = this.buildStartCommand(ip);
			
		builder.command("bash", "-c", "cd " + clientUrl + " ; pwd ; ls ; " 
										+ launchCommand); 
		Process process;
		
		process = builder.start();
		OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
			
		StreamGobbler streamGobbler = 
	    		  new StreamGobbler(process.getInputStream(), System.out::println);    		
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		
        	output.flush();
 			
		return "STARTED";
	}

	@Override
	public String stopNode(String ip) throws IOException {
	
		ProcessBuilder builder = new ProcessBuilder();
		
		String stopCommand = this.buildStopCommand(ip);
		
		builder.command("bash", "-c", "cd " + clientUrl + " ; pwd ; ls ; " 
				+ stopCommand); 
			
		Process process;
		
		process = builder.start();
		//process.
		OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
			
		StreamGobbler streamGobbler = 
	    		  new StreamGobbler(process.getInputStream(), System.out::println);    		
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		
		
            	output.flush();
   	
		return "STOPPED";
	}
	
	private String buildStartCommand(String ip) {
		
		String container = hostsHolder.getContainers().get(ip);
		String volume = hostsHolder.getVolumes().get(ip);
				
		String command = "./restartNode.sh " 
				+ container + " " + ip + " " + volume;
		
		return command;		 
	}
	
	private String buildStopCommand(String ip) {
		
		String container = hostsHolder.getContainers().get(ip);
		
		String command = "./stopContainer.sh " 
				+ container;
	
		return command;		 
	}

	@Override
	public List<String> getExclusions(String url) throws IOException {
		/** 
		 * Here we use the _cluster/setting API
		 * */
		
		String settingsURI = url + "/_cluster/settings";
			
		/* probable bug here */
		try {
			ResponseEntity<String> response 
			= restTemplate.getForEntity(settingsURI, String.class);
						
			JsonNode root = objectMapper.readTree(response.getBody());
			
			JsonNode ips = root.path("transient").path("cluster").path("routing")
					.path("allocation").path("exclude").path("_ip");
			 
			List<String> excludedIps = new ArrayList<>();
			
			if (!ips.asText().equals("")) {
				String[] array = ips.asText().split(",");
			    	  
				for (int i = 0; i < array.length; i++) {    
					System.out.println(array[i]);
					excludedIps.add(array[i]);
				}// for  
			}// if
			
			return excludedIps;	
		} catch (IOException e) {
			System.out.println("Exception caught "
					+ e);
			return null;
		}
	}

	@Override
	public String excludeNode(String ip) throws IOException {
		
		System.out.println("excludeNode " + ip);
		this.updateExclusions(ip, Actions.EXCLUDE);
			
		return "EXCLUDED";
	}

	@Override
	public String includeNode(String ip) throws IOException {
	
		this.updateExclusions(ip, Actions.INCLUDE);
		
		return "INCLUDED";
	}
	
	
	private void updateExclusions(String ip, Actions action) throws IOException {
				
		String exclusionsStr = "";
		String excludeURI = "";
		String activeURL = "";
			
		// first find an active node
		List<String> hosts = hostsHolder.getHosts();
		
		for (String host : hosts) {
			if (hostsHolder.getUp().get(host)) {
				excludeURI = "http://" + host + ":9200/_cluster/settings";
				activeURL =  "http://" + host + ":9200";
				break;
			} 
		}
			
		// retrieve actual exclusion list
		// a Set would be cleaner here
		List<String> excludedIps = this.getExclusions(activeURL);
			
		if (action.equals(Actions.INCLUDE) ) {
			if (excludedIps.contains(ip)) {
				excludedIps.remove(ip);
			}
		} else if (action.equals(Actions.EXCLUDE)) {
			if (!excludedIps.contains(ip)) {
				excludedIps.add(ip);
			}
		}
		
		exclusionsStr  = excludedIps.stream().collect(Collectors.joining(","));
					
		String excludeJson = "{\"transient\":{\"cluster.routing.allocation.exclude._ip\":\"" 
						+ exclusionsStr + "\"}}";
						
		List<MediaType> amt = new ArrayList<>();
		amt.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(amt);// JSON expected from resource server
				
		HttpEntity<?> requestEntity = 
				new HttpEntity<>(excludeJson, headers);

		ResponseEntity<String> response = restTemplate.exchange(excludeURI, HttpMethod.PUT, requestEntity, String.class);				
	}
	
	private enum Actions {
		EXCLUDE, INCLUDE;
	}
}
