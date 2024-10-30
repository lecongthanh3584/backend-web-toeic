package com.backend.spring.elastics.repositories;

import com.backend.spring.elastics.entities.ExamDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSearchRepository extends ElasticsearchRepository<ExamDocument, Integer> {
}
