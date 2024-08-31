package com.backend.spring.repository;

import com.backend.spring.entity.ScoreTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreTableRepository extends JpaRepository<ScoreTable, Integer> {
    List<ScoreTable> findByType(int i);

}
