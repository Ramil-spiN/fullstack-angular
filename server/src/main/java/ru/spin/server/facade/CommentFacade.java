package ru.spin.server.facade;

import org.springframework.stereotype.Component;
import ru.spin.server.dto.CommentDto;
import ru.spin.server.entity.Comment;

@Component
public class CommentFacade {
    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUsername(comment.getUsername());
        commentDto.setMessage(comment.getMessage());
        return commentDto;
    }
}
