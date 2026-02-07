package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.service.MessageService;
import org.siri_hate.main_service.api.MessageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public ResponseEntity<Void> deleteChatMessage(Long id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}