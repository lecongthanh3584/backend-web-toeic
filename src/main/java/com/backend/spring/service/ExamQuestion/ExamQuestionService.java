package com.backend.spring.service.ExamQuestion;

import com.backend.spring.entity.ExamQuestion;
import com.backend.spring.entity.Exam;
import com.backend.spring.payload.request.ExamQuestionRequest;
import com.backend.spring.repository.ExamQuestionRepository;
import com.backend.spring.repository.ExamRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExamQuestionService implements IExamQuestionService {

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Override
    public boolean uploadExamQuestionsFromExcel(MultipartFile file, Integer examId) throws IOException {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if(examOptional.isEmpty()) {
            return false;
        }

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<ExamQuestion> examQuestions = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                // Bỏ qua dòng tiêu đề (nếu có)
                continue;
            }

            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setQuestionContent(getStringValue(row.getCell(0)));
            examQuestion.setOptionA(getStringValue(row.getCell(1)));
            examQuestion.setOptionB(getStringValue(row.getCell(2)));
            examQuestion.setOptionC(getStringValue(row.getCell(3)));
            examQuestion.setOptionD(getStringValue(row.getCell(4)));
            examQuestion.setCorrectOption(getStringValue(row.getCell(5)));
            examQuestion.setQuestionImage(getStringValue(row.getCell(6)));
            examQuestion.setQuestionScript(getStringValue(row.getCell(7)));
            examQuestion.setQuestionAudio(getStringValue(row.getCell(8)));
            examQuestion.setQuestionExplanation(getStringValue(row.getCell(9)));
            examQuestion.setOrderNumber((int) row.getCell(10).getNumericCellValue());
            examQuestion.setQuestionPassage(getStringValue(row.getCell(11)));
            examQuestion.setQuestionPart((int) row.getCell(12).getNumericCellValue());
            examQuestion.setQuestionType(getStringValue(row.getCell(13)));
            examQuestion.setCreatedAt(LocalDateTime.now());
            examQuestion.setUpdatedAt(LocalDateTime.now());

            // Sử dụng examId từ tham số
            examQuestion.setExam(examOptional.get());

            examQuestions.add(examQuestion);
        }

        examQuestionRepository.saveAll(examQuestions);
        return true;
    }

    private String getStringValue(Cell cell) {
        if (cell != null) {
            // Kiểm tra kiểu dữ liệu của ô Excel
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                // Xử lý giá trị kiểu số
                return String.valueOf((int) cell.getNumericCellValue());
            }
        }
        return ""; // Trả về giá trị rỗng nếu ô là null hoặc không có giá trị
    }

    @Override
    public ExamQuestion createExamQuestion(ExamQuestionRequest examQuestionRequest) throws IOException {

        Optional<Exam> examOptional = examRepository.findById(examQuestionRequest.getExamId());
        if(examOptional.isEmpty()) {
            return null;
        }

        MultipartFile questionImage = examQuestionRequest.getQuestionImage();
        MultipartFile questionAudio = examQuestionRequest.getQuestionAudio();

        String imageName = null;
        String audioName = null;
        String imagePath = "images/";
        String audioPath = "audios/";
        Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);
        Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

        //Upload ảnh
        if (questionImage != null && !questionImage.isEmpty()) {
            imageName = questionImage.getOriginalFilename();
            if (!Files.exists(uploadImagePath)) {
                Files.createDirectories(uploadImagePath);
            }
            Path imageFile = uploadImagePath.resolve(imageName);
            try (OutputStream osImage = Files.newOutputStream(imageFile)) {
                osImage.write(questionImage.getBytes());
            }
        }

        //upload audio
        if (questionAudio != null && !questionAudio.isEmpty()) {
            audioName = questionAudio.getOriginalFilename();
            if (!Files.exists(uploadAudioPath)) {
                Files.createDirectories(uploadAudioPath);
            }
            Path audioFile = uploadAudioPath.resolve(audioName);
            try (OutputStream osAudio = Files.newOutputStream(audioFile)) {
                osAudio.write(questionAudio.getBytes());
            }
        }

        ExamQuestion examQuestion = new ExamQuestion();

        examQuestion.setQuestionContent(examQuestionRequest.getQuestionContent());
        examQuestion.setOptionA(examQuestionRequest.getOptionA());
        examQuestion.setOptionB(examQuestionRequest.getOptionB());
        examQuestion.setOptionC(examQuestionRequest.getOptionC());
        examQuestion.setOptionD(examQuestionRequest.getOptionD());
        examQuestion.setCorrectOption(examQuestionRequest.getCorrectOption());
        examQuestion.setQuestionType(examQuestionRequest.getQuestionType());
        examQuestion.setQuestionImage(imageName); // Có thể là null
        examQuestion.setQuestionScript(examQuestionRequest.getQuestionScript());
        examQuestion.setQuestionExplanation(examQuestionRequest.getQuestionExplanation());
        examQuestion.setQuestionAudio(audioName); // Có thể là null
        examQuestion.setCreatedAt(LocalDateTime.now());
        examQuestion.setUpdatedAt(LocalDateTime.now());
        examQuestion.setExam(examOptional.get());
        examQuestion.setQuestionPart(examQuestionRequest.getQuestionPart());

        return examQuestionRepository.save(examQuestion);
    }

    @Override
    public ExamQuestion updateExamQuestion(ExamQuestionRequest examQuestionRequest) throws IOException {
        Optional<ExamQuestion> examQuestionOptional = examQuestionRepository.findById(examQuestionRequest.getExamQuestionId());
        Optional<Exam> examOptional = examRepository.findById(examQuestionRequest.getExamId());

        if(examOptional.isEmpty() || examQuestionOptional.isEmpty()) {
            return null;
        }

        ExamQuestion existingExamQuestion = examQuestionOptional.get();
        existingExamQuestion.setQuestionContent(examQuestionRequest.getQuestionContent());
        existingExamQuestion.setOptionA(examQuestionRequest.getOptionA());
        existingExamQuestion.setOptionB(examQuestionRequest.getOptionB());
        existingExamQuestion.setOptionC(examQuestionRequest.getOptionC());
        existingExamQuestion.setOptionD(examQuestionRequest.getOptionD());
        existingExamQuestion.setCorrectOption(examQuestionRequest.getCorrectOption());
        existingExamQuestion.setQuestionType(examQuestionRequest.getQuestionType());
        existingExamQuestion.setQuestionScript(examQuestionRequest.getQuestionScript());
        existingExamQuestion.setQuestionExplanation(examQuestionRequest.getQuestionExplanation());
        existingExamQuestion.setUpdatedAt(LocalDateTime.now());

        MultipartFile questionImage = examQuestionRequest.getQuestionImage();
        MultipartFile questionAudio = examQuestionRequest.getQuestionAudio();

        String imageName = existingExamQuestion.getQuestionImage(); // Lấy tên ảnh hiện tại
        String audioName = existingExamQuestion.getQuestionAudio(); // Lấy tên audio hiện tại

        String imagePath = "images/";
        String audioPath = "audios/";

        Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);
        Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

        if (questionImage != null && !questionImage.isEmpty()) {
            // Nếu có ảnh mới được upload, xử lý ảnh và cập nhật tên mới
            imageName = questionImage.getOriginalFilename();

            if (!Files.exists(uploadImagePath)) {
                Files.createDirectories(uploadImagePath);
            }

            // Xóa ảnh cũ nếu có
            String oldImage = existingExamQuestion.getQuestionImage();
            if (oldImage != null && !oldImage.isEmpty()) {
                Path oldImageFile = uploadImagePath.resolve(oldImage);
                Files.deleteIfExists(oldImageFile);
            }

            // Lưu ảnh mới
            Path imageFile = uploadImagePath.resolve(imageName);
            try (OutputStream osImage = Files.newOutputStream(imageFile)) {
                osImage.write(questionImage.getBytes());
            }
        }

        if (questionAudio != null && !questionAudio.isEmpty()) {
            // Nếu có audio mới được upload, xử lý audio và cập nhật tên mới
            audioName = questionAudio.getOriginalFilename();

            if (!Files.exists(uploadAudioPath)) {
                Files.createDirectories(uploadAudioPath);
            }

            // Xóa audio cũ nếu có
            String oldAudio = existingExamQuestion.getQuestionAudio();
            if (oldAudio != null && !oldAudio.isEmpty()) {
                Path oldAudioFile = uploadAudioPath.resolve(oldAudio);
                Files.deleteIfExists(oldAudioFile);
            }

            // Lưu audio mới
            Path audioFile = uploadAudioPath.resolve(audioName);
            try (OutputStream osAudio = Files.newOutputStream(audioFile)) {
                osAudio.write(questionAudio.getBytes());
            }
        }

        existingExamQuestion.setQuestionImage(imageName);
        existingExamQuestion.setQuestionAudio(audioName);

        existingExamQuestion.setExam(examOptional.get());
        existingExamQuestion.setQuestionPart(examQuestionRequest.getQuestionPart());

        return examQuestionRepository.save(existingExamQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamQuestion getExamQuestionById(Integer examQuestionId) {
        return examQuestionRepository.findById(examQuestionId).orElse(null);
    }

    @Override
    public boolean deleteExamQuestion(Integer examQuestionId) {
        Optional<ExamQuestion> examQuestionOptional = examQuestionRepository.findById(examQuestionId);
        if(examQuestionOptional.isEmpty()) {
            return false;
        }

        examQuestionRepository.deleteById(examQuestionId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamQuestion> getAllExamQuestions() {
        return examQuestionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamQuestion> getExamQuestionsByExamId(Integer examId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if (examOptional.isPresent()) {
            Exam exam = examOptional.get();
            return examQuestionRepository.findByExam(exam);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean deleteAllExamQuestionsByExamId(Integer examId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if(examOptional.isEmpty()) {
            return false;
        }

        // Lấy danh sách câu hỏi dựa vào examId
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExam(examOptional.get());

        // Xóa tất cả câu hỏi trong danh sách
        examQuestionRepository.deleteAll(examQuestions);
        return true;
    }

}
