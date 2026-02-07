package org.siri_hate.chat_service.service;

import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.chat_service.model.mapper.MessageMapper;
import org.siri_hate.chat_service.repository.MessageRepository;
import org.siri_hate.main_service.dto.MessagePageResponseDTO;
import org.siri_hate.main_service.dto.MessageRequestDTO;
import org.siri_hate.main_service.dto.MessageResponseDTO;
import org.siri_hate.main_service.dto.SubscribeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;
    private final ChatService chatService;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            UserService userService,
            ChatService chatService
    )
    {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userService = userService;
        this.chatService = chatService;
    }

    public MessageResponseDTO createMessage(String senderUsername, MessageRequestDTO request) {
        Message message = messageMapper.toMessage(request);
        message.setSender(userService.getOrCreateUser(senderUsername));
        message.setChat(chatService.getChatEntity(request.getChatId()));
        messageRepository.save(message);
        return messageMapper.toMessageResponse(message);
    }

    public MessagePageResponseDTO getMessages(SubscribeRequestDTO request) {
        Page<Message> messages = messageRepository.findByChatId(
                request.getChatId(),
                PageRequest.of(request.getPage(), request.getSize())
        );
        return messageMapper.toMessagePageResponse(messages);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
} 