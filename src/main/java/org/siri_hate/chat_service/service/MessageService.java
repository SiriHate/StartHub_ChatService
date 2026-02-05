package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.chat_service.model.mapper.MessageMapper;
import org.siri_hate.chat_service.repository.ChatRepository;
import org.siri_hate.chat_service.repository.MessageRepository;
import org.siri_hate.main_service.dto.MessageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public Message saveMessage(MessageRequestDTO request, String sender) {
        Message message = messageMapper.toMessage(request, sender);
        message.setSender(sender);
        if (!chatRepository.existsById(request.getChatId())) {
            throw new EntityNotFoundException();
        }
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
} 