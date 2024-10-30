package com.backend.spring.elastics.services.Exam;

import com.backend.spring.elastics.entities.ExamDocument;
import com.backend.spring.elastics.repositories.ExamSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamDocumentService implements IExamDocumentService {

    private final ExamSearchRepository examSearchRepository;
    @Override
    public Iterable<ExamDocument> getAllExamDocument() {
        return examSearchRepository.findAll();
    }
}
