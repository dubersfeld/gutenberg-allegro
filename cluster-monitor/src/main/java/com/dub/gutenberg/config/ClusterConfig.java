package com.dub.gutenberg.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


public class ClusterConfig {
	
	@Value("${ips}")
	String ips;
	
	@Value("${containers}")
	String containers;
	
	@Value("${volumes}")
	String volumes;
	
	private final List<String> ipList = new ArrayList<>();
	private final List<String> containerList = new ArrayList<>();
	private final List<String> volumeList = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		String[] ipArray = ips.split(",");
		String[] containerArray = containers.split(",");
		String[] volumeArray = volumes.split(",");
		
		for (int i = 0; i < containerArray.length; i++) {
			ipList.add(ipArray[i]);
		}
		
		for (int i = 0; i < containerArray.length; i++) {
			containerList.add(containerArray[i]);
		}
		for (int i = 0; i < containerArray.length; i++) {
			volumeList.add(volumeArray[i]);
		}
	}
	
	@Bean
	public RestHighLevelClient client() {
		RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(
                        		"172.19.0.2",
                        		9200,
                        		"http"),
                        new HttpHost(
                        		"172.19.0.3",
                        		9200,
                        		"http"),
                        new HttpHost(
                        		"172.19.0.4",
                        		9200,
                        		"http")
                )
		);
	
		return client;
	}

	public List<String> getIpList() {
		return ipList;
	}

	public List<String> getContainerList() {
		return containerList;
	}

	public List<String> getVolumeList() {
		return volumeList;
	}
	
	
}
