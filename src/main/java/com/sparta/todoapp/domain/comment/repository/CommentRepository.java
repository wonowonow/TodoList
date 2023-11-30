package com.sparta.todoapp.domain.comment.repository;

import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);
}
