package com.example.messages.controller;

import com.example.messages.dto.Response;
import com.example.messages.dto.message.CreateMessageDTO;
import com.example.messages.dto.message.MessageResponseDTO;
import com.example.messages.services.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<Response<List<MessageResponseDTO>>> getMessages(
            @PathVariable UUID conversationId,
            Authentication authentication
    ) {

        List<MessageResponseDTO> messages =
                messageService.getMessages(
                        conversationId,
                        authentication
                );

        Response<List<MessageResponseDTO>> response =
                new Response<>(
                        true,
                        "Messages retrieved successfully",
                        messages,
                        Instant.now()
                );

        return ResponseEntity.ok(response);
    }
}