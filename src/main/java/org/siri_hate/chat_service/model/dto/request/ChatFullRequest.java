package org.siri_hate.chat_service.model.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ChatFullRequest {

    private String name;

    @NotNull(message = "isGroup field cannot be null")
    private Boolean isGroup;

    @NotNull(message = "Participants list cannot be null")
    private List<String> participantUsernames;

    public ChatFullRequest() {
    }

    public ChatFullRequest(String name, Boolean isGroup, List<String> participantUsernames) {
        this.name = name;
        this.isGroup = isGroup;
        this.participantUsernames = participantUsernames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public List<String> getParticipantUsernames() {
        return participantUsernames;
    }

    public void setParticipantUsernames(List<String> participantUsernames) {
        this.participantUsernames = participantUsernames;
    }
}
