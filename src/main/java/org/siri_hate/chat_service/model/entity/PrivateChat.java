package org.siri_hate.chat_service.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("PRIVATE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PrivateChat extends Chat {

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(name = "hidden_by_user1", nullable = false)
    private Boolean hiddenByUser1 = false;

    @Column(name = "hidden_by_user2", nullable = false)
    private Boolean hiddenByUser2 = false;

    public PrivateChat() {
    }

    public PrivateChat(Long id, List<Message> messages, User user1, User user2) {
        super(id, messages);
        this.user1 = user1;
        this.user2 = user2;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Boolean getHiddenByUser1() {
        return hiddenByUser1;
    }

    public void setHiddenByUser1(Boolean hiddenByUser1) {
        this.hiddenByUser1 = hiddenByUser1;
    }

    public Boolean getHiddenByUser2() {
        return hiddenByUser2;
    }

    public void setHiddenByUser2(Boolean hiddenByUser2) {
        this.hiddenByUser2 = hiddenByUser2;
    }
}
