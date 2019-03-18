package com.dub.client.controller.config;

import org.springframework.context.annotation.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Configuration
public class ServiceConfig {

	@Value("${baseBooksUrl}")
	private String baseBooksURL;
	
	@Value("${baseReviewsUrl}")
	private String baseReviewsURL;


	public String getBaseBooksURL() {
		return baseBooksURL;
	}

	public void setBaseBooksURL(String baseBooksURL) {
		this.baseBooksURL = baseBooksURL;
	}
	
	
	@Value("${baseOrdersUrl}")
	private String baseOrdersURL;
	
	
	
	public String getBaseOrdersURL() {
		return baseOrdersURL;
	}

	public void setBaseOrdersURL(String baseOrdersURL) {
		this.baseOrdersURL = baseOrdersURL;
	}
	
	public String getBaseReviewsURL() {
		return baseReviewsURL;
	}

	public void setBaseReviewsURL(String baseReviewsURL) {
		this.baseReviewsURL = baseReviewsURL;
	}
	

	
	
	@Value("${baseUsersUrl}")
	private String baseUsersURL;
	
	

	public String getBaseUsersURL() {
		return baseUsersURL;
	}

	public void setBaseUsersURL(String baseUsersURL) {
		this.baseUsersURL = baseUsersURL;
	}	
	
	
	/*
	@Value("${spring.redis.host}")
	private String redisServer;
	
	@Value("${spring.redis.port}")
	private int redisPort;



	

	public String getRedisServer() {
		return redisServer;
	}

	public void setRedisServer(String redisServer) {
		this.redisServer = redisServer;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	

	public String getBaseUsersURL() {
		return baseUsersURL;
	}

	public void setBaseUsersURL(String baseUsersURL) {
		this.baseUsersURL = baseUsersURL;
	}	
	*/
	
}
