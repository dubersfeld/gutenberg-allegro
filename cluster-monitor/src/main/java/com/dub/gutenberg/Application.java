package com.dub.gutenberg;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.dub.gutenberg.client.MyHandler;
import com.dub.gutenberg.client.StompClient;
import com.dub.gutenberg.cluster.ClusterMonitor;
import com.dub.gutenberg.cluster.HostsHolder;
import com.dub.gutenberg.config.ClusterConfig;
import com.dub.gutenberg.listeners.StompConnectEventListener;
import com.dub.gutenberg.services.ClusterService;


@SpringBootApplication
public class Application implements CommandLineRunner {
	
	@Bean
	public ClusterConfig clusterConfig() {
		return new ClusterConfig();
	}
	
	@Bean
	@DependsOn("clusterConfig")
	public HostsHolder hostsHolder() {
		return new HostsHolder(clusterConfig());
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestOperations restTemplate() {
		return new RestTemplate();
	}
	
	
	@Bean("stompConnectEventListener")
	public StompConnectEventListener stompConnectEventListener() {
		return new StompConnectEventListener();// {
	}
	
	@Bean 
	@DependsOn({"stompClient", "stompConnectEventListener", "hostsHolder"})
	public ClusterMonitor clusterMonitor() {
		return new ClusterMonitor(stompConnectEventListener(), hostsHolder());
	}
	
	@Bean("myHandler")
	public MyHandler myHandler() {
		return new MyHandler();
	}
	
	
	@Bean("stompClient")
	@DependsOn("myHandler")
	public StompClient stompClient() {
		StompClient stompClient = new StompClient(myHandler());	
		return stompClient;
	}
	
	@Override
	public void run(String... args) throws Exception {
		clusterMonitor().run();
		
	} 
}
