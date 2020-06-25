package com.dub.gutenberg.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dub.gutenberg.domain.Category;
import com.dub.gutenberg.exceptions.CategoryNotFoundException;
import com.dub.gutenberg.services.CategoryService;


@RestController
public class CategoryRestEndpoint {

	@Autowired 
	private CategoryService categoryService;
	
	@RequestMapping("/allCategories")
	public ResponseEntity<List<Category>> allCategories() {
			
		try {
			List<Category> categories = categoryService.getAllCategories();	
			return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Category>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
		
	@RequestMapping("/category/{slug}")
	public ResponseEntity<Category> getCategoryBySlug(@PathVariable("slug") String categorySlug) {
		
		try {
			Category cat
				= categoryService.getCategory(categorySlug);
			return new ResponseEntity<Category>(cat, HttpStatus.OK);
		} catch (CategoryNotFoundException e) {
			return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			return new ResponseEntity<Category>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}