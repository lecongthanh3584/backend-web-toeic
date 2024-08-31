package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SectionRequest {
    private Integer sectionId;

    @NotBlank(message = "Tên phần không được bỏ trống!")
    private String name;

    @NotBlank(message = "Mô tả phần thi không được bỏ trống!")
    private String description;

    private Integer status = EStatus.ENABLE.getValue();

    private MultipartFile image;

    @NotNull(message = "Loại phần thi không được bỏ trống!")
    private Integer type;
}

