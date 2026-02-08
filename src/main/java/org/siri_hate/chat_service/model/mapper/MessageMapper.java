package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.main_service.dto.MessagePageResponseDTO;
import org.siri_hate.main_service.dto.MessageRequestDTO;
import org.siri_hate.main_service.dto.MessageResponseDTO;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MessageMapper {
    Message toMessage(MessageRequestDTO request);

    @Mapping(target = "sender", source = "sender.username")
    @Mapping(target = "sendAt",
            source = "sendAt",
            dateFormat = "dd/MM/yyyy HH:mm")
    MessageResponseDTO toMessageResponse(Message message);

    MessagePageResponseDTO toMessagePageResponse(Page<Message> messages);
}
