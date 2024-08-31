package com.backend.spring.service.Topic;

import com.backend.spring.entity.Topic;
import com.backend.spring.payload.request.TopicRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ITopicService {
    List<Topic> getAllTopics();
    List<Topic> getAllTopicEnable();
    Topic getTopicById(Integer id);
    void uploadTopicFromExcel(MultipartFile file) throws IOException;
    Topic createTopic(TopicRequest topicRequest) throws IOException;
    Topic updateTopic(TopicRequest topicRequest) throws IOException;
    Topic updateTopicStatus(Integer topicId, Integer newStatus);
    boolean deleteTopic(Integer topicId) throws IOException;
    boolean isTopicNameExists(String topicName);
}
