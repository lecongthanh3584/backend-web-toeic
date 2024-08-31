package com.backend.spring.controller;

import com.backend.spring.mapper.UserGoalMapper;
import com.backend.spring.entity.UserGoal;
import com.backend.spring.payload.request.UserGoalRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.UserGoalResponse;
import com.backend.spring.service.UserGoal.IUserGoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class UserGoalController {

    @Autowired
    private IUserGoalService iUserGoalService;

    @PostMapping("/user/user-goal/create")
    public ResponseEntity<MessageResponse> createUserGoal(@RequestBody @Valid UserGoalRequest userGoalRequest) {
        UserGoal createdUserGoal = iUserGoalService.createUserGoal(userGoalRequest);

        if(createdUserGoal != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm điểm mục tiêu thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm điểm mục tiêu thất bại!"), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/user/user-goal/get-by-id/{id}")
    public ResponseEntity<UserGoalResponse> getUserGoalById(@PathVariable("id") Integer userGoalId) {
        UserGoalResponse userGoal = UserGoalMapper.mapFromEntityToResponse(iUserGoalService.getUserGoalById(userGoalId));

        if (userGoal != null) {
            return new ResponseEntity<>(userGoal, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/user-goal/get-all")
    public ResponseEntity<List<UserGoalResponse>> getAllUserGoals() {
        List<UserGoalResponse> userGoalList = iUserGoalService.getAllUserGoals().stream().map(
                UserGoalMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(userGoalList, HttpStatus.OK);
    }

    @GetMapping("/user/user-goal/get-by-user/{userId}")
    public ResponseEntity<UserGoalResponse> getUserGoalByUserId(@PathVariable Integer userId) {
        UserGoalResponse userGoal = UserGoalMapper.mapFromEntityToResponse(iUserGoalService.getUserGoalByUserId(userId));

        if (userGoal != null) {
            return new ResponseEntity<>(userGoal, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/user-goal/has-user-goal/{userId}")
    public ResponseEntity<?> hasUserGoalWithUserId(@PathVariable Integer userId) {
        try {
            boolean hasUserGoal = iUserGoalService.hasUserGoalWithUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasUserGoal", hasUserGoal);
            response.put("message", "Người dùng đã thiết lập điểm mục tiêu");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/user/user-goal/update")
    public ResponseEntity<MessageResponse> updateUserGoalByUserId(@RequestBody @Valid UserGoalRequest userGoalRequest) {
        UserGoal updatedUserGoal = iUserGoalService.updateUserGoalByUserId(userGoalRequest);

        if (updatedUserGoal != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật điểm mục tiêu thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật điểm mục tiêu thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }
}
