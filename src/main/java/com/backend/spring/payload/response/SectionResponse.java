package com.backend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SectionResponse {
    private Integer id;

    private String name;

    private int status;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private Integer type; // 1: Nghe, 2: Đọc, 3: Nói, 4: Viết

}
