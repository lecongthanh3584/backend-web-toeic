package com.backend.spring.services.Exam;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EExamType;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Exam;
import com.backend.spring.payload.request.ExamRequest;
import com.backend.spring.repositories.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ExamService implements IExamService {

    private final ExamRepository examRepository;

    @Override
    public Page<Exam> getFullTests(Integer pageNumber, String keyword, String... sortBys) {

        List<Sort.Order> orders = getListSort(sortBys);

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(orders));

        return examRepository.getExamByType(EExamType.FULL_TEST.getValue(), keyword, pageable);
    }

    @Override
    public Page<Exam> getMiniTests(Integer pageNumber, String keyword, String... sortBys) {

        List<Sort.Order> orders = getListSort(sortBys);

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(orders));

        return examRepository.getExamByType(EExamType.MINI_TEST.getValue(), keyword, pageable);
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

        return examRepository.save(examUpdate);
    }

    @Override
    public boolean deleteExam(Integer examId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if(examOptional.isEmpty()) {
            return false;
        }

        examOptional.get().setDeletedAt(LocalDateTime.now());
        examRepository.save(examOptional.get());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exam> getFullTestsEnable(Integer pageNumber, String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, 10);

        return examRepository.findByExamTypeAndEnable(EExamType.FULL_TEST.getValue(), EStatus.ACTIVATE.getValue(), keyword, pageable);
    }

    @Override
    public Page<Exam> getMiniTestsEnable(Integer pageNumber, String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, 10);

        return examRepository.findByExamTypeAndEnable(EExamType.MINI_TEST.getValue(), EStatus.ACTIVATE.getValue(), keyword, pageable);
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

    private List<Sort.Order> getListSort(String... sortBys) {
        List<Sort.Order> orders = new ArrayList<>();

        for(String sortBy : sortBys) {
            String[] sort = sortBy.split(":"); //Tách từng phần để xác định xem là sắp xếp tăng dần hay giảm dần

            if (sort.length == 2) {
                String field = sort[0].trim();
                String direction = sort[1].trim();

                if (direction.equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, field));
                } else if (direction.equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, field));
                }
            } else {
                throw new RuntimeException(MessageConstant.INVALID_PARAMETER);
            }
        }

        return orders;
    }
}
