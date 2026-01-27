package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.model.dto.request.private_chat.PrivateChatRequest;
import org.siri_hate.chat_service.model.dto.response.private_chat.PrivateChatResponse;
import org.siri_hate.chat_service.service.impl.PrivateChatServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat_service/private_chats")
public class PrivateChatController {

    private final PrivateChatServiceImpl privateChatServiceImpl;

    public PrivateChatController(PrivateChatServiceImpl privateChatServiceImpl) {
        this.privateChatServiceImpl = privateChatServiceImpl;
    }

    @PostMapping
    public ResponseEntity<PrivateChatResponse> createPrivateChat(@RequestBody PrivateChatRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firstUsername = authentication.getName();

        PrivateChatResponse response = privateChatServiceImpl.createPrivateChat(request, firstUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivateChatResponse> getPrivateChatById(@PathVariable Long id) {
        PrivateChatResponse response = privateChatServiceImpl.getPrivateChatById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivateChat(@PathVariable Long id) {
        privateChatServiceImpl.deletePrivateChat(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle_visibility")
    public ResponseEntity<Void> togglePrivateChatVisibility(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        privateChatServiceImpl.togglePrivateChatVisibility(id, username);
        return ResponseEntity.noContent().build();
    }
} 