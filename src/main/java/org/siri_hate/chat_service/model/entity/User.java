package org.siri_hate.chat_service.model.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "username_idx",  columnList="username", unique = true)
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    public User() {}

    public User(String username){
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}