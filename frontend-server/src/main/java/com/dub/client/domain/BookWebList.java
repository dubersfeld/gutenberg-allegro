package com.dub.client.domain;

import java.util.ArrayList;
import java.util.List;

public class BookWebList {
	
	List<String> bookIds;

	public BookWebList() {
		bookIds = new ArrayList<>();
	}

	public BookWebList(List<String> source) {
		bookIds = new ArrayList<>();
		bookIds.addAll(source);
	}

	public List<String> getBookIds() {
		return bookIds;
	}

	public void setBookIds(List<String> bookIds) {
		this.bookIds = bookIds;
	}

}
