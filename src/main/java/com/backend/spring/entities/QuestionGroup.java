package com.backend.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "question_group")
public class QuestionGroup extends BaseEntity {
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
