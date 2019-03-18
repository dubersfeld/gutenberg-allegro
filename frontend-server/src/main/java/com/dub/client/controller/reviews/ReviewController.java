package com.dub.client.controller.reviews;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dub.client.controller.utils.UserUtils;
import com.dub.client.domain.Book;
import com.dub.client.domain.MyUser;
import com.dub.client.domain.Review;
import com.dub.client.services.BookService;
import com.dub.client.services.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserUtils userUtils;
	
	
	@RequestMapping(value ="/createReview/{bookSlug}", 
								method = RequestMethod.GET)
	public ModelAndView getReviewForm(@PathVariable String bookSlug, 
								
								HttpServletRequest request) {
		
		ModelMap params = new ModelMap();
		Book book = bookService.getBookBySlug(bookSlug);
		HttpSession session = request.getSession();
		params.addAttribute("book", book);
		Review review = new Review();
		MyUser user = userUtils.getLoggedUser(session);
		review.setUserId(user.getId());
		review.setBookId(book.getId());
		params.addAttribute("review", review);
		
		return new ModelAndView("reviews/createReview", params);
	}
	
	@RequestMapping(
			value = "/createReview", 
			method = RequestMethod.POST)
	public String postReview(@ModelAttribute("review") Review review, ModelMap params) {
			
		// add review to database
		reviewService.createReview(review);
		
		return "reviews/createReviewSuccess";
	}
			
	@RequestMapping(value = "/voteHelpful", method = RequestMethod.POST)
	public String voteHelpful(@ModelAttribute("voteForm") VoteForm voteForm,
			HttpServletRequest request) {
			
		String reviewId = voteForm.getReviewId();
	
		HttpSession session = request.getSession();
		String userId = userUtils.getLoggedUser(session).getId();

		reviewService.voteHelpful(reviewId, userId, 
								voteForm.isHelpful());
		
		return "reviews/voteSuccess";
	}
	
	public static class VoteForm {
		private String reviewId;
		private boolean helpful = false;
		
		public VoteForm() {
		}

		public String getReviewId() {
			return reviewId;
		}

		public void setReviewId(String reviewId) {
			this.reviewId = reviewId;
		}

		public boolean isHelpful() {
			return helpful;
		}

		public void setHelpful(boolean helpful) {
			this.helpful = helpful;
		}
	}
}
