package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.model.dto.request.MessageRequest;
import org.siri_hate.chat_service.model.dto.request.SubscribeRequest;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.chat_service.service.ChatService;
import org.siri_hate.chat_service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.List;

@Controller
@Validated
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ChatService chatService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate,
                               MessageService messageService,
                               ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{chatId}/send")
    public void sendMessage(@DestinationVariable Long chatId, @Payload @Validated MessageRequest request, Principal principal) {
        String sender = principal.getName();
        Message message = messageService.saveMessage(request, sender);
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
    }

    @MessageMapping("/chat/subscribe")
    public void subscribeToChat(@Payload @Validated SubscribeRequest request) {
        chatService.getChatById(request.getChatId());
        List<Message> messages = messageService.getMessagesByChatId(request.getChatId());
        messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId() + "/history", messages);
    }
} 