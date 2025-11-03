package org.siri_hate.chat_service.model.entity.group_chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.enums.GroupChatRole;

@Entity
@Table(name = "group_chat_participants")
public class GroupChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonIgnore
    private GroupChat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupChatRole role;

    public GroupChatParticipant() {}

    public GroupChatParticipant(GroupChat chat, User user, GroupChatRole role) {
        this.chat = chat;
        this.user = user;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupChat getChat() {
        return chat;
    }

    public void setChat(GroupChat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupChatRole getRole() {
        return role;
    }

    public void setRole(GroupChatRole role) {
        this.role = role;
    }
}
