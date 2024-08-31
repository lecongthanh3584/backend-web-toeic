package com.backend.spring.service.Comment;

import com.backend.spring.entity.Comment;
import com.backend.spring.entity.User;
import com.backend.spring.payload.request.CommentRequest;
import com.backend.spring.repository.CommentRepository;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllRootComments() {
        List<Comment> allComments = commentRepository.findAll();
        List<Comment> rootComments = new ArrayList<>();
        for (Comment comment : allComments) {
            if (comment.getParentComment() == null) {
                rootComments.add(comment);
            }
        }
        // Sắp xếp danh sách bình luận theo thời gian giảm dần (từ mới nhất đến cũ nhất)
        Collections.sort(rootComments, (c1, c2) -> c2.getDate().compareTo(c1.getDate()));

        return rootComments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getRootCommentsByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {
            return Collections.emptyList();
        }

        // Lấy tất cả các bình luận của người dùng
        List<Comment> userComments = commentRepository.findByUser(userOptional.get());
        // Lọc ra các bình luận gốc (không có bình luận cha)
        List<Comment> rootComments = userComments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .collect(Collectors.toList());
        // Sắp xếp danh sách bình luận theo thời gian giảm dần (từ mới nhất đến cũ nhất)
        Collections.sort(rootComments, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        return rootComments;
    }

    @Override
    public Comment createComment(CommentRequest commentRequest) {
        Optional<User> userOptional = userRepository.findById(commentRequest.getUserId());

        if(userOptional.isEmpty()) {
            return null;
        }

        Comment comment = new Comment();
        comment.setUser(userOptional.get());
        comment.setText(commentRequest.getText());
        comment.setDate(LocalDateTime.now());

        if (commentRequest.getParentId() != null) {
            Optional<Comment> parentCommentOptional = commentRepository.findById(commentRequest.getParentId());

            if (parentCommentOptional.isPresent()) {
                Comment parentComment = parentCommentOptional.get();
                comment.setParentComment(parentComment);
                parentComment.getReplies().add(comment); // Thêm phản hồi vào danh sách phản hồi của bình luận cha
                commentRepository.save(parentComment); // Cập nhật bình luận cha
                commentRepository.save(comment); //Thêm bình luận

                return comment; // Trả về bình luận
            }
        }

        return commentRepository.save(comment);
    }

}
