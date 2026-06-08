package com.example.tracker.service;

import com.example.tracker.dto.BoardResponse;
import com.example.tracker.dto.CreateBoardRequest;
import com.example.tracker.model.Board;
import com.example.tracker.model.User;
import com.example.tracker.repository.BoardRepository;
import com.example.tracker.repository.cache.BoardCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCacheRepository boardCacheRepository;
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

        BoardResponse cached =
                boardCacheRepository.findById(id);

        if (cached != null) {

            if (!cached.getOwnerId().equals(user.getId())) {
                throw new RuntimeException("Access denied");
            }

            System.out.println(
                    "[REDIS] CACHE HIT board:id:" + id
            );

            return cached;
        }

        System.out.println(
                "[REDIS] CACHE MISS board:id:" + id
        );

        Board board = boardRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Board not found"
                        )
                );

        if (!board.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        BoardResponse response =
                BoardResponse.from(board);

        boardCacheRepository.save(response);

        return response;
    }

    public void delete(Long id, String email) {

        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (!board.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        boardCacheRepository.delete(id);

        boardRepository.delete(board);
    }
}