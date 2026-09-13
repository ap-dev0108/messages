package com.example.messages.dto.conversation;

import com.example.messages.dto.message.MessageResponseDTO;
import com.example.messages.entity.ConversationType;

import java.time.Instant;
import java.util.UUID;

public class ConversationResponseDTO {
    private UUID conversationId;
    private ConversationType type;
    private Instant createdAt;
    private MessageResponseDTO lastMessage;

    public ConversationResponseDTO(UUID id, ConversationType type, Instant createdAt, MessageResponseDTO message) {
        this.conversationId = id;
        this.type = type;
        this.createdAt = createdAt;
        this.lastMessage = message;
    }

    public UUID getConversationId() {
        return this.conversationId;
    }
    public void setConversationId(UUID id) {
        this.conversationId = id;
    }

    public ConversationType getType() {
        return this.type;
    }
    public void setType(ConversationType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public MessageResponseDTO getLastMessage() {
        return this.lastMessage;
    }
    public void setLastMessage(MessageResponseDTO lastMessage) {
        this.lastMessage = lastMessage;
    }
}
