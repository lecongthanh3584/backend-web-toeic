package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class QuestionRequest {
    private Integer questionId;

    @NotNull(message = "Id của phần thi phải có giá trị!")
    private Integer sectionId;

    private Integer groupId;

    private String questionContent;  //Áp dụng cho các câu nhỏ lẻ

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctOption;

    private String questionType;

    private MultipartFile questionImage;

    private String questionScript;

    private String questionExplanation;

    private MultipartFile questionAudio;

    private String questionPassage;  //Áp dụng cho đề bài part 6

    private String questionText;  //Áp dụng cho đề bài part 7

    private String suggestedAnswer;

    private Integer questionStatus = EStatus.ENABLE.getValue();
}
