package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TopicResponse {

    private Integer topicId;

    private String topicName;

    private String image;

    private Integer topicStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
