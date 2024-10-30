package com.backend.spring.services.Topic;
import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Topic;
import com.backend.spring.repositories.TopicRepository;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.backend.spring.payload.request.TopicRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TopicService implements ITopicService {

    private final TopicRepository topicRepository;

    @Override
    public void uploadTopicFromExcel(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<Topic> topics = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                // Bỏ qua dòng tiêu đề (nếu có)
                continue;
            }

            Topic topic = new Topic();
            topic.setTopicName(row.getCell(0).getStringCellValue()); // Chỉnh index cột tương ứng trong file Excel
            topic.setTopicStatus(EStatus.ENABLE.getValue());
            topic.setCreatedAt(LocalDateTime.now());
            topic.setUpdatedAt(LocalDateTime.now());

            topics.add(topic);
        }

        topicRepository.saveAll(topics);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Topic> getAllTopics(Integer pageNumber, String keyword, String... sortBys) {

        List<Sort.Order> orders = getListSort(sortBys);

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(orders));

        return topicRepository.getAllTopicWithoutStatus(keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Topic> getAllTopicEnable(Integer pageNumber, String keyword) {

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "created_at"));

        return topicRepository.getAllTopicHaveStatus(EStatus.ACTIVATE.getValue(), keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getTopicById(Integer id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public Topic createTopic(TopicRequest topicRequest) throws IOException {
        MultipartFile image = topicRequest.getImage();

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn một ảnh.");
        }

        String imageName = image.getOriginalFilename();
        String imagePath = "images/";
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path file = uploadPath.resolve(imageName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }

        Topic topic = new Topic();

        topic.setTopicName(topicRequest.getTopicName());
        topic.setImage(imageName);
        topic.setTopicStatus(EStatus.ENABLE.getValue());
        topic.setCreatedAt(LocalDateTime.now());
        topic.setUpdatedAt(LocalDateTime.now());

        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(TopicRequest topicRequest) throws IOException {
        Optional<Topic> topicOptional = topicRepository.findById(topicRequest.getTopicId());

        if(topicOptional.isEmpty()) {
            return null;
        }

        Topic existingTopic = topicOptional.get();

        existingTopic.setTopicName(topicRequest.getTopicName());
        existingTopic.setTopicStatus(topicRequest.getTopicStatus());
        existingTopic.setUpdatedAt(LocalDateTime.now());

        MultipartFile image = topicRequest.getImage();

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            String imagePath = "images/";
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Xóa ảnh cũ
            String oldImage = existingTopic.getImage();
            if (oldImage != null && !oldImage.isEmpty()) {
                Path oldImageFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldImage);
                Files.deleteIfExists(oldImageFile);
            }

            // Lưu ảnh mới
            Path file = uploadPath.resolve(imageName);
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }

            existingTopic.setImage(imageName);
        }

        return topicRepository.save(existingTopic);
    }

    @Override
    public Topic updateTopicStatus(Integer topicId, Integer newStatus) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if(topicOptional.isEmpty()) {
            return null;
        }

        Topic topicUpdate = topicOptional.get();
        if(newStatus.equals(EStatus.ENABLE.getValue())) {
            topicUpdate.setTopicStatus(newStatus);
        } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
            topicUpdate.setTopicStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }

        topicUpdate.setUpdatedAt(LocalDateTime.now());

        return topicRepository.save(topicUpdate);
    }

    @Override
    public boolean deleteTopic(Integer topicId) throws IOException {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if(topicOptional.isEmpty()) {
            return false;
        }

        String imagePath = "images/";
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

        // Xóa ảnh
        String oldImage = topicOptional.get().getImage();
        if (oldImage != null && !oldImage.isEmpty()) {
            Path oldImageFile = uploadPath.resolve(oldImage);
            Files.deleteIfExists(oldImageFile);
        }

        topicRepository.deleteById(topicId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTopicNameExists(String topicName) {
        return topicRepository.existsByTopicName(topicName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTopicNameExistsAndIdNot(String topicName, Integer id) {
        return topicRepository.existsByTopicNameAndTopicIdNot(topicName, id);
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
