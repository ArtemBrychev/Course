package com.example.tracker.service;

import com.example.tracker.dto.BoardResponse;
import com.example.tracker.dto.CreateBoardRequest;
import com.example.tracker.model.Board;
import com.example.tracker.model.User;
import com.example.tracker.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    public BoardResponse create(String email, CreateBoardRequest request) {

        User user = userService.getByEmail(email);

        Board board = Board.builder()
                .title(request.getTitle())
                .owner(user)
                .build();

        Board saved = boardRepository.save(board);

        return BoardResponse.from(saved);
    }

    public List<BoardResponse> getAll(String email) {

        User user = userService.getByEmail(email);

        return boardRepository.findAllByOwner(user)
                .stream()
                .map(BoardResponse::from)
                .toList();
    }

    public BoardResponse getById(Long id, String email) {

        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (!board.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return BoardResponse.from(board);
    }

    public void delete(Long id, String email) {

        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (!board.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        boardRepository.delete(board);
    }
}