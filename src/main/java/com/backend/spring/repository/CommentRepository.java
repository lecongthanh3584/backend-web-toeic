package com.backend.spring.repository;
import com.backend.spring.entity.Comment;
import com.backend.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByUser(User user);
}