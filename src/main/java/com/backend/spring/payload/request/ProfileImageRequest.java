package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ProfileImageRequest {

    @NotNull(message = "Id của user không được để trống")
    private int userId;

    @NotNull(message = "Ảnh phải được gửi lên")
    private MultipartFile image;
}
