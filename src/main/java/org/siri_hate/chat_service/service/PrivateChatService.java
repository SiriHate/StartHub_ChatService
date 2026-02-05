package org.siri_hate.chat_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.chat_service.model.entity.PrivateChat;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.mapper.PrivateChatMapper;
import org.siri_hate.chat_service.repository.PrivateChatRepository;
import org.siri_hate.main_service.dto.PrivateChatRequestDTO;
import org.siri_hate.main_service.dto.PrivateChatResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrivateChatService {

    private final PrivateChatRepository privateChatRepository;
    private final UserService userService;
    private final PrivateChatMapper privateChatMapper;

    public PrivateChatService(
            PrivateChatRepository privateChatRepository,
            UserService userService,
            PrivateChatMapper privateChatMapper
    )
    {
        this.privateChatRepository = privateChatRepository;
        this.userService = userService;
        this.privateChatMapper = privateChatMapper;
    }

    @Transactional
    public PrivateChatResponseDTO createPrivateChat(PrivateChatRequestDTO request, String firstUsername) {
        String secondUsername = request.getSecondUsername();

        if (privateChatRepository.existsByUser1UsernameAndUser2Username(firstUsername, secondUsername) ||
                privateChatRepository.existsByUser1UsernameAndUser2Username(secondUsername, firstUsername)) {
            throw new EntityExistsException("This private chat already exists!");
        }

        User firstUser = userService.getOrCreateUser(firstUsername);
        User secondUser = userService.getOrCreateUser(secondUsername);

        PrivateChat privateChat = new PrivateChat();
        privateChat.setUser1(firstUser);
        privateChat.setUser2(secondUser);

        PrivateChat savedChat = privateChatRepository.save(privateChat);
        return privateChatMapper.toPrivateChatResponse(savedChat);
    }

    public PrivateChatResponseDTO getPrivateChatById(Long id) {
        PrivateChat privateChat = privateChatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return privateChatMapper.toPrivateChatResponse(privateChat);
    }

    @Transactional
    public void deletePrivateChat(Long id) {
        if (!privateChatRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        privateChatRepository.deleteById(id);
    }

    public PrivateChat getPrivateChatEntityById(Long id) {
        return privateChatRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void togglePrivateChatVisibility(Long id, String username) {
        PrivateChat privateChat = privateChatRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (privateChat.getUser1().getUsername().equals(username)) {
            privateChat.setHiddenByUser1(!privateChat.getHiddenByUser1());
        } else if (privateChat.getUser2().getUsername().equals(username)) {
            privateChat.setHiddenByUser2(!privateChat.getHiddenByUser2());
        } else {
            throw new RuntimeException("User is not chat participant!");
        }

        privateChatRepository.save(privateChat);
    }
} 