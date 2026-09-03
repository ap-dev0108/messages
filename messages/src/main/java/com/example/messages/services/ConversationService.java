package com.example.messages.services;

import com.example.messages.entity.Conversation;
import com.example.messages.entity.ConversationParticipant;
import com.example.messages.entity.ConversationType;
import com.example.messages.entity.User;
import com.example.messages.repository.ConversationParticipantRepository;
import com.example.messages.repository.ConversationRepository;
import com.example.messages.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final UserRepository userRepository;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository conversationParticipantRepository,
            UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Conversation createConversation(ConversationType type, List<UUID> participantIds) {
        if(participantIds == null || participantIds.isEmpty()) {
            throw new IllegalArgumentException("Atleast one participant needed");
        }

        if (type == null) {
            throw new IllegalArgumentException("Conversation type is required");
        }

        if (type == ConversationType.DIRECT && participantIds.size() != 2) {
            throw new IllegalArgumentException(
                    "Direct conversations must have exactly 2 participants"
            );
        }

        if (type == ConversationType.GROUP && participantIds.size() < 2) {
            throw new IllegalArgumentException(
                    "Group conversations must have at least 2 participants"
            );
        }

        if (participantIds.size() != participantIds.stream().distinct().count()) {
            throw new IllegalArgumentException(
                    "A participant cannot be added more than once"
            );
        }

        if (type == ConversationType.DIRECT) {
            Optional<Conversation> existingConversation =
                    conversationParticipantRepository
                            .findDirectConversationBetweenUsers(participantIds);

            if (existingConversation.isPresent()) {
                throw new IllegalStateException(
                        "A direct conversation between these users already exists"
                );
            }
        }

        List<User> users = userRepository.findAllById(participantIds);

        if (users.size() != participantIds.size()) {
            throw new IllegalArgumentException("One or more users do not exists");
        }

        Conversation conversation = new Conversation();
        conversation.setType(type);

        Conversation savedConversation = conversationRepository.save(conversation);

        for (User user: users) {
            ConversationParticipant participant = new ConversationParticipant();

            participant.setConversation(savedConversation);
            participant.setUser(user);

            conversationParticipantRepository.save(participant);
        }

        return savedConversation;
    }
}
