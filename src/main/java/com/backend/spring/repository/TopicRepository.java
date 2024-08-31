package com.backend.spring.repository;
import com.backend.spring.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    boolean existsByTopicName(String topicName);
    List<Topic> findAllByTopicStatus(Integer status);
}