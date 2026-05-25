package com.example.tracker.controller;

import com.example.tracker.dto.BoardResponse;
import com.example.tracker.dto.CreateBoardRequest;
import com.example.tracker.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("new")
    public ResponseEntity<BoardResponse> create(
            @RequestBody CreateBoardRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(
                boardService.create(principal.getName(), request)
        );
    }

    @GetMapping("all")
    public ResponseEntity<List<BoardResponse>> getAll(
            Principal principal
    ) {
        return ResponseEntity.ok(
                boardService.getAll(principal.getName())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getById(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(
                boardService.getById(id, principal.getName())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            Principal principal
    ) {
        boardService.delete(id, principal.getName());

        return ResponseEntity.ok(
                java.util.Map.of("message", "Board deleted")
        );
    }
}