package com.dub.spring.services;

import java.io.IOException;

public interface OrderService {
	
	void setShipped(String orderId) throws IOException;
}
