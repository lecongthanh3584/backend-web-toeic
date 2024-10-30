package com.backend.spring.elastics.services.Exam;

import com.backend.spring.elastics.entities.ExamDocument;

import java.util.List;

public interface IExamDocumentService {
    Iterable<ExamDocument> getAllExamDocument();
}
