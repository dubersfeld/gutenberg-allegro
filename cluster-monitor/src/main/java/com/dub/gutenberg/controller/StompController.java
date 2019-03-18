package com.dub.gutenberg.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class StompController {

    @MessageMapping("/notify")
    @SendTo("/topic/cluster")
    public String notify(String state) throws Exception {
     
        return state;
        
    }
    
}