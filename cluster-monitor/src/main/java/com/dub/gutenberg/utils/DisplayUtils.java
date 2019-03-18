package com.dub.gutenberg.utils;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dub.gutenberg.cluster.HostsHolder;
import com.dub.gutenberg.cluster.Node;
import com.dub.gutenberg.cluster.ShardDisplay;
import com.dub.gutenberg.cluster.State;
import com.dub.gutenberg.cluster.StateDisplay;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DisplayUtils {
	
	ObjectMapper objectMapper = new ObjectMapper();
		
	@Autowired
	private HostsHolder hostsHolder;
	
	
	public StateDisplay state2Display(State state) {
		
		StateDisplay display = new StateDisplay();
				
		for (String host : hostsHolder.getHosts()) {	
			display.getHosts().add(host);
			display.getNodes().add(null);
			display.getExclusions().putAll(hostsHolder.getExcluded());
		} 
		
		for (String node : state.getNodes().keySet()) {
			
			String transport_address = state.getNodes().get(node).getTransport_address();
			String base_address 
				= Strings.substring(
						transport_address, 0, transport_address.length()-5);
				
			int index = hostsHolder.getHosts().indexOf(base_address);
			
			state.getNodes().get(node).setActive(true);	
			state.getNodes().get(node).setExcluded(hostsHolder.getExcluded().get(base_address));
			
			
			display.getNodes().set(index, state.getNodes().get(node));	
				
			if (state.getMaster_node().equals(node)) {
				display.getNodes().get(index).setMaster(true);
			}	
		}
		
		
		// avoid null nodes
		for (int i = 0; i < display.getNodes().size(); i++) {
			if (display.getNodes().get(i) == null) {
				Node node = new Node();
				node.setActive(false);
				display.getNodes().set(i, node);
			} 
		}
			
		return display;
		
	}
	
	public List<String> splitShards(String shards) {
		
		List<String> shardsList = new ArrayList<>(); 
		
		String[] array = shards.split("\n");
			
		for (int i = 0; i < array.length; i++) {
			shardsList.add(array[i]);
		}

		return shardsList;
	}
	
	public ShardDisplay shard2Display(String shard) {
		
		int length = 0;
		ShardDisplay display = null;
		try {
			String[] array = shard.split("\\s+");
		
			length = array.length;
			
			switch (length) {
				case 8:
					display = new ShardDisplay(
						array[0], array[1], array[2], array[3], 
						array[4], array[5], array[6], array[7]);
					break;
				case 6:
					display = new ShardDisplay(
						array[0], array[1], array[2], array[3], 
						null, null, array[4], array[5]);
					break;
				case 4:
					display = new ShardDisplay(
						array[0], array[1], array[2], array[3], null, null, null, null); 
					break;
				case 10:
					display = new ShardDisplay(
						array[0], array[1], array[2], array[3], null, null, array[4], array[5],
						array[6] + " " + array[7] + " " +  array[8] + " " + array[9]); 
					break;
		
				case 12:
					display = new ShardDisplay(
							array[0], array[1], array[2], array[3], array[4], array[5],
							array[6], array[7], 
							" " + array[8] + " " + array[9] + " " + array[10] + " " + array[11]); 
						break;
				default:
			}
			
			return display;
		} catch (ArrayIndexOutOfBoundsException e) {
			return new ShardDisplay();
		}
	}
	
	public List<ShardDisplay> shards2DisplayList(String shards) {
		
		// get shards as strings
		List<String> shardsList = splitShards(shards);
		
		List<ShardDisplay> displays = new ArrayList<>();
		
		for (String shard : shardsList) {
			displays.add(shard2Display(shard));
		}
		
		return displays;
	}

}
