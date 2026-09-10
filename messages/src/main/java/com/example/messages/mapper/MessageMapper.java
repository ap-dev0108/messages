package com.example.messages.mapper;

import com.example.messages.dto.message.MessageResponseDTO;
import com.example.messages.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public MessageResponseDTO toResponseDTO(Message message) {

        MessageResponseDTO response = new MessageResponseDTO();

        response.setId(message.getId());
        response.setConversationId(message.getConversation().getId());
        response.setSenderId(message.getSender().getId());
        response.setContent(message.getContent());
        response.setCreatedAt(message.getCreatedAt());

        return response;
    }
}
