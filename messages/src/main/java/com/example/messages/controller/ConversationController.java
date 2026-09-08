package com.example.messages.controller;

import com.example.messages.dto.Response;
import com.example.messages.dto.conversation.CreateConversationDTO;
import com.example.messages.entity.Conversation;
import com.example.messages.services.ConversationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/talk")
    @SecurityRequirement(name = "bearerAuth")
    public Conversation createConversation(@Valid @RequestBody CreateConversationDTO request) {
        return conversationService.createConversation(request.getType(), request.getParticipantIds());
    }

    @GetMapping("/getAllConvo")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<Conversation>> getConversation() {
        var conversationList = conversationService.getConversation();
        return ResponseEntity.status(HttpStatus.OK).body(conversationList);
    }
}
