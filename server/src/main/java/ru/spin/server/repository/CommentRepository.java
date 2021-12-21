package ru.spin.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spin.server.entity.Brickset;
import ru.spin.server.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBricksetOrderByCreateDate(Brickset brickset);

    Comment findCommentByIdAndUserId(Long id, Long userId);
}
