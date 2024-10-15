package com.backend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExamQuestionResponse {

    private Integer userExamQuestionId;

    private String selectedOption;

    private Integer isCorrect;
}
