package com.backend.spring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "grammar")
public class Grammar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grammar_id")
    private Integer grammarId;

    @Column(name = "grammar_name", nullable = false, unique = true)
    private String grammarName;

    @Column(name = "grammar_status", nullable = false)
    private Integer grammarStatus;

}
