package org.siri_hate.chat_service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        if (accessor.getUser() != null) return message;

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) return message;

        String username = (String) sessionAttributes.get("username");
        if (username == null || username.isBlank()) {
            log.warn("WebSocket: missing username in session attributes");
            return message;
        }

        String rolesStr = (String) sessionAttributes.get("roles");
        List<SimpleGrantedAuthority> authorities = List.of();
        if (rolesStr != null && !rolesStr.isBlank()) {
            authorities = Arrays.stream(rolesStr.split(","))
                    .map(String::trim)
                    .filter(r -> !r.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }

        var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        accessor.setUser(authentication);

        return message;
    }
}
