package org.siri_hate.chat_service.repository;

import org.siri_hate.chat_service.model.entity.ChatMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    Page<ChatMember> findByChatId(Long chatId, Pageable pageable);

    Optional<ChatMember> findByChatIdAndUserUsername(Long chatId, String username);

    @Query("select cm.chat.id from ChatMember cm where cm.user.username = :username and cm.mutedNotifications = true")
    List<Long> findMutedChatIdsByUsername(@Param("username") String username);

    @Query("select cm.user.username from ChatMember cm where cm.chat.id = :chatId and cm.user.username <> :senderUsername and cm.mutedNotifications = false")
    List<String> findRecipientUsernames(@Param("chatId") Long chatId, @Param("senderUsername") String senderUsername);
}