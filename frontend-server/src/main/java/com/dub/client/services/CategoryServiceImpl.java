package com.dub.client.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.dub.client.controller.config.ServiceConfig;
import com.dub.client.domain.Category;



@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private RestOperations restTemplate;
	
	@Autowired
	private ServiceConfig serviceConfig;
	
	private String baseBooksUrl;
	
	@PostConstruct
	public void init() {
		baseBooksUrl = serviceConfig.getBaseBooksURL();
	}
	
	@Override
	public List<Category> getLeaveCategories() {
			
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> amt = new ArrayList<>();
		amt.add(MediaType.APPLICATION_JSON);
		headers.setAccept(amt);
			
		HttpEntity<List<Category>> request = new HttpEntity<>(null, headers);
	
		String categoriesURI = baseBooksUrl + "/allCategories";
		
		ResponseEntity<List<Category>> response 
		= restTemplate.exchange(
			categoriesURI, HttpMethod.GET, request, new ParameterizedTypeReference<List<Category>>(){});
	
		List<Category> cats = response.getBody();
		List<Category> leaves = new ArrayList<>();
		
		for (Category cat : cats) {
			if (cat.getChildren().isEmpty()) {
				leaves.add(cat);
			}
		}
			
		return leaves;
	}

	@Override
	public Category getCategory(String categorySlug) {
			
		String categoriesURI = baseBooksUrl + "/category/" + categorySlug;
			
		ResponseEntity<Category> response 
		= restTemplate.getForEntity(categoriesURI, Category.class);
		
		return response.getBody();
	}
}
