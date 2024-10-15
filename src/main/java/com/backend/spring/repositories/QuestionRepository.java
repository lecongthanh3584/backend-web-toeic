package com.backend.spring.repositories;

import com.backend.spring.entities.Question;
import com.backend.spring.entities.Section;
import com.backend.spring.entities.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findBySection(Section section);

    List<Question> findByQuestionGroup(QuestionGroup questionGroup);

    List<Question> findBySectionAndQuestionType(Section section, String questionType);
}

