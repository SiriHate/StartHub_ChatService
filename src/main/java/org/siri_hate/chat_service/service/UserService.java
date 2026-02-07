package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.mapper.UserMapper;
import org.siri_hate.chat_service.repository.UserRepository;
import org.siri_hate.main_service.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    )
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public User getOrCreateUser(String username) {
        return userRepository.findByUsername(username).orElseGet(() -> userRepository.save(new User(username)));
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
        userRepository.deleteById(userId);
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return userMapper.toUserResponseDTO(user);
    }
} 