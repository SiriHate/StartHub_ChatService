package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.ChatMember;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.enums.ChatRole;
import org.siri_hate.chat_service.model.enums.ChatType;
import org.siri_hate.chat_service.model.mapper.ChatMapper;
import org.siri_hate.chat_service.model.mapper.ChatMemberMapper;
import org.siri_hate.chat_service.repository.ChatMemberRepository;
import org.siri_hate.chat_service.repository.ChatRepository;
import org.siri_hate.chat_service.repository.MessageRepository;
import org.siri_hate.chat_service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatMemberMapper chatMemberMapper;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    @Autowired
    public ChatService(
            ChatRepository chatRepository,
            ChatMapper chatMapper,
            UserService userService,
            ChatMemberMapper chatMemberMapper,
            ChatMemberRepository chatMemberRepository,
            MessageRepository messageRepository
    ) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
        this.userService = userService;
        this.chatMemberMapper = chatMemberMapper;
        this.chatMemberRepository = chatMemberRepository;
        this.messageRepository = messageRepository;
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
        Page<Chat> chats = chatRepository.findMyChatsOrderedByLastMessage(
                username,
                LocalDateTime.of(1970, 1, 1, 0, 0),
                PageRequest.of(page, size)
        );
        for (Chat chat : chats.getContent()) {
            if (chat.getType().equals(ChatType.PRIVATE)) {
                String chatName = chat.getMembers().stream().filter(chatMember -> !chatMember.getUser().getUsername().equals(username)).findFirst().orElseThrow((EntityNotFoundException::new)).getUser().getUsername();
                chat.setName(chatName);
            }
            var lastMessage = messageRepository.findTopByChatIdOrderBySendAtDescIdDesc(chat.getId());
            if (lastMessage != null) {
                if (lastMessage.getContent() != null && !lastMessage.getContent().isBlank()) {
                    chat.setLastMessage(lastMessage.getContent());
                } else if (lastMessage.getImageKey() != null && !lastMessage.getImageKey().isBlank()) {
                    chat.setLastMessage("Изображение");
                } else {
                    chat.setLastMessage("");
                }
                chat.setLastMessageAt(lastMessage.getSendAt());
            } else {
                chat.setLastMessage("");
                chat.setLastMessageAt(null);
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

    public List<Long> getMutedChats(String username) {
        return chatMemberRepository.findMutedChatIdsByUsername(username);
    }

    public void muteChat(Long chatId, String username) {
        ChatMember member = chatMemberRepository
                .findByChatIdAndUserUsername(chatId, username)
                .orElseThrow(EntityNotFoundException::new);
        member.setMutedNotifications(true);
        chatMemberRepository.save(member);
    }

    public void unmuteChat(Long chatId, String username) {
        ChatMember member = chatMemberRepository
                .findByChatIdAndUserUsername(chatId, username)
                .orElseThrow(EntityNotFoundException::new);
        member.setMutedNotifications(false);
        chatMemberRepository.save(member);
    }
}