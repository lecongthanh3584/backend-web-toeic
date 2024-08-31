package com.backend.spring.service.Exam;

import com.backend.spring.enums.EExamType;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Exam;
import com.backend.spring.payload.request.ExamRequest;
import com.backend.spring.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExamService implements IExamService {

    @Autowired
    private ExamRepository examRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Exam getExamById(Integer examId) {
        return examRepository.findById(examId).orElse(null);
    }

    @Override
    public Exam createExam(ExamRequest examRequest) {
        Exam exam = new Exam();

        exam.setExamName(examRequest.getExamName());
        exam.setExamType(examRequest.getExamType());
        exam.setExamStatus(EStatus.ENABLE.getValue());

        if (examRequest.getExamType().equals(EExamType.MINI_TEST.getValue())) { // mini test
            exam.setExamDuration(3600); // 1 hour in seconds
        } else if (examRequest.getExamType().equals(EExamType.FULL_TEST.getValue())) { // full test
            exam.setExamDuration(7200); // 2 hour in seconds
        } else {
            throw new IllegalArgumentException("Invalid exam type.");
        }

        exam.setCreatedAt(LocalDateTime.now());
        exam.setUpdatedAt(LocalDateTime.now());

        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(ExamRequest examRequest) {
        Optional<Exam> examOptional = examRepository.findById(examRequest.getExamId());

        if(examOptional.isEmpty()) {
            return null;
        }

        Exam existingExam = examOptional.get();
        existingExam.setExamName(examRequest.getExamName());
        existingExam.setExamType(examRequest.getExamType());
        existingExam.setExamStatus(examRequest.getExamStatus());
        existingExam.setUpdatedAt(LocalDateTime.now());

        // Tính toán lại examDuration dựa vào loại kỳ thi mới
        if (examRequest.getExamType().equals(EExamType.MINI_TEST.getValue())) { // mini test
            existingExam.setExamDuration(3600); // 60 minutes in seconds
        } else if (examRequest.getExamType().equals(EExamType.FULL_TEST.getValue())) { // full test
            existingExam.setExamDuration(7200); // 1 hour in seconds
        } else {
            throw new IllegalArgumentException("Invalid exam type.");
        }

        return examRepository.save(existingExam);
    }

    @Override
    public Exam updateExamStatus(Integer examId, Integer status) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if(examOptional.isEmpty()) {
            return null;
        }

        Exam examUpdate = examOptional.get();
        if(status.equals(EStatus.DISABLE.getValue())) {
            examUpdate.setExamStatus(status);
        } else if (status.equals(EStatus.ENABLE.getValue())) {
            examUpdate.setExamStatus(status);
        } else {
            throw new IllegalArgumentException("Invalid Status");
        }

        examUpdate.setUpdatedAt(LocalDateTime.now());

        return examRepository.save(examUpdate);
    }

    @Override
    public boolean deleteExam(Integer examId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if(examOptional.isEmpty()) {
            return false;
        }

        examRepository.deleteById(examId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exam> getMiniTests() {
        return examRepository.findByExamType(EExamType.MINI_TEST.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exam> getFullTests() {
        return examRepository.findByExamType(EExamType.FULL_TEST.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalExams() {
        return examRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExamNameExists(String examName) {
        return examRepository.existsByExamName(examName);
    }

    @Override
    public boolean isExamNameExistsAndExamIdNot(String examName, Integer examId) {
        return examRepository.existsByExamNameAndExamIdNot(examName, examId);
    }
}
