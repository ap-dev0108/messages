package com.example.messages.controller;

import com.example.messages.dto.message.SendMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    @MessageMapping("/chat")
    public void handleMessage(SendMessageDTO message) {
        System.out.println("Conversation: " + message.getConversationId());
        System.out.println("Messages: " + message.getContent());
    }
}
