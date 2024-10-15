package com.backend.spring.services.Section;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Section;
import com.backend.spring.repositories.SectionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.backend.spring.payload.request.SectionRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SectionService implements ISectionService {

    private final SectionRepository sectionRepository;

    @Override
    public Section createSection(SectionRequest sectionRequest) throws IOException {
        MultipartFile image = sectionRequest.getImage();

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn một ảnh để upload!.");
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

        Section section = new Section();

        section.setName(sectionRequest.getName());
        section.setDescription(sectionRequest.getDescription()); // Thêm trường description
        section.setStatus(EStatus.ENABLE.getValue());
        section.setType(sectionRequest.getType());
        section.setImage(imageName);
        section.setCreatedAt(LocalDateTime.now());
        section.setUpdatedAt(LocalDateTime.now());

        return sectionRepository.save(section);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Section getSectionById(Integer id) {
        return sectionRepository.findById(id).orElse(null);
    }

    @Override
    public Section updateSection(SectionRequest sectionRequest) throws IOException {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionRequest.getSectionId());

        if(sectionOptional.isEmpty()) {
            return null;
        }

        Section existingSection = sectionOptional.get();

        existingSection.setName(sectionRequest.getName());
        existingSection.setDescription(sectionRequest.getDescription()); // Thêm trường description
        existingSection.setStatus(sectionRequest.getStatus());
        existingSection.setType(sectionRequest.getType());
        existingSection.setUpdatedAt(LocalDateTime.now());

        MultipartFile image = sectionRequest.getImage();

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            String imagePath = "images/";
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Xóa ảnh cũ
            String oldImage = existingSection.getImage();
            if (oldImage != null && !oldImage.isEmpty()) {
                Path oldImageFile = uploadPath.resolve(oldImage);
                Files.deleteIfExists(oldImageFile);
            }

            // Lưu ảnh mới
            Path file = uploadPath.resolve(imageName);
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }

            existingSection.setImage(imageName);
        }

        return sectionRepository.save(existingSection);
    }

    @Override
    public Section updateSectionStatus(Integer id, Integer newStatus) {
        Optional<Section> sectionOptional = sectionRepository.findById(id);
        if(sectionOptional.isEmpty()) {
            return null;
        }

        Section updateSection = sectionOptional.get();

        if(newStatus.equals(EStatus.ENABLE.getValue())) {
            updateSection.setStatus(newStatus);
        } else if (newStatus.equals(EStatus.DISABLE.getValue())) {
            updateSection.setStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value!");
        }

        updateSection.setUpdatedAt(LocalDateTime.now());

        return sectionRepository.save(updateSection);
    }

    @Override
    public boolean deleteSection(Integer id) throws IOException {
        Optional<Section> sectionOptional = sectionRepository.findById(id);
        if(sectionOptional.isEmpty()) {
            return false;
        }

        String imagePath = "images/";
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

        // Xóa ảnh
        String oldImage = sectionOptional.get().getImage();
        if (oldImage != null && !oldImage.isEmpty()) {
            Path oldImageFile = uploadPath.resolve(oldImage);
            Files.deleteIfExists(oldImageFile);
        }

        sectionRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public String getSectionNameById(Integer sectionId) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if(sectionOptional.isEmpty()) {
            return null;
        }

        return sectionOptional.get().getName();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSectionNameExists(String name) {
        return sectionRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSectionNameExistsAndIdNot(String name, Integer sectionId) {
        return sectionRepository.existsByNameAndSectionIdNot(name, sectionId);
    }

}
