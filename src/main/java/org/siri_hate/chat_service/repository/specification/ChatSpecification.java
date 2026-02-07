package org.siri_hate.chat_service.repository.specification;

import jakarta.persistence.criteria.Join;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.ChatMember;
import org.siri_hate.chat_service.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class ChatSpecification {

    public static Specification<Chat> hasMemberWithUsername(String username) {
        return (root, query, cb) -> {

            query.distinct(true);

            Join<Chat, ChatMember> members = root.join("members");
            Join<ChatMember, User> user = members.join("user");

            return cb.equal(user.get("username"), username);
        };
    }
}
