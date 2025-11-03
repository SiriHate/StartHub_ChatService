package org.siri_hate.chat_service.repository;

import org.siri_hate.chat_service.model.entity.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Long> {
    boolean existsByUser1UsernameAndUser2Username(String user1Username, String user2Username);

    List<PrivateChat> findByUser1Username(String username);

    List<PrivateChat> findByUser2Username(String username);
} 