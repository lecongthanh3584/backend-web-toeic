package com.backend.spring.services.User;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.entities.Role;
import com.backend.spring.entities.AuthToken;
import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.User;
import com.backend.spring.exception.AlreadyExistsException;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.payload.request.*;
import com.backend.spring.repositories.RoleRepository;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.repositories.AuthTokenRepository;
import com.backend.spring.services.Email.EmailService;
import com.backend.spring.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${spring.domain.front-end}")
    private String domainMail;

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllLearners(Integer pageNumber, String keyword, String... sortBys) {

        List<Sort.Order> orders = getListSort(sortBys);

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(orders));

        return userRepository.getAllLearners(ERole.LEARNER.name(), keyword, pageable);
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
        User userLogin = UserUtil.getDataUserLogin();

        if(userLogin == null) {
            return null;
        }

        userLogin.setFullName(profileRequest.getFullName());
        userLogin.setAddress(profileRequest.getAddress());
        userLogin.setPhoneNumber(profileRequest.getPhoneNumber());

        return userRepository.save(userLogin);
    }

    @Override
    public User updateImageProfile(ProfileImageRequest profileImageRequest) throws IOException, NotFoundException {
        User userLogin = UserUtil.getDataUserLogin();

        if (userLogin != null) {
            MultipartFile image = profileImageRequest.getImage();
            if (image != null && !image.isEmpty()) {
                String imageName = image.getOriginalFilename();
                String imagePath = "images/";
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Xóa ảnh cũ
                String oldImage = userLogin.getImage();
                if (oldImage != null && !oldImage.isEmpty()) {
                    Path oldImageFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldImage);
                    Files.deleteIfExists(oldImageFile);
                }
                // Lưu ảnh mới
                Path file = uploadPath.resolve(imageName);
                try (OutputStream os = Files.newOutputStream(file)) {
                    os.write(image.getBytes());
                }

                userLogin.setImage(imageName);

                return userRepository.save(userLogin);
            } else {
                throw new NotFoundException("Image not found");
            }
        }

        return null;
    }

    @Override
    public User updatePassword(ChangePasswordRequest changePasswordRequest) {
        User userLogin = UserUtil.getDataUserLogin();
        if(userLogin == null) {
            return null;
        }

        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), userLogin.getPassword())) {
            return null;
        }

        userLogin.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userLogin.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userLogin);
    }

    private List<Sort.Order> getListSort(String... sortBys) {
        List<Sort.Order> orders = new ArrayList<>();

        for(String sortBy : sortBys) {
            String[] sort = sortBy.split(":"); //Tách từng phần để xác định xem là sắp xếp tăng dần hay giảm dần

            if (sort.length == 2) {
                String field = sort[0].trim();
                String direction = sort[1].trim();

                if (direction.equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, field));
                } else if (direction.equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, field));
                }
            } else {
                throw new RuntimeException(MessageConstant.INVALID_PARAMETER);
            }
        }

        return orders;
    }

}
