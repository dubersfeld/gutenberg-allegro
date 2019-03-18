package com.dub.gutenberg;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.dub.gutenberg.config.ElasticsearchConfig;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	@Autowired
	private ElasticsearchConfig elasticsearchConfig;
		
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestOperations restTemplate() {
		return new RestTemplate();
	}
	
		
	@Bean
	@DependsOn("elasticsearchConfig")
	public RestHighLevelClient client() {
		RestHighLevelClient client = new RestHighLevelClient(
	                RestClient.builder(
	                        new HttpHost(
	                        		elasticsearchConfig.getHost(),
	                        		elasticsearchConfig.getPortOne(),
	                        		elasticsearchConfig.getScheme()),
	                        new HttpHost(elasticsearchConfig.getHost(),
	                        		elasticsearchConfig.getPortTwo(),
	                        		elasticsearchConfig.getScheme())
	                )
		);
		
		return client;
	} 
}
