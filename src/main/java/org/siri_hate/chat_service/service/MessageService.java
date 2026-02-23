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

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;
    private final ChatService chatService;
    private final FileService fileService;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            UserService userService,
            ChatService chatService,
            FileService fileService
    ) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userService = userService;
        this.chatService = chatService;
        this.fileService = fileService;
    }

    public MessageResponseDTO createMessage(String senderUsername, MessageRequestDTO request) {
        Message message = messageMapper.toMessage(request);
        message.setSender(userService.getOrCreateUser(senderUsername));
        message.setChat(chatService.getChatEntity(request.getChatId()));
        messageRepository.save(message);
        return enrichWithImageUrl(messageMapper.toMessageResponse(message), message.getImageKey());
    }

    public MessagePageResponseDTO getMessages(SubscribeRequestDTO request) {
        Page<Message> messages = messageRepository.findByChatId(
                request.getChatId(),
                PageRequest.of(request.getPage(), request.getSize())
        );
        MessagePageResponseDTO response = messageMapper.toMessagePageResponse(messages);
        List<Message> messageList = messages.getContent();
        List<MessageResponseDTO> dtoList = response.getContent();
        if (dtoList != null) {
            for (int i = 0; i < dtoList.size(); i++) {
                enrichWithImageUrl(dtoList.get(i), messageList.get(i).getImageKey());
            }
        }
        return response;
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    private MessageResponseDTO enrichWithImageUrl(MessageResponseDTO dto, String imageKey) {
        if (imageKey != null && !imageKey.isBlank()) {
            dto.setImageUrl(fileService.getPresignedUrl(imageKey));
        }
        return dto;
    }
}
