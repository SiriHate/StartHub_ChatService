package org.siri_hate.chat_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    @NotNull(message = "Chat ID cannot be null")
    private Long chatId;

    public MessageRequest() {}

    public MessageRequest(String content, Long chatId) {
        this.content = content;
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
} 