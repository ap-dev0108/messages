package com.example.messages.websocket;

import com.example.messages.exception.IllegalException;
import com.example.messages.security.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;

    public JwtChannelInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
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

        return message;
    }
}
