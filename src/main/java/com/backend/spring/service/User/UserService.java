package com.backend.spring.service.User;

import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.User;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.request.ProfileImageRequest;
import com.backend.spring.payload.request.ProfileRequest;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRoleName(String roleName) {
        return userRepository.findByRoles(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLearner() {
        return userRepository.countByRoles(ERole.LEARNER.getValue());
    }

    @Override
    public User updateStatus(Integer userId, Integer newStatus) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            return null;
        }

        User userUpdate = userOptional.get();

        if(newStatus.equals(EStatus.DISABLE.getValue())) {
            userUpdate.setStatus(newStatus);
        } else if(newStatus.equals(EStatus.ENABLE.getValue())) {
            userUpdate.setStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid new status value!");
        }

        userUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getUserIdByUserName(String userName) {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        if(userOptional.isEmpty()) {
            return 0;
        }

        return userOptional.get().getUserId();

    }

    @Override
    public User updateProfile(ProfileRequest profileRequest) {
        Optional<User> userOptional = userRepository.findById(profileRequest.getUserId());
        if(userOptional.isEmpty()) {
            return null;
        }

        User userUpdate = userOptional.get();
        userUpdate.setFullName(profileRequest.getFullName());
        userUpdate.setAddress(profileRequest.getAddress());
        userUpdate.setGender(profileRequest.getGender());
        userUpdate.setPhoneNumber(profileRequest.getPhoneNumber());
        userUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userUpdate);
    }

    @Override
    public User updateImageProfile(ProfileImageRequest profileImageRequest) throws IOException, NotFoundException {
        Optional<User> userOptional = userRepository.findById(profileImageRequest.getUserId());

        if(userOptional.isPresent()) {
            User userUpdateImageProfile = userOptional.get();

            MultipartFile image = profileImageRequest.getImage();
            if (image != null && !image.isEmpty()) {
                String imageName = image.getOriginalFilename();
                String imagePath = "images/";
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Xóa ảnh cũ
                String oldImage = userUpdateImageProfile.getImage();
                if (oldImage != null && !oldImage.isEmpty()) {
                    Path oldImageFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldImage);
                    Files.deleteIfExists(oldImageFile);
                }
                // Lưu ảnh mới
                Path file = uploadPath.resolve(imageName);
                try (OutputStream os = Files.newOutputStream(file)) {
                    os.write(image.getBytes());
                }

                userUpdateImageProfile.setImage(imageName);
                userUpdateImageProfile.setUpdatedAt(LocalDateTime.now());

                return userRepository.save(userUpdateImageProfile);
            } else {
                throw new NotFoundException("Image not found");
            }
        }

        return null;
    }

    @Override
    public User updatePassword(ChangePasswordRequest changePasswordRequest) {
        Optional<User> userOptional = userRepository.findById(changePasswordRequest.getUserId());
        if(userOptional.isEmpty()) {
            return null;
        }

        User userUpdate = userOptional.get();

        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userUpdate.getPassword())) {
            return null;
        }

        userUpdate.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userUpdate);
    }

}
