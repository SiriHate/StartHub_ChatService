package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.siri_hate.chat_service.model.dto.request.group_chat.GroupChatRequest;
import org.siri_hate.chat_service.model.dto.response.group_chat.GroupChatResponse;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;

@Mapper(componentModel = "spring")
public interface GroupChatMapper {

    @Mapping(target = "groupParticipants", ignore = true)
    GroupChat toGroupChat(GroupChatRequest request);

    @Mapping(target = "participantUsernames", expression = "java(groupChat.getGroupParticipants().stream().map(participant -> participant.getUser().getUsername()).collect(java.util.stream.Collectors.toSet()))")
    GroupChatResponse toGroupChatResponse(GroupChat groupChat);
} 