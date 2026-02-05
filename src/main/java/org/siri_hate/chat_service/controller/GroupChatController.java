package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.service.GroupChatService;
import org.siri_hate.main_service.api.GroupChatApi;
import org.siri_hate.main_service.dto.GroupChatRequestDTO;
import org.siri_hate.main_service.dto.GroupChatResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupChatController implements GroupChatApi {

    private final GroupChatService groupChatService;

    public GroupChatController(GroupChatService groupChatService) {
        this.groupChatService = groupChatService;
    }

    @Override
    public ResponseEntity<GroupChatResponseDTO> createGroupChat(GroupChatRequestDTO groupChatRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication.getName();
        var response = groupChatService.createGroupChat(groupChatRequestDTO, creatorUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteGroupChat(Long id) {
        groupChatService.deleteGroupChat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GroupChatResponseDTO> getGroupChatById(Long id) {
        var response = groupChatService.getGroupChatById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupChatResponseDTO> updateGroupChatName(Long id, String newName) {
        var response = groupChatService.updateGroupChatName(id, newName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}