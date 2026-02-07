package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.ChatMember;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.enums.ChatRole;
import org.siri_hate.chat_service.model.enums.ChatType;
import org.siri_hate.chat_service.model.mapper.ChatMapper;
import org.siri_hate.chat_service.model.mapper.ChatMemberMapper;
import org.siri_hate.chat_service.repository.ChatRepository;
import org.siri_hate.chat_service.repository.specification.ChatSpecification;
import org.siri_hate.main_service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatMemberMapper chatMemberMapper;
    private final UserService userService;

    @Autowired
    public ChatService(ChatRepository chatRepository, ChatMapper chatMapper, UserService userService, ChatMemberMapper chatMemberMapper) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
        this.userService = userService;
        this.chatMemberMapper = chatMemberMapper;
    }

    public ChatFullResponseDTO createChat(String creatorUsername, ChatRequestDTO request) {
        Chat chat = chatMapper.toChat(request);
        for (ChatMemberRequestDTO memberDTO : request.getMembers()) {
            User user = userService.getOrCreateUser(memberDTO.getUsername());
            var chatMember = new ChatMember(chat, user, chatMemberMapper.toChatRole(memberDTO.getRole()));
            chat.getMembers().add(chatMember);
        }
        User owner = userService.getOrCreateUser(creatorUsername);
        chat.getMembers().add(new ChatMember(chat, owner, ChatRole.OWNER));
        chatRepository.save(chat);
        return chatMapper.toChatFullResponse(chat);
    }

    public ChatPageResponseDTO getMyChats(String username, int page, int size) {
        Specification<Chat> specification = Specification.allOf(ChatSpecification.hasMemberWithUsername(username));
        Page<Chat> chats = chatRepository.findAll(specification, PageRequest.of(page, size));
        for (Chat chat : chats.getContent()) {
            if (chat.getType().equals(ChatType.PRIVATE)) {
                String chatName = chat.getMembers().stream().filter(chatMember -> !chatMember.getUser().getUsername().equals(username)).findFirst().orElseThrow((EntityNotFoundException::new)).getUser().getUsername();
                chat.setName(chatName);
            }
        }
        return chatMapper.toChatPageResponse(chats);
    }

    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    public Chat getChatEntity(Long id) {
        return chatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public ChatSummaryResponseDTO getChat(Long id) {
        return chatMapper.toChatSummaryResponse(getChatEntity(id));
    }
}