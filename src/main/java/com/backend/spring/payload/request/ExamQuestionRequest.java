package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ExamQuestionRequest {
    private Integer examQuestionId;

    @NotNull(message = "Id của Exam phải có giá trị!")
    private Integer examId;

    @NotBlank(message = "Nôi dung câu hỏi không được để trống!")
    private String questionContent;

    @NotBlank(message = "Đáp án A không được để trống!")
    private String optionA;

    @NotBlank(message = "Đáp án B không được để trống!")
    private String optionB;

    @NotBlank(message = "Đáp án C không được để trống!")
    private String optionC;

    private String optionD;

    @NotBlank(message = "Đáp án đúng không được để trống!")
    private String correctOption;

    private String questionType;

    private MultipartFile questionImage;

    private String questionScript;

    private String questionExplanation;

    private MultipartFile questionAudio;

    private String questionPassage; // Thêm trường này nếu cần

    @NotNull
    private Integer orderNumber;

    @NotNull(message = "Chỉ định thuộc part không được để trống!")
    private Integer questionPart; // Tùy theo nhu cầu
}
