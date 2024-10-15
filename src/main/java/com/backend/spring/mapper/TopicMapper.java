package com.backend.spring.mapper;

import com.backend.spring.entities.Topic;
import com.backend.spring.payload.response.TopicResponse;

public class TopicMapper {

    public static TopicResponse mapFromEntityToResponse(Topic topic) {
        if(topic == null) {
            return null;
        }

        return new TopicResponse(
                topic.getTopicId(),
                topic.getTopicName(),
                topic.getImage(),
                topic.getTopicStatus(),
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }
}
