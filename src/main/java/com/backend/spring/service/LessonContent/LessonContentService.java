package com.backend.spring.service.LessonContent;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Lesson;
import com.backend.spring.entity.LessonContent;
import com.backend.spring.payload.request.LessonContentRequest;
import com.backend.spring.repository.LessonContentRepository;
import com.backend.spring.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LessonContentService implements ILessonContentService {

    @Autowired
    private LessonContentRepository lessonContentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public LessonContent createLessonContent(LessonContentRequest lessonContentRequest) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonContentRequest.getLessonId());
        if (lessonOptional.isPresent()) {
            LessonContent lessonContent = new LessonContent();

            lessonContent.setLesson(lessonOptional.get());
            lessonContent.setTitle(lessonContentRequest.getTitle());
            lessonContent.setContent(lessonContentRequest.getContent());
            lessonContent.setLessonContentStatus(EStatus.ENABLE.getValue());

            lessonContent.setCreatedAt(LocalDateTime.now());
            lessonContent.setUpdatedAt(LocalDateTime.now());

            return lessonContentRepository.save(lessonContent);
        }

        return null;
    }

    @Override
    public LessonContent updateLessonContent(LessonContentRequest lessonContentRequest) {
        Optional<LessonContent> lessonContentOptional = lessonContentRepository.findById(lessonContentRequest.getContentId());
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonContentRequest.getLessonId());

        if (lessonContentOptional.isPresent() && lessonOptional.isPresent()) {
            LessonContent lessonContentUpdate = lessonContentOptional.get();

            lessonContentUpdate.setLesson(lessonOptional.get());
            lessonContentUpdate.setTitle(lessonContentRequest.getTitle());
            lessonContentUpdate.setContent(lessonContentRequest.getContent());
            lessonContentUpdate.setLessonContentStatus(lessonContentRequest.getLessonContentStatus());

            lessonContentUpdate.setUpdatedAt(LocalDateTime.now());

            return lessonContentRepository.save(lessonContentUpdate);
        }

        return null;
    }

    @Override
    public LessonContent updateLessonContentStatus(Integer id, Integer newStatus) {
       Optional<LessonContent> lessonContentOptional = lessonContentRepository.findById(id);
       if(lessonContentOptional.isEmpty()) {
           return null;
       }

       LessonContent lessonContentUpdate = lessonContentOptional.get();
       if(newStatus.equals(EStatus.ENABLE.getValue())) {
           lessonContentUpdate.setLessonContentStatus(newStatus);
       } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
           lessonContentUpdate.setLessonContentStatus(newStatus);
       } else {
           throw new IllegalArgumentException("Invalid new status value");
       }

        lessonContentUpdate.setUpdatedAt(LocalDateTime.now());

       return lessonContentRepository.save(lessonContentUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonContent> getAllLessonContents() {
        return lessonContentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonContent getLessonContentById(Integer id) {
        return lessonContentRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteLessonContent(Integer id) {
        Optional<LessonContent> lessonContentOptional = lessonContentRepository.findById(id);
        if(lessonContentOptional.isEmpty()) {
            return false;
        }

        lessonContentRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonContent> getLessonContentsByLessonId(Integer lessonId) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            Lesson lesson = lessonOptional.get();
            return lessonContentRepository.findByLesson(lesson);
        }

        return Collections.emptyList();
    }
}
