package com.dub.gutenberg;



import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import com.dub.gutenberg.config.ElasticsearchConfig;


@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private ElasticsearchConfig elasticsearchConfig;
	
	
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
