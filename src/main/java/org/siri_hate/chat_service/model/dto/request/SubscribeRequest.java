package org.siri_hate.chat_service.model.dto.request;

import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {

    @NotNull(message = "Chat ID cannot be null")
    private Long chatId;

    public SubscribeRequest() {}

    public SubscribeRequest(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
} 