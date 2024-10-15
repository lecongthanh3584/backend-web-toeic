package com.backend.spring.repositories;
import com.backend.spring.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

}