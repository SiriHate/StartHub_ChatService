package org.siri_hate.chat_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.siri_hate.chat_service.kafka.ChatMessageNotificationMessage;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.chat_service.model.mapper.MessageMapper;
import org.siri_hate.chat_service.repository.ChatMemberRepository;
import org.siri_hate.chat_service.repository.MessageRepository;
import org.siri_hate.chat_service.dto.MessagePageResponseDTO;
import org.siri_hate.chat_service.dto.MessageRequestDTO;
import org.siri_hate.chat_service.dto.MessageResponseDTO;
import org.siri_hate.chat_service.dto.SubscribeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;
    private final ChatService chatService;
    private final FileService fileService;
    private final ChatMemberRepository chatMemberRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${notification.topic.chat-message:chat_message_notification_topic}")
    private String chatMessageNotificationTopic;

    @Value("${notification.service.url:http://localhost:8082/api/v1/notification_service/internal/chat-message}")
    private String notificationServiceUrl;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            UserService userService,
            ChatService chatService,
            FileService fileService,
            ChatMemberRepository chatMemberRepository,
            ObjectProvider<KafkaTemplate<String, String>> kafkaTemplateProvider,
            ObjectProvider<ObjectMapper> objectMapperProvider
    ) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userService = userService;
        this.chatService = chatService;
        this.fileService = fileService;
        this.chatMemberRepository = chatMemberRepository;
        this.kafkaTemplate = kafkaTemplateProvider.getIfAvailable();
        this.objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
        this.httpClient = HttpClient.newHttpClient();
    }

    public MessageResponseDTO createMessage(String senderUsername, MessageRequestDTO request) {
        Message message = messageMapper.toMessage(request);
        String normalizedContent = normalize(request.getContent());
        String normalizedImageKey = normalize(request.getImageKey());
        boolean hasText = !normalizedContent.isEmpty();
        boolean hasImage = !normalizedImageKey.isEmpty();

        if (!hasText && !hasImage) {
            throw new IllegalArgumentException("Message must contain text or image");
        }

        message.setContent(normalizedContent);
        message.setImageKey(hasImage ? normalizedImageKey : null);
        message.setSender(userService.getOrCreateUser(senderUsername));
        message.setChat(chatService.getChatEntity(request.getChatId()));
        messageRepository.save(message);
        MessageResponseDTO response = enrichWithImageUrl(messageMapper.toMessageResponse(message), message.getImageKey());
        publishMessageNotification(message, senderUsername);
        return response;
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

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private void publishMessageNotification(Message message, String senderUsername) {
        try {
            List<String> recipients = chatMemberRepository.findRecipientUsernames(message.getChat().getId(), senderUsername);
            if (recipients.isEmpty()) {
                return;
            }
            ChatMessageNotificationMessage payload = new ChatMessageNotificationMessage(
                    message.getChat().getId(),
                    senderUsername,
                    message.getContent(),
                    message.getSendAt() != null ? message.getSendAt().toString() : null,
                    recipients
            );
            String serializedPayload = objectMapper.writeValueAsString(payload);
            if (kafkaTemplate != null) {
                kafkaTemplate.send(chatMessageNotificationTopic, serializedPayload);
            }
            sendNotificationHttpFallback(serializedPayload);
        } catch (Exception ignored) {
        }
    }

    private void sendNotificationHttpFallback(String payload) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(notificationServiceUrl))
                .header("Content-Type", "application/json")
                .header("X-Gateway", "true")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding()).exceptionally(ex -> null);
    }
}
