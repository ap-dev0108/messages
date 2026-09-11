package com.example.messages.websocket;

import com.example.messages.exception.ForbiddenException;
import com.example.messages.exception.IllegalException;
import com.example.messages.repository.ConversationParticipantRepository;
import com.example.messages.security.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final ConversationParticipantRepository conversationParticipantRepository;

    public JwtChannelInterceptor(JwtService jwtService, ConversationParticipantRepository conversationParticipantRepository) {
        this.jwtService = jwtService;
        this.conversationParticipantRepository = conversationParticipantRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())) {

            String auth = stompHeaderAccessor.getFirstNativeHeader("Authorization");

            if (auth == null || !auth.startsWith("Bearer")) {
                throw new IllegalException("Missing Authorization Header");
            }

            String token = auth.substring(7);

            UUID userId = jwtService.extractUserId(token);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of()
            );

            stompHeaderAccessor.setUser(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        if (StompCommand.SUBSCRIBE.equals(stompHeaderAccessor.getCommand())) {

            Principal principal = stompHeaderAccessor.getUser();

            if (principal == null) {
                throw new ForbiddenException(
                        "User is not authenticated"
                );
            }

            UUID userId = UUID.fromString(principal.getName());

            String destination = stompHeaderAccessor.getDestination();

            if (destination == null ||
                    !destination.startsWith("/topic/conversations/")) {
                throw new IllegalArgumentException(
                        "Invalid conversation subscription"
                );
            }

            String conversationIdString =
                    destination.substring("/topic/conversations/".length());

            UUID conversationId;

            try {
                conversationId = UUID.fromString(conversationIdString);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Invalid conversation ID"
                );
            }

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
        }

        return message;
    }
}
