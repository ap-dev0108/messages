package com.example.messages.controller;

import com.example.messages.dto.message.CreateMessageDTO;
import com.example.messages.dto.message.MessageResponseDTO;
import com.example.messages.services.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> createMessage(
            @Valid @RequestBody CreateMessageDTO request,
            Authentication authentication
    ) {

        MessageResponseDTO response =
                messageService.createMessage(request, authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}