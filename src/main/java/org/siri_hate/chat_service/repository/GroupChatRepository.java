package org.siri_hate.chat_service.repository;

import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    boolean existsByName(String name);

    List<GroupChat> findByGroupParticipantsUserUsername(String username);
} 