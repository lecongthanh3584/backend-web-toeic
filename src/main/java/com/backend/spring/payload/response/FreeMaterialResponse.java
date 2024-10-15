package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FreeMaterialResponse {

    private Integer materialId;

    private String title;

    private String description;

    private String fileName;

    private Integer materialStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
