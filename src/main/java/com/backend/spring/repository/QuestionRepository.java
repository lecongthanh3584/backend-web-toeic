package com.backend.spring.repository;

import com.backend.spring.entity.Question;
import com.backend.spring.entity.Section;
import com.backend.spring.entity.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findBySection(Section section);

    List<Question> findByQuestionGroup(QuestionGroup questionGroup);

    List<Question> findBySectionAndQuestionType(Section section, String questionType);
}

