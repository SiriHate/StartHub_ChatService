package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.service.UserService;
import org.siri_hate.main_service.api.UserApi;
import org.siri_hate.main_service.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponseDTO> createUser(String username) {
        var response = userService.createUser(username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserByUsername(String username) {
        var response = userService.getUserByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 