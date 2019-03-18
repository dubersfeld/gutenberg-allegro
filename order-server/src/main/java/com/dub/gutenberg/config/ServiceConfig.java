package com.dub.gutenberg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

	@Value("${baseBooksUrl}")
	private String baseBooksURL;

	public String getBaseBooksURL() {
		return baseBooksURL;
	}

	public void setBaseBooksURL(String baseBooksURL) {
		this.baseBooksURL = baseBooksURL;
	}
	
	
	
}
