package com.backend.spring.repositories;

import com.backend.spring.entities.Test; // Make sure to import the correct Test entity class
import com.backend.spring.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findBySection(Section section);
}
