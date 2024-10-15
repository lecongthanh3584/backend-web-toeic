package com.backend.spring.repositories;
import com.backend.spring.entities.Comment;
import com.backend.spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByUser(User user);
}