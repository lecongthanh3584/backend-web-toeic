package com.backend.spring.payload.request;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ProfileImageDto {
    private MultipartFile image;
}
