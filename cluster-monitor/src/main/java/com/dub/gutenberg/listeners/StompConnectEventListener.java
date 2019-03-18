package com.dub.gutenberg.listeners;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.dub.gutenberg.client.StompClient;


public class StompConnectEventListener implements ApplicationListener<SessionConnectEvent> {
		
	@Autowired
	private StompClient stompClient;
			
	private boolean enable = true;
	
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
					
		if (stompClient.getStompSession() == null) {
			
			try {
				stompClient.setStompSession(stompClient.connect().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}				
		}
	}

	// useless, to be removed
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}	
}
