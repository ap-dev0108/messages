package com.example.messages.controller;

import com.example.messages.dto.message.MessageResponseDTO;
import com.example.messages.dto.message.SendMessageDTO;
import com.example.messages.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatWebSocketController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    public void handleMessage(SendMessageDTO message, Principal principal) {
        MessageResponseDTO response = messageService.createMessageFromWebSocket(message, principal);

        simpMessagingTemplate.convertAndSend("/topic/conversations/" + message.getConversationId(), response);
    }
}
