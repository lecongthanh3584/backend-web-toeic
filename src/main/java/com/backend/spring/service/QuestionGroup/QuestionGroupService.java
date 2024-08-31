// QuestionGroupService.java
package com.backend.spring.service.QuestionGroup;

import com.backend.spring.entity.QuestionGroup;
import com.backend.spring.repository.QuestionGroupRepository;
import com.backend.spring.payload.request.QuestionGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionGroupService implements IQuestionGroupService {

    @Autowired
    private QuestionGroupRepository questionGroupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionGroup> getAllQuestionGroups() {
        return questionGroupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionGroup getQuestionGroupById(Integer groupId) {
        return questionGroupRepository.findById(groupId).orElse(null);

    }

    @Override
    public QuestionGroup createQuestionGroup(QuestionGroupRequest questionGroupRequest) throws IOException {
        MultipartFile image = questionGroupRequest.getGroupImage();
        MultipartFile audio = questionGroupRequest.getGroupAudio();

        String imageName = null;
        String audioName = null;

        //Lưu ảnh
        if (image != null && !image.isEmpty()) {
            imageName = image.getOriginalFilename();
            String imagePath = "images/";
            Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

            if (!Files.exists(uploadImagePath)) {
                Files.createDirectories(uploadImagePath);
            }

            Path imageFile = uploadImagePath.resolve(imageName);
            try (OutputStream osImage = Files.newOutputStream(imageFile)) {
                osImage.write(image.getBytes());
            }
        }

        //Lưu audio
        if (audio != null && !audio.isEmpty()) {
            audioName = audio.getOriginalFilename();
            String audioPath = "audios/";
            Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

            if (!Files.exists(uploadAudioPath)) {
                Files.createDirectories(uploadAudioPath);
            }

            Path audioFile = uploadAudioPath.resolve(audioName);
            try (OutputStream osAudio = Files.newOutputStream(audioFile)) {
                osAudio.write(audio.getBytes());
            }
        }

        QuestionGroup questionGroup = new QuestionGroup();
        questionGroup.setGroupImage(imageName); // Có thể là null
        questionGroup.setGroupScript(questionGroupRequest.getGroupScript());
        questionGroup.setGroupAudio(audioName); // Có thể là null
        questionGroup.setGroupPassage(questionGroupRequest.getGroupPassage());
        questionGroup.setGroupText(questionGroupRequest.getGroupText());

        return questionGroupRepository.save(questionGroup);
    }

    @Override
    public QuestionGroup updateQuestionGroup(QuestionGroupRequest questionGroupRequest) throws IOException {
        Optional<QuestionGroup> groupOptional = questionGroupRepository.findById(questionGroupRequest.getGroupId());

        if(groupOptional.isEmpty()) {
            return null;
        }

        QuestionGroup existingGroup = groupOptional.get();
        existingGroup.setGroupScript(questionGroupRequest.getGroupScript());
        existingGroup.setGroupPassage(questionGroupRequest.getGroupPassage());
        existingGroup.setGroupText(questionGroupRequest.getGroupText());

        MultipartFile image = questionGroupRequest.getGroupImage();
        MultipartFile audio = questionGroupRequest.getGroupAudio();

        //Cập nhật lại ảnh nếu có
        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            String imagePath = "images/";
            Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

            if (!Files.exists(uploadImagePath)) {
                Files.createDirectories(uploadImagePath);
            }

            // Xóa ảnh cũ nếu có
            String oldImage = existingGroup.getGroupImage();
            if (oldImage != null && !oldImage.isEmpty()) {
                Path oldImageFile = uploadImagePath.resolve(oldImage);
                Files.deleteIfExists(oldImageFile);
            }

            // Lưu ảnh mới
            Path file = uploadImagePath.resolve(imageName);
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }

            existingGroup.setGroupImage(imageName);
        }

        //Cập nhật lại audio nếu có
        if (audio != null && !audio.isEmpty()) {
            String audioName = audio.getOriginalFilename();
            String audioPath = "audios/";
            Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

            if (!Files.exists(uploadAudioPath)) {
                Files.createDirectories(uploadAudioPath);
            }

            // Xóa audio cũ nếu có
            String oldAudio = existingGroup.getGroupAudio();
            if (oldAudio != null && !oldAudio.isEmpty()) {
                Path oldAudioFile = uploadAudioPath.resolve(oldAudio);
                Files.deleteIfExists(oldAudioFile);
            }

            // Lưu audio mới
            Path file = uploadAudioPath.resolve(audioName);
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(audio.getBytes());
            }

            existingGroup.setGroupAudio(audioName);
        }

        return questionGroupRepository.save(existingGroup);
    }

    @Override
    public boolean deleteQuestionGroup(Integer groupId) throws IOException {
        Optional<QuestionGroup> questionGroupOptional = questionGroupRepository.findById(groupId);
        if(questionGroupOptional.isEmpty()) {
            return false;
        }

        String imagePath = "images/";
        Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

        String audioPath = "audios/";
        Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

        // Xóa audio nếu có
        String oldAudio = questionGroupOptional.get().getGroupAudio();
        if (oldAudio != null && !oldAudio.isEmpty()) {
            Path oldAudioFile = uploadAudioPath.resolve(oldAudio);
            Files.deleteIfExists(oldAudioFile);
        }

        // Xóa ảnh nếu có
        String oldImage = questionGroupOptional.get().getGroupImage();
        if (oldImage != null && !oldImage.isEmpty()) {
            Path oldImageFile = uploadImagePath.resolve(oldImage);
            Files.deleteIfExists(oldImageFile);
        }

        questionGroupRepository.deleteById(groupId);
        return true;
    }

}
