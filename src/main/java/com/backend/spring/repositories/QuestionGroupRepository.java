package com.backend.spring.repositories;

import com.backend.spring.entities.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Integer> {

}
