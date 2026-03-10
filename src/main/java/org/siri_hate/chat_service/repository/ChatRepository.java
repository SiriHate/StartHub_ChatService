package org.siri_hate.chat_service.repository;

import org.siri_hate.chat_service.model.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {
    @Query("""
            select c
            from Chat c
            join c.members cm
            join cm.user u
            left join c.messages m
            where u.username = :username
            group by c
            order by coalesce(max(m.sendAt), :minDate) desc, c.id desc
            """)
    Page<Chat> findMyChatsOrderedByLastMessage(
            @Param("username") String username,
            @Param("minDate") LocalDateTime minDate,
            Pageable pageable
    );
}