package com.backend.spring.service.Question;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Question;
import com.backend.spring.entity.QuestionGroup;
import com.backend.spring.entity.Section;
import com.backend.spring.payload.request.QuestionRequest;
import com.backend.spring.repository.QuestionGroupRepository;
import com.backend.spring.repository.QuestionRepository;
import com.backend.spring.repository.SectionRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionService implements IQuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private QuestionGroupRepository questionGroupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Question getQuestionById(Integer questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    @Override
    public Question createQuestion(QuestionRequest questionRequest) throws IOException {
        Optional<Section> sectionOptional = sectionRepository.findById(questionRequest.getSectionId());

        if(sectionOptional.isEmpty()) {
            return null;
        }

        MultipartFile questionImage = questionRequest.getQuestionImage();
        MultipartFile questionAudio = questionRequest.getQuestionAudio();

        String imageName = null;
        String audioName = null;
        String imagePath = "images/";
        String audioPath = "audios/";
        Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);
        Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

        //Lưu ảnh nếu có
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

        //Lưu audio nếu có
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

        Question question = new Question();

        question.setQuestionContent(questionRequest.getQuestionContent());
        question.setOptionA(questionRequest.getOptionA());
        question.setOptionB(questionRequest.getOptionB());
        question.setOptionC(questionRequest.getOptionC());
        question.setOptionD(questionRequest.getOptionD());
        question.setCorrectOption(questionRequest.getCorrectOption());
        question.setQuestionType(questionRequest.getQuestionType());
        question.setQuestionImage(imageName); // Có thể là null
        question.setQuestionScript(questionRequest.getQuestionScript());
        question.setQuestionExplanation(questionRequest.getQuestionExplanation());
        question.setQuestionAudio(audioName); // Có thể là null
        question.setQuestionPassage(questionRequest.getQuestionPassage());
        question.setQuestionText(questionRequest.getQuestionText());
        question.setSuggestedAnswer(questionRequest.getSuggestedAnswer());
        question.setQuestionStatus(EStatus.ENABLE.getValue());
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        question.setSection(sectionOptional.get());

        // Lấy đối tượng QuestionGroup từ groupId (Nếu groupId không null)
        if(questionRequest.getGroupId() != null) {
            Optional<QuestionGroup> questionGroupOptional = questionGroupRepository.findById(questionRequest.getGroupId());
            if(questionGroupOptional.isEmpty()) {
                return null;
            }

            question.setQuestionGroup(questionGroupOptional.get());
        }

        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(QuestionRequest questionRequest) throws IOException {
        Optional<Question> questionOptional = questionRepository.findById(questionRequest.getQuestionId());
        Optional<Section> sectionOptional = sectionRepository.findById(questionRequest.getSectionId());

        if(questionOptional.isEmpty() || sectionOptional.isEmpty()) {
            return null;
        }

        Question existingQuestion = questionOptional.get();

        existingQuestion.setQuestionContent(questionRequest.getQuestionContent());
        existingQuestion.setOptionA(questionRequest.getOptionA());
        existingQuestion.setOptionB(questionRequest.getOptionB());
        existingQuestion.setOptionC(questionRequest.getOptionC());
        existingQuestion.setOptionD(questionRequest.getOptionD());
        existingQuestion.setCorrectOption(questionRequest.getCorrectOption());
        existingQuestion.setQuestionType(questionRequest.getQuestionType());
        existingQuestion.setQuestionScript(questionRequest.getQuestionScript());
        existingQuestion.setQuestionExplanation(questionRequest.getQuestionExplanation());
        existingQuestion.setQuestionPassage(questionRequest.getQuestionPassage());
        existingQuestion.setQuestionText(questionRequest.getQuestionText());
        existingQuestion.setSuggestedAnswer(questionRequest.getSuggestedAnswer());
        existingQuestion.setQuestionStatus(questionRequest.getQuestionStatus());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        existingQuestion.setSection(sectionOptional.get());

        MultipartFile questionImage = questionRequest.getQuestionImage();
        MultipartFile questionAudio = questionRequest.getQuestionAudio();

        String imageName = existingQuestion.getQuestionImage(); // Lấy tên ảnh hiện tại
        String audioName = existingQuestion.getQuestionAudio(); // Lấy tên audio hiện tại

        String imagePath = "images/";
        String audioPath = "audios/";

        Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);
        Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

        //Cập nhật ảnh
        if (questionImage != null && !questionImage.isEmpty()) {
            // Nếu có ảnh mới được upload, xử lý ảnh và cập nhật tên mới
            imageName = questionImage.getOriginalFilename();

            if (!Files.exists(uploadImagePath)) {
                Files.createDirectories(uploadImagePath);
            }

            // Xóa ảnh cũ nếu có
            String oldImage = existingQuestion.getQuestionImage();
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

        //Cập nhật audio
        if (questionAudio != null && !questionAudio.isEmpty()) {
            // Nếu có audio mới được upload, xử lý audio và cập nhật tên mới
            audioName = questionAudio.getOriginalFilename();

            if (!Files.exists(uploadAudioPath)) {
                Files.createDirectories(uploadAudioPath);
            }

            // Xóa audio cũ nếu có
            String oldAudio = existingQuestion.getQuestionAudio();
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

        existingQuestion.setQuestionImage(imageName);
        existingQuestion.setQuestionAudio(audioName);

        // Lấy đối tượng QuestionGroup từ groupId (Nếu groupId không null)
        if (questionRequest.getGroupId() != null) {
            Optional<QuestionGroup> questionGroupOptional = questionGroupRepository.findById(questionRequest.getGroupId());
            if (questionGroupOptional.isEmpty()) {
                return null;
            }

            existingQuestion.setQuestionGroup(questionGroupOptional.get());
        }

        return questionRepository.save(existingQuestion);
    }

    @Override
    public Question updateQuestionStatus(Integer id, Integer newStatus) {
        Optional<Question> questionOptional = questionRepository.findById(id);
        if(questionOptional.isEmpty()) {
            return null;
        }

        Question question = questionOptional.get();

       if(newStatus.equals(EStatus.ENABLE.getValue())) {
           question.setQuestionStatus(newStatus);
       } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
           question.setQuestionStatus(newStatus);
       } else {
           throw new IllegalArgumentException("Invalid status value!");
       }

        question.setUpdatedAt(LocalDateTime.now());

        return questionRepository.save(question);
    }

    @Override
    public boolean deleteQuestion(Integer questionId) throws IOException {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty()) {
            return false;
        }

        String imagePath = "images/";

        Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

        String oldImage = questionOptional.get().getQuestionImage();
        if (oldImage != null && !oldImage.isEmpty()) {
            Path oldImageFile = uploadImagePath.resolve(oldImage);
            Files.deleteIfExists(oldImageFile);
        }

        questionRepository.deleteById(questionId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> getQuestionsBySectionId(Integer sectionId) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            return questionRepository.findBySection(section);
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> getQuestionsByGroupId(Integer groupId) {
        Optional<QuestionGroup> questionGroupOptional = questionGroupRepository.findById(groupId);
        if (questionGroupOptional.isPresent()) {
            QuestionGroup questionGroup = questionGroupOptional.get();
            return questionRepository.findByQuestionGroup(questionGroup);
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> getQuestionsBySectionIdAndType(Integer sectionId, String questionType) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if(sectionOptional.isEmpty()) {
            return Collections.emptyList();
        }

        // Thực hiện truy vấn cơ sở dữ liệu để lấy danh sách câu hỏi dựa trên sectionId và questionType
        return questionRepository.findBySectionAndQuestionType(sectionOptional.get(), questionType);
    }


}
