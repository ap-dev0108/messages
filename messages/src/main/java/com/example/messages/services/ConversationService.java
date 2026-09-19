package com.example.messages.services;

import com.example.messages.dto.conversation.ConversationResponseDTO;
import com.example.messages.entity.Conversation;
import com.example.messages.entity.ConversationParticipant;
import com.example.messages.entity.ConversationType;
import com.example.messages.entity.User;
import com.example.messages.exception.IllegalException;
import com.example.messages.repository.ConversationParticipantRepository;
import com.example.messages.repository.ConversationRepository;
import com.example.messages.repository.MessageRepository;
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
    private final MessageRepository messageRepository;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository conversationParticipantRepository,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Conversation createConversation(ConversationType type, List<UUID> participantIds) {
        if(participantIds == null || participantIds.isEmpty()) {
            throw new IllegalException("Atleast one participant needed");
        }

        if (type == null) {
            throw new IllegalException("Conversation type is required");
        }

        if (type == ConversationType.DIRECT && participantIds.size() != 2) {
            throw new IllegalException(
                    "Direct conversations must have exactly 2 participants"
            );
        }

        if (type == ConversationType.GROUP && participantIds.size() < 2) {
            throw new IllegalException(
                    "Group conversations must have at least 2 participants"
            );
        }

        if (participantIds.size() != participantIds.stream().distinct().count()) {
            throw new IllegalException(
                    "A participant cannot be added more than once"
            );
        }

        if (type == ConversationType.DIRECT) {
            Optional<Conversation> existingConversation =
                    conversationParticipantRepository
                            .findDirectConversationBetweenUsers(participantIds);

            if (existingConversation.isPresent()) {
                throw new IllegalException(
                        "A direct conversation between these users already exists"
                );
            }
        }

        List<User> users = userRepository.findAllById(participantIds);

        if (users.size() != participantIds.size()) {
            throw new IllegalException("One or more users do not exists");
        }

        Conversation conversation = new Conversation();
        conversation.setType(type);

        Conversation savedConversation = conversationRepository.save(conversation);

        for (User user: users) {
            ConversationParticipant participant = new ConversationParticipant();

            boolean isParticipant = conversationParticipantRepository.existsByConversationIdAndUserId(conversation.getId(), participant.getId());

            if (!isParticipant) {
                throw new IllegalException("You do not belong in this conversation");
            }

            participant.setConversation(savedConversation);
            participant.setUser(user);

            conversationParticipantRepository.save(participant);
        }

        return savedConversation;
    }

    public List<ConversationResponseDTO> getConversation() {
        List<Conversation> conversations = conversationRepository.findAll();

        return conversations.stream().map(conversation -> {
            ConversationResponseDTO response = new ConversationResponseDTO();

            response.setConversationId(conversation.getId());
            response.setType(conversation.getType());
            response.setCreatedAt(conversation.getCreatedAt());

            return response;
        });
    }
}
