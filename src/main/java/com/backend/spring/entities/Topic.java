package com.backend.spring.entities;

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
@Table(name = "topic")
public class Topic extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "topic_name", nullable = false, unique = true)
    private String topicName;

    @Column(name = "image")
    private String image;

    @Column(name = "topic_status", nullable = false)
    private Integer topicStatus;

}


