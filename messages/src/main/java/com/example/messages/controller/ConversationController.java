package com.example.messages.controller;

import com.example.messages.dto.conversation.CreateConversationDTO;
import com.example.messages.entity.Conversation;
import com.example.messages.services.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Conversation createConversation(@Valid @RequestBody CreateConversationDTO request) {
        return conversationService.createConversation(request.getType(), request.getParticipantIds());
    }
}
