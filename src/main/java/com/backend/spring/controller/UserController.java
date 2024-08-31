package com.backend.spring.controller;

import com.backend.spring.enums.ERole;
import com.backend.spring.mapper.UserMapper;
import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.request.ProfileRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.UserResponse;
import com.backend.spring.service.User.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/admin/get-all-learners")
    public ResponseEntity<List<UserResponse>> getAllLearns() {
        List<User> allLearns = iUserService.findByRoleName(ERole.LEARNER.name());
        List<UserResponse> userResponseList = allLearns.stream().map(
                UserMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(userResponseList, HttpStatus.OK);

    }

    @GetMapping("/admin/count-learners")
    public ResponseEntity<Long> countLearners() {
        Long learnerCount = iUserService.countLearner();

        return ResponseEntity.ok(learnerCount);
    }

    @PutMapping("/admin/update-status/{userId}")
    public ResponseEntity<MessageResponse> updateUserStatus(@PathVariable Integer userId, @RequestBody Integer newStatus) {
        User userUpdate = iUserService.updateStatus(userId, newStatus);

        if(userUpdate != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái của người dùng thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái của người dùng thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/getUserIdByUsername/{username}")
    public ResponseEntity<Integer> getUserIdByUsername(@PathVariable String username) {
        Integer userId = iUserService.getUserIdByUserName(username);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @GetMapping("/admin/get-by-id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {

        UserResponse userReturn = UserMapper.mapFromEntityToResponse(iUserService.getUserById(userId));

        if(userReturn != null) {
            return new ResponseEntity<>(userReturn, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin/update-profile")
    public ResponseEntity<MessageResponse> updateProfile(@RequestBody @Valid ProfileRequest profileRequest) {
       User userUpdate = iUserService.updateProfile(profileRequest);

       if(userUpdate != null) {
           return new ResponseEntity<>(new MessageResponse("Cập nhật thông tin tài khoản thành công!"), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new MessageResponse("Cập nhật thông tin tài khoản thất bại!"), HttpStatus.NOT_FOUND);
       }
    }

    @PutMapping("/admin/change-password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody @Valid  ChangePasswordRequest changePasswordRequest) {
       User userReturn = iUserService.updatePassword(changePasswordRequest);

       if(userReturn != null) {
           return new ResponseEntity<>(new MessageResponse("Cập nhật mật khẩu thành công!"), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new MessageResponse("Cập nhật mật khẩu thất bại, mật khẩu cũ không chính xác!"), HttpStatus.BAD_REQUEST);
       }
    }

}
