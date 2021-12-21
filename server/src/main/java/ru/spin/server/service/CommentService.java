package ru.spin.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.spin.server.dto.CommentDto;
import ru.spin.server.entity.Brickset;
import ru.spin.server.entity.Comment;
import ru.spin.server.entity.User;
import ru.spin.server.exception.BricksetNotFoundException;
import ru.spin.server.repository.BricksetRepository;
import ru.spin.server.repository.CommentRepository;
import ru.spin.server.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private BricksetRepository bricksetRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, BricksetRepository bricksetRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.bricksetRepository = bricksetRepository;
    }

    public Comment createComment(Long bricksetId, CommentDto commentDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Brickset brickset = bricksetRepository.findById(bricksetId)
                .orElseThrow(() -> new BricksetNotFoundException("Brickset for " + user.getUsername() + " not found"));

        Comment comment = new Comment();
        comment.setBrickset(brickset);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDto.getMessage());

        LOGGER.info("Saving comment for brickset {} of user {}", brickset.getTitle(), user.getUsername());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForBrickset(Long bricksetId) {
        Brickset brickset = bricksetRepository.findById(bricksetId)
                .orElseThrow(() -> new BricksetNotFoundException("Brickset not found"));
        return commentRepository.findAllByBricksetOrderByCreateDate(brickset);
    }

    public void deleteComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
