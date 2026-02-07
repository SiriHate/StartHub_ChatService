package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.service.ChatMemberService;
import org.siri_hate.main_service.api.ChatMemberApi;
import org.siri_hate.main_service.dto.ChangeMemberChatRoleDTO;
import org.siri_hate.main_service.dto.ChatMemberPageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatMemberController implements ChatMemberApi {

    private final ChatMemberService chatMemberService;

    @Autowired
    public ChatMemberController(ChatMemberService chatMemberService) {
        this.chatMemberService = chatMemberService;
    }

    @Override
    public ResponseEntity<Void> changeChatMemberRole(Long id, ChangeMemberChatRoleDTO changeMemberChatRoleDTO) {
        chatMemberService.changeChatMemberRole(id, changeMemberChatRoleDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteChatMemberById(Long id) {
        chatMemberService.deleteChatMemberById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ChatMemberPageResponseDTO> getChatMembers(Long id, Integer page, Integer size) {
        var response =  chatMemberService.getChatMembers(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
