package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.*;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.ChatMember;
import org.siri_hate.chat_service.model.enums.ChatRole;
import org.siri_hate.main_service.dto.ChatMemberPageResponseDTO;
import org.siri_hate.main_service.dto.ChatMemberRequestDTO;
import org.siri_hate.main_service.dto.ChatMemberResponseDTO;
import org.siri_hate.main_service.dto.MemberChatRoleDTO;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMemberMapper {

    @Mapping(target = "username", source = "user.username")
    ChatMemberResponseDTO toMemberResponseDTO(ChatMember chatMember);

    ChatMemberPageResponseDTO toMemberPageResponse(Page<ChatMember> members);

    ChatRole toChatRole(MemberChatRoleDTO memberChatRoleDTO);
}