package com.dub.client.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dub.client.domain.Category;
import com.dub.client.domain.MyUser;
import com.dub.client.services.BookService;
import com.dub.client.services.CategoryService;
import com.dub.client.services.UserService;
import com.dub.client.domain.Book;
import com.dub.client.domain.BookSearch;
import com.dub.client.controller.utils.UserUtils;



@Controller
public class DefaultController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserUtils userUtils;
	
    @GetMapping({"/", "/backHome", "/index"})
    public String greetings(Model model, HttpServletRequest request) {
    	    
    	List<Category> categories = categoryService.getLeaveCategories();
    	 
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			

		String username = auth.getName();
		MyUser user = userService.findByUsername(username);
		String userId = user.getId();
		
		
		List<Book> booksToReview = new ArrayList<>();
		
		try {
			booksToReview = bookService.getBooksNotReviewed(userId, 5);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		model.addAttribute("categories", categories); 	
		model.addAttribute("booksToReview", booksToReview);	
		model.addAttribute("username", username);
		model.addAttribute("bookSearch", new BookSearch());
		
		// attach user to current session
		HttpSession session = request.getSession();
				
		userUtils.setLoggedUser(session, user);
				
		// add a Set<String> to store invalid book Ids
		session.setAttribute("invalidBooks", new HashSet<String>());
				
	
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
    
        return "login";
    }
    
    
}