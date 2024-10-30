package com.backend.spring.repositories;
import com.backend.spring.entities.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query(value = "SELECT * FROM topic t " +
            "WHERE t.topic_name LIKE CONCAT('%', ?1, '%') AND t.deleted_at IS NULL", nativeQuery = true)
    Page<Topic> getAllTopicWithoutStatus(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM topic t " +
            "WHERE t.topic_name LIKE CONCAT('%', ?2, '%') AND t.topic_status = ?1 " +
            "AND t.deleted_at IS NULL", nativeQuery = true)
    Page<Topic> getAllTopicHaveStatus(Integer status, String keyword, Pageable pageable);

    boolean existsByTopicName(String topicName);
    boolean existsByTopicNameAndTopicIdNot(String topicName, Integer topicId);
    List<Topic> findAllByTopicStatus(Integer status);
}