package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserExamRequest {
    private Integer userExamId;

    @NotNull(message = "Id người dùng không được bỏ trống!")
    private Integer userId;

    @NotNull(message = "Id của bài thi không được bỏ trống!")
    private Integer examId;

    @NotNull(message = "Thời gian hoàn thành bài thi không được bỏ trống!")
    private Integer completionTime;

    @NotNull(message = "Số câu nghe trả lời đúng không được bỏ trống!")
    private Integer numListeningCorrectAnswers;

    @NotNull(message = "Điểm phần nghe không được bỏ trống!")
    private Integer listeningScore;

    @NotNull(message = "Số câu đọc trả lời đúng không được bỏ trống!")
    private Integer numReadingCorrectAnswers;

    @NotNull(message = "Điểm phần đọc không được bỏ trống!")
    private Integer readingScore;

    @NotNull(message = "Tổng điểm không được bỏ trống!")
    private Integer totalScore;

    @NotNull(message = "Số câu đúng không được bỏ trống!")
    private Integer numCorrectAnswers;

    @NotNull(message = "Số câu sai không được bỏ trống!")
    private Integer numWrongAnswers;

    @NotNull(message = "Số câu hỏi bỏ qua không được bỏ trống!")
    private Integer numSkippedQuestions;

    @NotNull(message = "Điểm mục tiêu không được bỏ trống!")
    private Integer goalScore;
}

