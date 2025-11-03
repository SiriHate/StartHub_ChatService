package org.siri_hate.chat_service.service;

import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.PrivateChat;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.entity.group_chat.GroupChat;
import org.siri_hate.chat_service.repository.GroupChatRepository;
import org.siri_hate.chat_service.repository.PrivateChatRepository;
import org.siri_hate.chat_service.repository.UserRepository;
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

    @Autowired
    public UserService(
            UserRepository userRepository,
            GroupChatRepository groupChatRepository,
            PrivateChatRepository privateChatRepository
    ) {
        this.userRepository = userRepository;
        this.groupChatRepository = groupChatRepository;
        this.privateChatRepository = privateChatRepository;
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
    public User createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User with username " + username + " already exists");
        }

        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found"));
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