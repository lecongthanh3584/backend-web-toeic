package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.UserGoalMapper;
import com.backend.spring.entities.UserGoal;
import com.backend.spring.payload.request.UserGoalRequest;
import com.backend.spring.payload.response.UserGoalResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.UserGoal.IUserGoalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class UserGoalController {

    private final IUserGoalService iUserGoalService;

    @PostMapping("/user/user-goal/create")
    public ResponseEntity<?> createUserGoal(@RequestBody @Valid UserGoalRequest userGoalRequest) {
        UserGoal createdUserGoal = iUserGoalService.createUserGoal(userGoalRequest);

        if(createdUserGoal != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.UserGoal.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.UserGoal.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/user-goal/get-by-id/{id}")
    public ResponseEntity<?> getUserGoalById(@PathVariable("id") @Min(1) Integer userGoalId) {
        UserGoalResponse userGoal = UserGoalMapper.mapFromEntityToResponse(iUserGoalService.getUserGoalById(userGoalId));

        if (userGoal != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserGoal.GET_DATA_SUCCESS, userGoal),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.UserGoal.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/user-goal/get-all")
    public ResponseEntity<?> getAllUserGoals() {
        List<UserGoalResponse> userGoalList = iUserGoalService.getAllUserGoals().stream().map(
                UserGoalMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserGoal.GET_DATA_SUCCESS, userGoalList),
                HttpStatus.OK);
    }

    @GetMapping("/user/user-goal/get-by-user/{userId}")
    public ResponseEntity<?> getUserGoalByUserId(@PathVariable("userId") @Min(1) Integer userId) {
        UserGoalResponse userGoal = UserGoalMapper.mapFromEntityToResponse(iUserGoalService.getUserGoalByUserId(userId));

        if (userGoal != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserGoal.GET_DATA_SUCCESS, userGoal),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.UserGoal.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/user-goal/has-user-goal/{userId}")
    public ResponseEntity<?> hasUserGoalWithUserId(@PathVariable("userId") @Min(1) Integer userId) {
        try {
            boolean hasUserGoal = iUserGoalService.hasUserGoalWithUserId(userId);
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserGoal.USER_HAS_SET, hasUserGoal),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/user-goal/update")
    public ResponseEntity<?> updateUserGoalByUser(@RequestBody @Valid UserGoalRequest userGoalRequest) {
        UserGoal updatedUserGoal = iUserGoalService.updateUserGoalByUserId(userGoalRequest);

        if (updatedUserGoal != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.UserGoal.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.UserGoal.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
