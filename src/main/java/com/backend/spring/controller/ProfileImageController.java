package com.backend.spring.controller;

import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ProfileImageDto;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.service.ProfileImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile-image")
public class ProfileImageController {

    @Autowired
    private ProfileImageService profileImageService;

    @PutMapping("/{userId}")
    public ResponseEntity<MessageResponse> updateProfileImage(@PathVariable Integer userId, @ModelAttribute ProfileImageDto profileImageDto) {
        try {
            System.out.println("Cập nhật ảnh");
            User updateProfileImage = profileImageService.updateProfileImage(userId, profileImageDto);
            if (updateProfileImage != null) {
                return ResponseEntity.ok(new MessageResponse("Cập nhật ảnh đại diện thành công!"));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi cập nhật ảnh đại diện: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
