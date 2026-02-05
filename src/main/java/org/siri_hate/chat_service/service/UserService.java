package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.PrivateChat;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;
import org.siri_hate.chat_service.model.mapper.UserMapper;
import org.siri_hate.chat_service.repository.GroupChatRepository;
import org.siri_hate.chat_service.repository.PrivateChatRepository;
import org.siri_hate.chat_service.repository.UserRepository;
import org.siri_hate.main_service.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupChatRepository groupChatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(
            UserRepository userRepository,
            GroupChatRepository groupChatRepository,
            PrivateChatRepository privateChatRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.groupChatRepository = groupChatRepository;
        this.privateChatRepository = privateChatRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public User getOrCreateUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User user = new User();
                    user.setUsername(username);
                    return userRepository.save(user);
                });
    }

    @Transactional
    public UserResponseDTO createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new EntityExistsException("User already exists!");
        }
        var user = new User(username);
        userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException();
        }
        userRepository.deleteById(userId);
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return userMapper.toUserResponseDTO(user);
    }

    public List<Chat> getChatsByUsername(String username) {

        List<PrivateChat> privateChatsAsUser1 = privateChatRepository.findByUser1Username(username);
        List<Chat> allChats = new ArrayList<>(privateChatsAsUser1);

        List<PrivateChat> privateChatsAsUser2 = privateChatRepository.findByUser2Username(username);
        allChats.addAll(privateChatsAsUser2);

        List<GroupChat> groupChats = groupChatRepository.findByGroupParticipantsUserUsername(username);
        allChats.addAll(groupChats);

        return allChats;
    }
} 