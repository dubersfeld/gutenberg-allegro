package com.dub.client.controller.utils;

import java.util.List;

import com.dub.client.controller.books.DisplayOthers;
import com.dub.client.controller.display.DisplayItem;
import com.dub.client.controller.display.DisplayItemPrice;
import com.dub.client.controller.reviews.ReviewWithAuthor;
import com.dub.client.domain.Order;
import com.dub.client.domain.Review;

public interface DisplayUtils {

	public List<ReviewWithAuthor> getReviewWithAuthors(List<Review> reviews, String userId);
	public List<DisplayOthers> getOtherBooksBoughtWith(Order order);

	List<DisplayItem> getDisplayItems(String orderId);
	List<DisplayItemPrice> getDisplayItemPrices(String orderId);
}
