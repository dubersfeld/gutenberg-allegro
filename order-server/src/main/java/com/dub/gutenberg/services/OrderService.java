package com.dub.gutenberg.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.dub.gutenberg.domain.EditCart;
import com.dub.gutenberg.domain.Order;
import com.dub.gutenberg.domain.OrderState;
import com.dub.gutenberg.domain.UserAndReviewedBooks;


public interface OrderService {
	
	List<String> getBooksNotReviewed(UserAndReviewedBooks userAndReviewedBooks) 
											throws ParseException, IOException;
	
	Order saveOrder(Order order, boolean creation) throws IOException;	
	Order getOrderById(String orderId) throws IOException;
	Order getActiveOrder(String userId) throws IOException;
	Order addBookToOrder(String orderId, String bookId) throws IOException;
	Order editCart(EditCart editCart) throws IOException;
	Order setOrderState(String orderId, OrderState state) throws IOException;
	Order checkoutOrder(String orderId) throws IOException;
			
}
