package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.main_service.dto.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);
}
