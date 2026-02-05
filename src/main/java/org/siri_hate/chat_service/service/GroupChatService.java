package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChatParticipant;
import org.siri_hate.chat_service.model.enums.GroupChatRole;
import org.siri_hate.chat_service.model.mapper.GroupChatMapper;
import org.siri_hate.chat_service.repository.GroupChatRepository;
import org.siri_hate.main_service.dto.GroupChatRequestDTO;
import org.siri_hate.main_service.dto.GroupChatResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final UserService userService;
    private final GroupChatMapper groupChatMapper;

    public GroupChatService(
            GroupChatRepository groupChatRepository,
            UserService userService,
            GroupChatMapper groupChatMapper
    ) {
        this.groupChatRepository = groupChatRepository;
        this.userService = userService;
        this.groupChatMapper = groupChatMapper;
    }

    @Transactional
    public GroupChatResponseDTO createGroupChat(GroupChatRequestDTO request, String creatorUsername) {

        if (groupChatRepository.existsByName(request.getName())) {
            throw new EntityExistsException("Group chat already exists!");
        }

        GroupChat groupChat = groupChatMapper.toGroupChat(request);
        Set<GroupChatParticipant> participants = request.getParticipantUsernames().stream()
                .map(username -> {
                    User user = userService.getOrCreateUser(username);
                    GroupChatRole role = username.equals(creatorUsername)
                            ? GroupChatRole.OWNER
                            : GroupChatRole.MEMBER;
                    return new GroupChatParticipant(groupChat, user, role);
                })
                .collect(Collectors.toSet());

        groupChat.setGroupParticipants(participants);
        GroupChat savedChat = groupChatRepository.save(groupChat);
        return groupChatMapper.toGroupChatResponseDTO(savedChat);
    }

    public GroupChatResponseDTO getGroupChatById(Long id) {
        GroupChat groupChat = groupChatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return groupChatMapper.toGroupChatResponseDTO(groupChat);
    }

    @Transactional
    public GroupChatResponseDTO updateGroupChatName(Long id, String newName) {
        GroupChat groupChat = groupChatRepository.findById(id).orElseThrow(EntityExistsException::new);
        if (groupChatRepository.existsByName(newName) && !groupChat.getName().equals(newName)) {
            throw new EntityExistsException("Group chat already exists!");
        }
        groupChat.setName(newName);
        GroupChat updatedChat = groupChatRepository.save(groupChat);
        return groupChatMapper.toGroupChatResponseDTO(updatedChat);
    }

    @Transactional
    public void deleteGroupChat(Long id) {
        GroupChat groupChat = groupChatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        groupChatRepository.delete(groupChat);
    }

    public GroupChat getGroupChatEntityById(Long id) {
        return groupChatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}