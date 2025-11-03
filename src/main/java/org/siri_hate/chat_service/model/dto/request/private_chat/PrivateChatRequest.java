package org.siri_hate.chat_service.model.dto.request.private_chat;

public class PrivateChatRequest {

    private String secondUsername;

    public PrivateChatRequest() {}

    public PrivateChatRequest(String secondUsername) {
        this.secondUsername = secondUsername;
    }

    public String getSecondUsername() {
        return secondUsername;
    }

    public void setSecondUsername(String secondUsername) {
        this.secondUsername = secondUsername;
    }
} 