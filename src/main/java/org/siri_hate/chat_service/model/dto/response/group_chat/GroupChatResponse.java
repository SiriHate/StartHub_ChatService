package org.siri_hate.chat_service.model.dto.response.group_chat;

import java.util.Set;

public class GroupChatResponse {
    private Long id;
    private String name;
    private Set<String> participantUsernames;

    public GroupChatResponse() {}

    public GroupChatResponse(Long id, String name, Set<String> participantUsernames) {
        this.id = id;
        this.name = name;
        this.participantUsernames = participantUsernames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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