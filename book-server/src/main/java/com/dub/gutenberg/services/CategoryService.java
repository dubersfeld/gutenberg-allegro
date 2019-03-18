package com.dub.gutenberg.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.dub.gutenberg.domain.Category;


public interface CategoryService {

	public List<Category> getAllCategories() throws IOException;	
	public Category getCategory(String categorySlug) throws IOException;
}
