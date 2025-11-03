package org.siri_hate.chat_service.model.dto.response.private_chat;

import java.util.Set;

public class PrivateChatResponse {
    private Long id;
    private Set<String> participantUsernames;

    public PrivateChatResponse() {}

    public PrivateChatResponse(Long id, Set<String> participantUsernames) {
        this.id = id;
        this.participantUsernames = participantUsernames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getParticipantUsernames() {
        return participantUsernames;
    }

    public void setParticipantUsernames(Set<String> participantUsernames) {
        this.participantUsernames = participantUsernames;
    }
} 