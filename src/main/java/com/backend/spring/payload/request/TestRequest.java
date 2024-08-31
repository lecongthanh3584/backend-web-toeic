package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestRequest {
    private Integer testId;

    @NotBlank(message = "Tên bài test không được để trống!")
    private String testName;// Default initial value of testProgress is set to 0

    private Integer testParticipants = 0; // Default initial value of testParticipants is set to 0

    private Integer testStatus = EStatus.ENABLE.getValue();

    @NotNull(message = "Id của phần không được trống!")
    private Integer sectionId;
}
