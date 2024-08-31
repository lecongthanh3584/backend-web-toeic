package com.backend.spring.service.Lesson;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Lesson;
import com.backend.spring.entity.Section;
import com.backend.spring.payload.request.LessonRequest;
import com.backend.spring.repository.LessonRepository;
import com.backend.spring.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LessonService implements ILessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Lesson getLessonById(Integer id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public Lesson createLesson(LessonRequest lessonRequest) {
        Optional<Section> sectionOptional = sectionRepository.findById(lessonRequest.getSectionId());
        if (sectionOptional.isPresent()) {

            Lesson lesson = new Lesson();

            lesson.setSection(sectionOptional.get());
            lesson.setLessonName(lessonRequest.getLessonName());
            lesson.setLessonStatus(EStatus.ENABLE.getValue());
            lesson.setCreatedAt(LocalDateTime.now());
            lesson.setUpdatedAt(LocalDateTime.now());

            return lessonRepository.save(lesson);
        }

        return null;
    }

    @Override
    public boolean deleteLesson(Integer id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        if(lessonOptional.isEmpty()) {
            return false;
        }

        lessonRepository.deleteById(id);
        return true;
    }

    @Override
    public Lesson updateLesson(LessonRequest lessonRequest) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonRequest.getLessonId());
        Optional<Section> sectionOptional = sectionRepository.findById(lessonRequest.getSectionId());

        if(lessonOptional.isEmpty() || sectionOptional.isEmpty()) {
            return null;
        }

        Lesson lessonUpdate = lessonOptional.get();

        lessonUpdate.setSection(sectionOptional.get());
        lessonUpdate.setLessonName(lessonRequest.getLessonName());
        lessonUpdate.setLessonStatus(lessonRequest.getLessonStatus());
        lessonUpdate.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lessonUpdate);
    }

    @Override
    public Lesson updateLessonStatus(Integer id, Integer newStatus) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        if(lessonOptional.isEmpty()) {
            return null;
        }

        Lesson lessonUpdate = lessonOptional.get();

        if(newStatus.equals(EStatus.ENABLE.getValue())) {
            lessonUpdate.setLessonStatus(newStatus);
        } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
            lessonUpdate.setLessonStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }

        lessonUpdate.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lessonUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsBySectionId(Integer sectionId) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            return lessonRepository.findBySection(section);
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public String getLessonNameById(Integer id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        return lessonOptional.map(Lesson::getLessonName).orElse(null);
    }

}
