package org.siri_hate.chat_service.service.impl;

import org.siri_hate.chat_service.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, UserServiceImpl userServiceImpl) {
        this.chatRepository = chatRepository;
    }

    @Transactional
    public void deleteChat(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new RuntimeException("Chat with id " + chatId + " not found");
        }
        chatRepository.deleteById(chatId);
    }

    public void getChatById(Long chatId) {
        chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat with id " + chatId + " not found"));
    }
}