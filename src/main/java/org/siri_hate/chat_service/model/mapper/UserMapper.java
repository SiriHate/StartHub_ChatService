package org.siri_hate.chat_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.main_service.dto.UserResponseDTO;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);
}