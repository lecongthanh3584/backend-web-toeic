package com.backend.spring.mapper;

import com.backend.spring.entity.Exam;
import com.backend.spring.payload.response.ExamResponse;

public class ExamMapper {
    public static ExamResponse mapFromEntityToResponse(Exam exam) {
        if(exam == null) {
            return null;
        }

        return new ExamResponse(
                exam.getExamId(),
                exam.getExamName(),
                exam.getExamType(),
                exam.getExamDuration(),
                exam.getExamStatus(),
                exam.getCreatedAt(),
                exam.getUpdatedAt()
        );
    }
}
