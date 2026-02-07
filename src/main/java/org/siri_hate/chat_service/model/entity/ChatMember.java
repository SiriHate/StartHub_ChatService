package org.siri_hate.chat_service.model.entity;

import jakarta.persistence.*;
import org.siri_hate.chat_service.model.enums.ChatRole;

@Entity
@Table(
        name = "chat_members",
        indexes = {
                @Index(name = "chat_member_user_idx", columnList = "user_id"),
                @Index(name = "chat_member_chat_idx", columnList = "chat_id"),
                @Index(name = "chat_member_user_chat_idx", columnList = "user_id, chat_id")
        }
)
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatRole role;

    public ChatMember() {}

    public ChatMember(Chat chat, User user, ChatRole role) {
        this.chat = chat;
        this.user = user;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatRole getRole() {
        return role;
    }

    public void setRole(ChatRole role) {
        this.role = role;
    }
}
