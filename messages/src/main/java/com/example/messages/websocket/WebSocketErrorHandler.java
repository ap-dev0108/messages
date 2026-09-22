package com.example.messages.websocket;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(
            Message<byte[]> clientMessage, Throwable ex
    ) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "Websocket request failed";

        return super.handleClientMessageProcessingError(clientMessage, new RuntimeException(errorMessage));
    }
}
