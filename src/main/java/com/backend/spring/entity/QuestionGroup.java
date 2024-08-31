package com.backend.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "question_group")
public class QuestionGroup implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "group_image")
    private String groupImage;

    @Column(name = "group_script", columnDefinition = "TEXT")
    private String groupScript;

    @Column(name = "group_audio")
    private String groupAudio;

    @Column(name = "group_passage", columnDefinition = "TEXT")
    private String groupPassage;

    @Column(name = "group_text", columnDefinition = "TEXT")
    private String groupText;


}
