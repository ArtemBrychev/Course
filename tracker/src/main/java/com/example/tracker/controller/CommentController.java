package com.example.tracker.controller;

import com.example.tracker.dto.CommentResponse;
import com.example.tracker.dto.CreateCommentRequest;
import com.example.tracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create/{taskId}")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long taskId,
            @RequestBody CreateCommentRequest request,
            Principal principal
    ) {

        return ResponseEntity.ok(
                commentService.create(
                        taskId,
                        principal.getName(),
                        request
                )
        );
    }

    @PostMapping("/change/{commentId}")
    public ResponseEntity<CommentResponse> changeComment(
            @PathVariable Long commentId,
            @RequestBody CreateCommentRequest request,
            Principal principal
    ) {

        return ResponseEntity.ok(
                commentService.changeComment(
                        commentId,
                        principal.getName(),
                        request
                )
        );
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponse>> getAllByTask(
            @PathVariable Long taskId,
            Principal principal
    ) {

        return ResponseEntity.ok(
                commentService.getAllByTask(
                        taskId,
                        principal.getName()
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            Principal principal
    ) {

        commentService.delete(
                id,
                principal.getName()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Comment deleted"
                )
        );
    }
}