package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.model.dto.request.group_chat.GroupChatRequest;
import org.siri_hate.chat_service.model.dto.response.group_chat.GroupChatResponse;
import org.siri_hate.chat_service.service.GroupChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat_service/group_chats")
public class GroupChatController {

    private final GroupChatService groupChatService;

    public GroupChatController(GroupChatService groupChatService) {
        this.groupChatService = groupChatService;
    }

    @PostMapping
    public ResponseEntity<GroupChatResponse> createGroupChat(@RequestBody GroupChatRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication.getName();

        GroupChatResponse response = groupChatService.createGroupChat(request, creatorUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupChatResponse> getGroupChatById(@PathVariable Long id) {
        GroupChatResponse response = groupChatService.getGroupChatById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<GroupChatResponse> updateGroupChatName(
            @PathVariable Long id,
            @RequestParam String newName
    ) {
        GroupChatResponse response = groupChatService.updateGroupChatName(id, newName);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupChat(@PathVariable Long id) {
        groupChatService.deleteGroupChat(id);
        return ResponseEntity.noContent().build();
    }
} 