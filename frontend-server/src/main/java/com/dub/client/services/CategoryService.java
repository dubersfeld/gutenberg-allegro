package com.dub.client.services;

import java.util.List;

import com.dub.client.domain.Category;



public interface CategoryService {

	public List<Category> getLeaveCategories();
	
	public Category getCategory(String categorySlug);
}
