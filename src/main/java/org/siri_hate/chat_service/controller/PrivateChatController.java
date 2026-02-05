package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.service.PrivateChatService;
import org.siri_hate.main_service.api.PrivateChatApi;
import org.siri_hate.main_service.dto.PrivateChatRequestDTO;
import org.siri_hate.main_service.dto.PrivateChatResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrivateChatController implements PrivateChatApi {

    private final PrivateChatService privateChatService;

    public PrivateChatController(PrivateChatService privateChatService) {
        this.privateChatService = privateChatService;
    }

    @Override
    public ResponseEntity<PrivateChatResponseDTO> createPrivateChat(PrivateChatRequestDTO privateChatRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firstUsername = authentication.getName();
        var response = privateChatService.createPrivateChat(privateChatRequestDTO, firstUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePrivateChat(Long id) {
        privateChatService.deletePrivateChat(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PrivateChatResponseDTO> getPrivateChat(Long id) {
        var response = privateChatService.getPrivateChatById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> togglePrivateChatVisibility(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        privateChatService.togglePrivateChatVisibility(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
} 