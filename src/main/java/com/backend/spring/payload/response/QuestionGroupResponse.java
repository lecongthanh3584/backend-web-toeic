package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class QuestionGroupResponse {
    private Integer groupId;

    private String groupImage;

    private String groupScript;

    private String groupAudio;

    private String groupPassage;

    private String groupText;
}
