package ru.spin.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spin.server.dto.CommentDto;
import ru.spin.server.entity.Comment;
import ru.spin.server.facade.CommentFacade;
import ru.spin.server.payload.response.MessageResponse;
import ru.spin.server.service.CommentService;
import ru.spin.server.validation.ResponseErrorValidation;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {
    private CommentService commentService;
    private CommentFacade commentFacade;
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, CommentFacade commentFacade, ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("{bricksetId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsForBrickset(@PathVariable("bricksetId") String bricksetId) {
        List<CommentDto> commentDtoList = commentService
                .getAllCommentsForBrickset(Long.parseLong(bricksetId))
                .stream()
                .map(commentFacade::commentToCommentDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @PostMapping("{bricksetId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable("bricksetId") String bricksetId,
                                                Principal principal,
                                                BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.createComment(Long.parseLong(bricksetId), commentDto, principal);

        CommentDto commentCreated = commentFacade.commentToCommentDto(comment);
        return new ResponseEntity<>(commentCreated, HttpStatus.OK);
    }

    @PostMapping("{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }
}
