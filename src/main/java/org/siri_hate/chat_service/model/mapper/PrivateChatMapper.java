package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.siri_hate.chat_service.model.entity.PrivateChat;
import org.siri_hate.main_service.dto.PrivateChatResponseDTO;

@Mapper(componentModel = "spring")
public interface PrivateChatMapper {

    @Mapping(target = "participantUsernames", expression = "java(java.util.Set.of(privateChat.getUser1().getUsername(), privateChat.getUser2().getUsername()))")
    PrivateChatResponseDTO toPrivateChatResponse(PrivateChat privateChat);
}