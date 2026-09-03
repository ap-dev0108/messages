package com.example.messages.repository;

import com.example.messages.entity.Conversation;
import com.example.messages.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, UUID> {
    @Query("""
    SELECT cp.conversation
    FROM ConversationParticipant cp
    WHERE cp.conversation.type = com.example.messages.entity.ConversationType.DIRECT
      AND cp.user.id IN :userIds
    GROUP BY cp.conversation
    HAVING COUNT(DISTINCT cp.user.id) = 2
""")
    Optional<Conversation> findDirectConversationBetweenUsers(
            @Param("userIds") List<UUID> userIds
    );
    List<ConversationParticipant> findByUserId(UUID userId);
}