package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.mapper.UserMapper;
import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.request.ProfileImageRequest;
import com.backend.spring.payload.request.ProfileRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.UserResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.User.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/admin/user/get-all-learners")
    public ResponseEntity<?> getAllLearns() {
        List<User> allLearns = iUserService.findByRoleName(ERole.LEARNER.name());
        List<UserResponse> userResponseList = allLearns.stream().map(
                UserMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.User.GET_DATA_SUCCESS, userResponseList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/user/count-learners")
    public ResponseEntity<?> countLearners() {
        Long learnerCount = iUserService.countLearner();

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.User.GET_DATA_SUCCESS, learnerCount),
                HttpStatus.OK);
    }

    @PutMapping("/admin/user/update-status/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("userId") @Min(1) Integer userId, @RequestBody Integer newStatus) {
        User userUpdate = iUserService.updateStatus(userId, newStatus);

        if(userUpdate != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.User.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.User.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/user/getUserIdByUsername/{username}")
    public ResponseEntity<?> getUserIdByUsername(@PathVariable("username") String username) {
        Integer userId = iUserService.getUserIdByUserName(username);

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.User.GET_DATA_SUCCESS, userId),
                HttpStatus.OK);
    }

    @GetMapping("/admin/user/get-by-id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") @Min(1) Integer userId) {

        UserResponse userReturn = UserMapper.mapFromEntityToResponse(iUserService.getUserById(userId));

        if(userReturn != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.User.GET_DATA_SUCCESS, userReturn),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.User.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin/user/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileRequest profileRequest) {
       User userUpdate = iUserService.updateProfile(profileRequest);

       if(userUpdate != null) {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.User.UPDATE_INFOR_SUCCESS),
                   HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.User.UPDATE_INFOR_FAILED),
                   HttpStatus.BAD_REQUEST);
       }
    }

    @PutMapping("/admin/user/update-image-profile")
    public ResponseEntity<?> updateImageProfileAdmin(@ModelAttribute @Valid ProfileImageRequest profileImageRequest) {
        try {
            User userReturn = iUserService.updateImageProfile(profileImageRequest);

            if(userReturn != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.User.UPDATE_IMAGE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.User.UPDATE_IMAGE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | NotFoundException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/user/user/update-image-profile")
    public ResponseEntity<?> updateImageProfileUser(@ModelAttribute @Valid ProfileImageRequest profileImageRequest) {
         return updateImageProfileAdmin(profileImageRequest);
    }

    @PutMapping("/admin/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
       User userReturn = iUserService.updatePassword(changePasswordRequest);

       if(userReturn != null) {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.User.CHANGE_PASSWORD_SUCCESS),
                   HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.User.CHANGE_PASSWORD_FAILED),
                   HttpStatus.BAD_REQUEST);
       }
    }

}
