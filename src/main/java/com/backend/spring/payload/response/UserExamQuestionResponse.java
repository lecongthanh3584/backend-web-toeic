package com.backend.spring.payload.response;

import com.backend.spring.entity.ExamQuestion;
import com.backend.spring.entity.UserExam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
