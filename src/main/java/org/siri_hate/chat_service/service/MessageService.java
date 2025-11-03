package org.siri_hate.chat_service.service;

import org.siri_hate.chat_service.model.dto.request.MessageRequest;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.chat_service.repository.ChatRepository;
import org.siri_hate.chat_service.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    @Transactional
    public Message saveMessage(MessageRequest request, String sender) {

        if (!chatRepository.existsById(request.getChatId())) {
            throw new RuntimeException("Chat with id " + request.getChatId() + " not found");
        }

        Message message = new Message();
        message.setContent(request.getContent());
        message.setChatId(request.getChatId());
        message.setSender(sender);

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
} 