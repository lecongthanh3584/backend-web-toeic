package com.backend.spring.services.Vocabulary;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Topic;
import com.backend.spring.entities.Vocabulary;
import com.backend.spring.payload.request.VocabularyRequest;
import com.backend.spring.repositories.TopicRepository;
import com.backend.spring.repositories.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class VocabularyService implements IVocabularyService {

    private final VocabularyRepository vocabularyRepository;

    private final TopicRepository topicRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Vocabulary> getAllVocabularies() {
        return vocabularyRepository.findAll();
    }

    @Override
    public boolean uploadVocabularyFromExcel(MultipartFile file, Integer topicId) throws IOException {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if(topicOptional.isEmpty()) {
            return false;
        }

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<Vocabulary> vocabularies = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                // Bỏ qua dòng tiêu đề (nếu có)
                continue;
            }

            Vocabulary vocabulary = new Vocabulary();

            vocabulary.setWord(row.getCell(0).getStringCellValue());
            vocabulary.setIpa(row.getCell(1).getStringCellValue());
            vocabulary.setMeaning(row.getCell(2).getStringCellValue());
            vocabulary.setExampleSentence(row.getCell(3).getStringCellValue());

            vocabulary.setTopic(topicOptional.get());
            vocabulary.setVocabularyStatus(EStatus.ENABLE.getValue());

            vocabularies.add(vocabulary);
        }

        vocabularyRepository.saveAll(vocabularies);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Vocabulary getVocabularyById(Integer id) {
        return vocabularyRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteVocabulary(Integer vocabularyId) throws IOException {
        Optional<Vocabulary> vocabularyOptional = vocabularyRepository.findById(vocabularyId);
        if(vocabularyOptional.isEmpty()) {
            return false;
        }

        String oldImage = vocabularyOptional.get().getImage();
        String imagePath = "images/vocabulary/";

        if (oldImage != null && !oldImage.isEmpty()) {
            Path oldImageFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath, oldImage);
            Files.deleteIfExists(oldImageFile);
        }

        vocabularyRepository.deleteById(vocabularyId);
        return true;
    }

    @Override
    public Vocabulary createVocabulary(VocabularyRequest vocabularyRequest) throws IOException {
        Optional<Topic> topicOptional = topicRepository.findById(vocabularyRequest.getTopicId());
        if(topicOptional.isEmpty()) {
            return null;
        }

        MultipartFile image = vocabularyRequest.getImage();

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn 1 ảnh để upload.");
        }

        String imageName = image.getOriginalFilename();
        String imagePath = "images/vocabulary/";  // Thay đổi đường dẫn hình ảnh tương ứng
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path file = uploadPath.resolve(imageName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }

        Vocabulary vocabulary = new Vocabulary();

        vocabulary.setTopic(topicOptional.get());
        vocabulary.setWord(vocabularyRequest.getWord());
        vocabulary.setIpa(vocabularyRequest.getIpa());
        vocabulary.setMeaning(vocabularyRequest.getMeaning());
        vocabulary.setExampleSentence(vocabularyRequest.getExampleSentence());
        vocabulary.setVocabularyStatus(EStatus.ENABLE.getValue());
        vocabulary.setImage(imageName);
        vocabulary.setCreatedAt(LocalDateTime.now());
        vocabulary.setUpdatedAt(LocalDateTime.now());

        return vocabularyRepository.save(vocabulary);
    }

    @Override
    public Vocabulary updateVocabulary(VocabularyRequest vocabularyRequest) throws IOException {
        Optional<Vocabulary> vocabularyOptional = vocabularyRepository.findById(vocabularyRequest.getVocabularyId());
        Optional<Topic> topicOptional = topicRepository.findById(vocabularyRequest.getTopicId());

        if (vocabularyOptional.isPresent() && topicOptional.isPresent()) {
            Vocabulary existingVocabulary = vocabularyOptional.get();

            existingVocabulary.setWord(vocabularyRequest.getWord());
            existingVocabulary.setIpa(vocabularyRequest.getIpa());
            existingVocabulary.setMeaning(vocabularyRequest.getMeaning());
            existingVocabulary.setExampleSentence(vocabularyRequest.getExampleSentence());
            existingVocabulary.setVocabularyStatus(vocabularyRequest.getVocabularyStatus());
            existingVocabulary.setTopic(topicOptional.get());
            existingVocabulary.setUpdatedAt(LocalDateTime.now());

            MultipartFile image = vocabularyRequest.getImage();

            if (image != null && !image.isEmpty()) {
                // Xóa ảnh cũ
                String oldImage = existingVocabulary.getImage();
                if (oldImage != null && !oldImage.isEmpty()) {
                    Path oldImageFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldImage);
                    Files.deleteIfExists(oldImageFile);
                }

                // Lưu ảnh mới
                String imageName = image.getOriginalFilename();
                String imagePath = "images/vocabulary/";  // Thay đổi đường dẫn hình ảnh tương ứng
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path file = uploadPath.resolve(imageName);
                try (OutputStream os = Files.newOutputStream(file)) {
                    os.write(image.getBytes());
                }

                existingVocabulary.setImage(imageName);
            }

            return vocabularyRepository.save(existingVocabulary);
        }

        return null;
    }

    @Override
    public Vocabulary updateVocabularyStatus(Integer vocabularyId, Integer newStatus) {
        Optional<Vocabulary> vocabularyOptional = vocabularyRepository.findById(vocabularyId);
        if(vocabularyOptional.isEmpty()) {
            return null;
        }

        Vocabulary vocabularyUpdate = vocabularyOptional.get();

        if(newStatus.equals(EStatus.DISABLE.getValue())) {
            vocabularyUpdate.setVocabularyStatus(newStatus);
        } else if(newStatus.equals(EStatus.ENABLE.getValue())) {
            vocabularyUpdate.setVocabularyStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }

        vocabularyUpdate.setUpdatedAt(LocalDateTime.now());

        return vocabularyRepository.save(vocabularyUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vocabulary> getVocabulariesByTopicId(Integer topicId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);

        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            return vocabularyRepository.findByTopic(topic);
        }

        return Collections.emptyList();
    }

}
