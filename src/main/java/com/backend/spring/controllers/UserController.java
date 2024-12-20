package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.mapper.ExamMapper;
import com.backend.spring.mapper.UserMapper;
import com.backend.spring.entities.User;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.request.ProfileImageRequest;
import com.backend.spring.payload.request.ProfileRequest;
import com.backend.spring.payload.response.ExamResponse;
import com.backend.spring.payload.response.UserResponse;
import com.backend.spring.payload.response.main.PaginationData;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.User.IUserService;
import com.backend.spring.utils.UserUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;

    //admin
    @GetMapping("/admin/user/get-all-learners")
    public ResponseEntity<?> getAllLearns(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "created_at:desc") String... sortBys
    ) {
        Page<User> userPage = iUserService.getAllLearners(page, keyword, sortBys);

        PaginationData paginationData = PaginationData.builder().totalPage(userPage.getTotalPages()).totalElement(userPage.getTotalElements())
                .pageNumber(userPage.getPageable().getPageNumber()).pageSize(userPage.getPageable().getPageSize()).build();

        List<UserResponse> userResponseList = userPage.getContent().stream().map(
                UserMapper::mapFromEntityToResponse
        ).toList();

        if (!userResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.User.GET_DATA_SUCCESS,
                    paginationData, userResponseList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.User.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
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

    @PutMapping("/admin/user/change-password")
    public ResponseEntity<?> changePasswordAdmin(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        User userReturn = iUserService.updatePassword(changePasswordRequest);

        if(userReturn != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.User.CHANGE_PASSWORD_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.User.CHANGE_PASSWORD_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    //user
    @PutMapping("/user/user/update-image-profile")
    public ResponseEntity<?> updateImageProfileUser(@ModelAttribute @Valid ProfileImageRequest profileImageRequest) {
         return updateImageProfileAdmin(profileImageRequest);
    }

    @PutMapping("/user/user/change-password")
    public ResponseEntity<?> changePasswordUser(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return changePasswordAdmin(changePasswordRequest);
    }

    @GetMapping("/user/get-user-infor")
    public ResponseEntity<?> getUserInfor() {
        User userLogin = UserUtil.getDataUserLogin();
        UserResponse userResponse = UserMapper.mapFromEntityToResponse(userLogin);

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.User.GET_DATA_SUCCESS, userResponse),
                HttpStatus.OK);
    }

}
