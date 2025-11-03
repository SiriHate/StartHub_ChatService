package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.siri_hate.chat_service.model.dto.response.private_chat.PrivateChatResponse;
import org.siri_hate.chat_service.model.entity.PrivateChat;

@Mapper(componentModel = "spring")
public interface PrivateChatMapper {

    @Mapping(target = "participantUsernames", expression = "java(java.util.Set.of(privateChat.getUser1().getUsername(), privateChat.getUser2().getUsername()))")
    PrivateChatResponse toPrivateChatResponse(PrivateChat privateChat);
}