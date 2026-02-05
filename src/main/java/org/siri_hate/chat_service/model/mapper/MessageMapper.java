package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.main_service.dto.MessageRequestDTO;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message toMessage(MessageRequestDTO request, String sender);
}
