package com.dub.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);	
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		
		logger.debug("--Application started--");
	
	}
	
	@Bean 
	public RestOperations restTemplate() {
		return new RestTemplate();
	}
	
}

