package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExamRequest {

    private Integer examId;

    @NotBlank(message = "Tên bài thi không được để trống!")
    private String examName;

    @NotNull(message = "Loại bài thi không được để trống!")
    private Integer examType; //Giá trị 0 là minitest, 1 là fulltest

    private Integer examStatus = EStatus.ENABLE.getValue();
}
