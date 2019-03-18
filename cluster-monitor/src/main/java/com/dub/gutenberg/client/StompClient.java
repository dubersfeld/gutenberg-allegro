package com.dub.gutenberg.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;


public class StompClient {

	@Value("${websocketUrl}")
	private String websocketUrl;
	
	private MyHandler myHandler;
  
    private StompSession stompSession = null;
    
        
    public StompClient(MyHandler myHandler) {
    	this.myHandler = myHandler;    	
    }
    
    public StompSession getStompSession() {
		return stompSession;
	}

	public void setStompSession(StompSession stompSession) {
		this.stompSession = stompSession;
	}

	public void disconnect() {
		this.stompSession = null;
	}
    
    public ListenableFuture<StompSession> connect() {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
      
        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
         
        return stompClient.connect(websocketUrl, myHandler);
    }
     
}