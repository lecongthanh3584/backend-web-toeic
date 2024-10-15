package com.backend.spring.services.Vocabulary;

import com.backend.spring.entities.Vocabulary;
import com.backend.spring.payload.request.VocabularyRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IVocabularyService {
    List<Vocabulary> getAllVocabularies();
    boolean uploadVocabularyFromExcel(MultipartFile file, Integer topicId) throws IOException;
    Vocabulary getVocabularyById(Integer id);
    boolean deleteVocabulary(Integer vocabularyId) throws IOException;
    Vocabulary createVocabulary(VocabularyRequest vocabularyRequest) throws IOException;
    Vocabulary updateVocabulary(VocabularyRequest vocabularyRequest) throws IOException;
    Vocabulary updateVocabularyStatus(Integer vocabularyId, Integer newStatus);
    List<Vocabulary> getVocabulariesByTopicId(Integer topicId);

}
