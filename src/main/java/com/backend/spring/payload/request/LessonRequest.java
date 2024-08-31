package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonRequest {
    private Integer lessonId;

    @NotBlank(message = "Tên bài học không được để trống!")
    private String lessonName;

    private Integer lessonStatus = EStatus.ENABLE.getValue();

    @NotNull(message = "Id của phần thi không được trống!")
    private Integer sectionId;
}
