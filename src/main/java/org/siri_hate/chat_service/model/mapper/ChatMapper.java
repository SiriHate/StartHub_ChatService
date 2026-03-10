package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(
        componentModel = "spring",
        uses = {
                ChatMemberMapper.class,
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMapper {

    @Mapping(target = "members", ignore = true)
    Chat toChat(ChatRequestDTO request);

    ChatSummaryResponseDTO toChatSummaryResponse(Chat chat);

    ChatFullResponseDTO toChatFullResponse(Chat chat);

    ChatPageResponseDTO toChatPageResponse(Page<Chat> chats);

    default OffsetDateTime map(LocalDateTime value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }
}