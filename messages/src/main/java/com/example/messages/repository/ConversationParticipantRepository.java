package com.example.messages.repository;

import com.example.messages.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, UUID> {
}