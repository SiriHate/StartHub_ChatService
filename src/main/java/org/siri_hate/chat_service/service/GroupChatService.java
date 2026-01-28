package org.siri_hate.chat_service.service;

import org.siri_hate.chat_service.model.dto.request.group_chat.GroupChatRequest;
import org.siri_hate.chat_service.model.dto.response.group_chat.GroupChatResponse;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChatParticipant;
import org.siri_hate.chat_service.model.enums.GroupChatRole;
import org.siri_hate.chat_service.model.mapper.GroupChatMapper;
import org.siri_hate.chat_service.repository.GroupChatRepository;
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
    public GroupChatResponse createGroupChat(GroupChatRequest request, String creatorUsername) {
        if (groupChatRepository.existsByName(request.getName())) {
            throw new RuntimeException("Group chat with name " + request.getName() + " already exists");
        }

        GroupChat groupChat = groupChatMapper.toGroupChat(request);

        // Создаем участников с соответствующими ролями
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
        return groupChatMapper.toGroupChatResponse(savedChat);
    }

    public GroupChatResponse getGroupChatById(Long id) {
        GroupChat groupChat = groupChatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group chat not found with id: " + id));
        return groupChatMapper.toGroupChatResponse(groupChat);
    }

    @Transactional
    public GroupChatResponse updateGroupChatName(Long id, String newName) {
        GroupChat groupChat = groupChatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group chat not found with id: " + id));

        if (groupChatRepository.existsByName(newName) && !groupChat.getName().equals(newName)) {
            throw new RuntimeException("Group chat with name " + newName + " already exists");
        }

        groupChat.setName(newName);
        GroupChat updatedChat = groupChatRepository.save(groupChat);
        return groupChatMapper.toGroupChatResponse(updatedChat);
    }

    @Transactional
    public void deleteGroupChat(Long id) {
        if (!groupChatRepository.existsById(id)) {
            throw new RuntimeException("Group chat not found with id: " + id);
        }
        groupChatRepository.deleteById(id);
    }

    public GroupChat getGroupChatEntityById(Long id) {
        return groupChatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group chat not found with id: " + id));
    }
}