package com.backend.spring.services.Topic;

import com.backend.spring.entities.Topic;
import com.backend.spring.payload.request.TopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ITopicService {
    Page<Topic> getAllTopics(Integer pageNumber, String keyword, String... sortBys);
    Page<Topic> getAllTopicEnable(Integer pageNumber, String keyword);
    Topic getTopicById(Integer id);
    void uploadTopicFromExcel(MultipartFile file) throws IOException;
    Topic createTopic(TopicRequest topicRequest) throws IOException;
    Topic updateTopic(TopicRequest topicRequest) throws IOException;
    Topic updateTopicStatus(Integer topicId, Integer newStatus);
    boolean deleteTopic(Integer topicId) throws IOException;
    boolean isTopicNameExists(String topicName);
    boolean isTopicNameExistsAndIdNot(String topicName, Integer id);

}
