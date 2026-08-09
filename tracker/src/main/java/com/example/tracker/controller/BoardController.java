package com.example.tracker.controller;

import com.example.tracker.dto.AddBoardMemberRequest;
import com.example.tracker.dto.BoardResponse;
import com.example.tracker.dto.CreateBoardRequest;
import com.example.tracker.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/{id}/changeName")
    public ResponseEntity<BoardResponse> rename(
            @PathVariable Long id,
            @RequestBody CreateBoardRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(
                boardService.rename(principal.getName(), request, id)
        );
    }

    @GetMapping("allOwner")
    public ResponseEntity<List<BoardResponse>> getAllOwner(
            Principal principal
    ) {
        return ResponseEntity.ok(
                boardService.getAllByOwner(principal.getName())
        );
    }

    @GetMapping("allUser")
    public ResponseEntity<List<BoardResponse>> getAllUser(
            Principal principal
    ) {
        return ResponseEntity.ok(
                boardService.getAllByUser(principal.getName())
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

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getAllBoardMembers(
            @PathVariable Long id,
            Principal principal
    ){
        return ResponseEntity.ok(boardService.findBoardMembers(id, principal.getName()));
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

    @PostMapping("/{boardId}/members")
    public ResponseEntity<?> addMember(
            @PathVariable Long boardId,
            @RequestBody AddBoardMemberRequest request,
            Principal principal
    ) {
        boardService.addMember(boardId, principal.getName(), request.getEmail());
        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "User added to board"
                )
        );
    }

    @DeleteMapping("/{boardId}/members")
    public ResponseEntity<?> removeMember(
            @PathVariable Long boardId,
            @RequestBody AddBoardMemberRequest request,
            Principal principal
    ) {

        boardService.removeMember(
                boardId,
                principal.getName(),
                request.getEmail()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "User removed from board"
                )
        );
    }
}