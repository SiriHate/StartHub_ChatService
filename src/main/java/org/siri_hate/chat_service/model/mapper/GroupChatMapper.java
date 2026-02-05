package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;
import org.siri_hate.main_service.dto.GroupChatRequestDTO;
import org.siri_hate.main_service.dto.GroupChatResponseDTO;

@Mapper(componentModel = "spring")
public interface GroupChatMapper {

    @Mapping(target = "groupParticipants", ignore = true)
    GroupChat toGroupChat(GroupChatRequestDTO request);

    @Mapping(target = "participantUsernames", expression = "java(groupChat.getGroupParticipants().stream().map(participant -> participant.getUser().getUsername()).collect(java.util.stream.Collectors.toSet()))")
    GroupChatResponseDTO toGroupChatResponseDTO(GroupChat groupChat);
} 