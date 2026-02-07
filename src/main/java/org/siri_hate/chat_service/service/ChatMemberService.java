package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.ChatMember;
import org.siri_hate.chat_service.model.mapper.ChatMemberMapper;
import org.siri_hate.chat_service.repository.ChatMemberRepository;
import org.siri_hate.main_service.dto.ChangeMemberChatRoleDTO;
import org.siri_hate.main_service.dto.ChatMemberPageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final ChatMemberMapper chatMemberMapper;

    @Autowired
    public ChatMemberService(ChatMemberRepository chatMemberRepository, ChatMemberMapper chatMemberMapper) {
        this.chatMemberRepository = chatMemberRepository;
        this.chatMemberMapper = chatMemberMapper;
    }

    public void changeChatMemberRole(Long id, ChangeMemberChatRoleDTO request) {
        ChatMember chatMember = chatMemberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        chatMember.setRole(chatMemberMapper.toChatRole(request.getRole()));
        chatMemberRepository.save(chatMember);
    }

    public void deleteChatMemberById(Long id) {
        chatMemberRepository.deleteById(id);
    }

    public ChatMemberPageResponseDTO getChatMembers(Long chatId, Integer page, Integer size) {
        Page<ChatMember> members = chatMemberRepository.findByChatId(chatId, PageRequest.of(page, size));
        return chatMemberMapper.toMemberPageResponse(members);
    }
}
