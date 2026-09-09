package com.example.messages.services;

import com.example.messages.dto.message.CreateMessageDTO;
import com.example.messages.dto.message.MessageResponseDTO;
import com.example.messages.entity.Conversation;
import com.example.messages.entity.Message;
import com.example.messages.entity.User;
import com.example.messages.exception.ForbiddenException;
import com.example.messages.exception.ResourceNotFoundException;
import com.example.messages.mapper.MessageMapper;
import com.example.messages.repository.ConversationParticipantRepository;
import com.example.messages.repository.ConversationRepository;
import com.example.messages.repository.MessageRepository;
import com.example.messages.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public MessageService(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            ConversationParticipantRepository conversationParticipantRepository,
            UserRepository userRepository,
            MessageMapper messageMapper
    ) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.userRepository = userRepository;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public MessageResponseDTO createMessage(
            CreateMessageDTO request,
            Authentication authentication
    ) {

        UUID userId = (UUID) authentication.getPrincipal();

        Conversation conversation = conversationRepository
                .findById(request.getConversationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conversation not found")
                );

        boolean isParticipant =
                conversationParticipantRepository
                        .existsByConversationIdAndUserId(
                                conversation.getId(),
                                userId
                        );

        if (!isParticipant) {
            throw new IllegalStateException(
                    "You are not a participant in this conversation"
            );
        }

        User sender = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toResponseDTO(savedMessage);
    }

    public List<MessageResponseDTO> getMessages(
            UUID conversationId,
            Authentication authentication
    ) {
        UUID userId = (UUID) authentication.getPrincipal();

        boolean isParticipant =
                conversationParticipantRepository
                        .existsByConversationIdAndUserId(
                                conversationId,
                                userId
                        );

        if (!isParticipant) {
            throw new ForbiddenException(
                    "You are not a participant in this conversation"
            );
        }

        List<Message> messages =
                messageRepository
                        .findByConversationIdOrderByCreatedAtAsc(
                                conversationId
                        );

        return messages.stream()
                .map(messageMapper::toResponseDTO)
                .toList();
    }
}