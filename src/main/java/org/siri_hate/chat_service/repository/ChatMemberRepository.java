package org.siri_hate.chat_service.repository;

import org.siri_hate.chat_service.model.entity.ChatMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    Page<ChatMember> findByChatId(Long chatId, Pageable pageable);
}