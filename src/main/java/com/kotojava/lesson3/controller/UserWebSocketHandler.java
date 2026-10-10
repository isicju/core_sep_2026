package com.kotojava.lesson3.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kotojava.lesson3.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static com.kotojava.lesson3.controller.Utils.logCall;


@Slf4j
@Component
@AllArgsConstructor
public class UserWebSocketHandler extends TextWebSocketHandler {

    private static final int MAX_ROWS = 500;

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WS /ws/users connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String requestId = null;
        try {
            JsonNode req = objectMapper.readTree(message.getPayload());
            requestId = req.path("requestId").asText(null);
            String email3Chars = req.hasNonNull("email3Chars") ? req.get("email3Chars").asText() : null;
            long delay = req.path("delay").asLong(0);

            log.info("WS /users: email3Chars={}, delay={}", email3Chars, delay);

            if (delay > 0) {
                Thread.sleep(delay);
            }

            Object data = email3Chars != null
                    ? logCall("getUsersBy3EmailChars(" + email3Chars + ")",
                    () -> userService.getUsersBy3EmailChars(email3Chars))
                    : logCall("getAllUsers()",
                    () -> userService.getAllUsers().stream().limit(MAX_ROWS).toList());

            ObjectNode resp = objectMapper.createObjectNode();
            resp.put("requestId", requestId);
            resp.set("data", objectMapper.valueToTree(data));
            send(session, resp);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("WS request failed", e);
            ObjectNode resp = objectMapper.createObjectNode();
            resp.put("requestId", requestId);
            resp.put("error", String.valueOf(e.getMessage()));
            send(session, resp);
        }
    }

    // WebSocketSession.sendMessage is not thread-safe
    private void send(WebSocketSession session, ObjectNode payload) throws IOException {
        synchronized (session) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("WS /ws/users closed: {} ({})", session.getId(), status);
    }
}