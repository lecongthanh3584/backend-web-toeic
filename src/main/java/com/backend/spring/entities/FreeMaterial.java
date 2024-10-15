package com.backend.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "free_material")
public class FreeMaterial extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Integer materialId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "material_status", nullable = false)  // Thêm trường material_status
    private Integer materialStatus;
}
