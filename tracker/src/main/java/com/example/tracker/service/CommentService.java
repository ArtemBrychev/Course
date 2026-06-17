package com.example.tracker.service;

import com.example.tracker.dto.CommentResponse;
import com.example.tracker.dto.CreateCommentRequest;
import com.example.tracker.eventdriven.EventSender;
import com.example.tracker.eventdriven.EventType;
import com.example.tracker.eventdriven.events.CommentCreatedPayload;
import com.example.tracker.eventdriven.events.CommentDeletedPayload;
import com.example.tracker.exceptions.AccessDeniedException;
import com.example.tracker.exceptions.DataNotFoundException;
import com.example.tracker.model.Comment;
import com.example.tracker.model.Task;
import com.example.tracker.model.User;
import com.example.tracker.repository.CommentRepository;
import com.example.tracker.repository.TaskRepository;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final BoardService boardService;

    private final EventSender eventSender;

    public CommentResponse create(
            Long taskId,
            String email,
            CreateCommentRequest request
    ) {

        User user = userService.getByEmail(email);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new DataNotFoundException("Task not found")
                );

        if (!boardService.hasAccess(task.getBoard(), user)) {
            throw new AccessDeniedException("Access denied");
        }

        Comment comment = Comment.builder()
                .text(request.getText())
                .task(task)
                .author(user)
                .build();

        Comment saved = commentRepository.save(comment);

        CommentCreatedPayload payload = new CommentCreatedPayload(
                saved.getId(),
                saved.getTask().getId(),
                saved.getTask().getBoard().getId(),
                saved.getAuthor().getId(),
                saved.getText()
        );
        eventSender.eventType(EventType.COMMENT_CREATED)
                .payload(payload)
                .send();

        return CommentResponse.from(saved);
    }

    public List<CommentResponse> getAllByTask(
            Long taskId,
            String email
    ) {

        User user = userService.getByEmail(email);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new DataNotFoundException("Task not found")
                );

        if (!boardService.hasAccess(task.getBoard(), user)) {
            throw new AccessDeniedException("Access denied");
        }

        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    public void delete(Long id, String email) {
        User user = userService.getByEmail(email);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Comment not found")
                );

        boolean isBoardOwner = comment.getTask()
                        .getBoard()
                        .getOwner()
                        .getId()
                        .equals(user.getId());

        boolean isAuthor = comment.getAuthor()
                        .getId()
                        .equals(user.getId());

        if (!isBoardOwner && !isAuthor) {
            throw new AccessDeniedException("Access denied");
        }

        commentRepository.delete(comment);

        CommentDeletedPayload payload = new CommentDeletedPayload(
                comment.getId(),
                comment.getTask().getId(),
                comment.getTask().getBoard().getId(),
                user.getId()
        );
        eventSender.eventType(EventType.COMMENT_DELETED)
                .payload(payload)
                .send();
    }
}