package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat_service/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestParam String username) {
        User user = userService.createUser(username);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{username}/chats")
    public ResponseEntity<List<Chat>> getUserChatsByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.getChatsByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/me/chats")
    public ResponseEntity<List<Chat>> getUserChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new ResponseEntity<>(userService.getChatsByUsername(username), HttpStatus.OK);
    }
} 