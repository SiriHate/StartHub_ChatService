package org.siri_hate.chat_service.controller;

import org.siri_hate.chat_service.service.FileService;
import org.siri_hate.main_service.api.ChatImageApi;
import org.siri_hate.main_service.dto.ImageUploadResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController implements ChatImageApi {

    private final FileService fileService;

    @Autowired
    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public ResponseEntity<ImageUploadResponseDTO> uploadChatImage(Long chatId, String xUserName, MultipartFile file) {
        String imageKey = fileService.uploadChatImage(file);
        ImageUploadResponseDTO response = new ImageUploadResponseDTO();
        response.setImageKey(imageKey);
        return ResponseEntity.ok(response);
    }
}
