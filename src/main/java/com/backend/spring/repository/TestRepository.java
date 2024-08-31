package com.backend.spring.repository;

import com.backend.spring.entity.Test; // Make sure to import the correct Test entity class
import com.backend.spring.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findBySection(Section section);
}
