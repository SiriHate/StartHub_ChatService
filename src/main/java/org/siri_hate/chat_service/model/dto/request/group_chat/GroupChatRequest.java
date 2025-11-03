package org.siri_hate.chat_service.model.dto.request.group_chat;

import java.util.Set;

public class GroupChatRequest {
    private String name;
    private Set<String> participantUsernames;

    public GroupChatRequest() {}

    public GroupChatRequest(String name, Set<String> participantUsernames) {
        this.name = name;
        this.participantUsernames = participantUsernames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getParticipantUsernames() {
        return participantUsernames;
    }

    public void setParticipantUsernames(Set<String> participantUsernames) {
        this.participantUsernames = participantUsernames;
    }
} 