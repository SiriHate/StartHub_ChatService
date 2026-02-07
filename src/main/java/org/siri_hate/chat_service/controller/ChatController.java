package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.service.ChatService;
import org.siri_hate.main_service.api.ChatApi;
import org.siri_hate.main_service.dto.ChatFullResponseDTO;
import org.siri_hate.main_service.dto.ChatPageResponseDTO;
import org.siri_hate.main_service.dto.ChatRequestDTO;
import org.siri_hate.main_service.dto.ChatSummaryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController implements ChatApi {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public ResponseEntity<ChatFullResponseDTO> createChat(ChatRequestDTO chatRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerUsername = authentication.getName();
        var response = chatService.createChat(ownerUsername, chatRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteChat(Long id) {
        chatService.deleteChat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ChatSummaryResponseDTO> getChatById(Long id) {
        var response = chatService.getChat(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ChatPageResponseDTO> getMyChats(Integer page, Integer size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        var response = chatService.getMyChats(username, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
