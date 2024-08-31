package com.backend.spring.service;
import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ProfileImageDto;
import com.backend.spring.repository.ProfileImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Optional;

@Service
public class ProfileImageService {

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Transactional
    public User updateProfileImage(Integer userId, ProfileImageDto profileImageDto) throws IOException {
        Optional<User> profileImageOptional = profileImageRepository.findById(userId);
        if (profileImageOptional.isPresent()) {
            User profileImage = profileImageOptional.get();

            MultipartFile image = profileImageDto.getImage();
            if (image != null && !image.isEmpty()) {
                String imageName = image.getOriginalFilename();
                String imagePath = "images/";
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Xóa ảnh cũ
                String oldImage = profileImage.getImage();
                if (oldImage != null && !oldImage.isEmpty()) {
                    Path oldImageFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldImage);
                    Files.deleteIfExists(oldImageFile);
                }
                // Lưu ảnh mới
                Path file = uploadPath.resolve(imageName);
                try (OutputStream os = Files.newOutputStream(file)) {
                    os.write(image.getBytes());
                }
                profileImage.setImage(imageName);
            }
            return profileImageRepository.save(profileImage);
        }
        return null;
    }
}
