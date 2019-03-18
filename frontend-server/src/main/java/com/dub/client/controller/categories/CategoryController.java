package com.dub.client.controller.categories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dub.client.domain.Book;
import com.dub.client.services.BookService;
import com.dub.client.services.CategoryService;


@Controller
public class CategoryController {
		
	@Autowired 
	private BookService bookService;
	
	@Autowired 
	private CategoryService categoryService;
	
	@RequestMapping("/categories/{cat}")
	public ModelAndView booksByCategory(@PathVariable String cat) {
		
		Map<String, Object> params = new HashMap<>();
			
		/** sort by title */
		List<Book> books = bookService.allBooksByCategory(cat, "title");
		
		params.put("books", books);
		params.put("category", categoryService.getCategory(cat).getName());
		
		return new ModelAndView("books/allBooksByCategory", params);
	}
}
