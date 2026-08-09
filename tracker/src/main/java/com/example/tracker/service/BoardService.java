package com.example.tracker.service;

import com.example.tracker.dto.BoardResponse;
import com.example.tracker.dto.CreateBoardRequest;
import com.example.tracker.dto.UserResponse;
import com.example.tracker.eventdriven.EventSender;
import com.example.tracker.eventdriven.EventType;
import com.example.tracker.eventdriven.events.BoardCreatedPayload;
import com.example.tracker.eventdriven.events.BoardDeletedPayload;
import com.example.tracker.exceptions.AccessDeniedException;
import com.example.tracker.exceptions.DataAlreadyExistsException;
import com.example.tracker.exceptions.DataNotFoundException;
import com.example.tracker.exceptions.InvalidDataException;
import com.example.tracker.model.Board;
import com.example.tracker.model.BoardMember;
import com.example.tracker.model.User;
import com.example.tracker.repository.BoardMemberRepository;
import com.example.tracker.repository.BoardRepository;
import com.example.tracker.repository.cache.BoardCacheRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCacheRepository boardCacheRepository;
    private final UserService userService;
    private final BoardMemberRepository boardMemberRepository;
    private final EventSender eventSender;

    public BoardResponse create(String email, CreateBoardRequest request) {

        User user = userService.getByEmail(email);

        Board board = Board.builder()
                .title(request.getTitle())
                .owner(user)
                .build();

        Board saved = boardRepository.save(board);

        BoardCreatedPayload payload = new BoardCreatedPayload(
                saved.getId(),
                saved.getTitle(),
                saved.getOwner().getId()
        );
        eventSender.eventType(EventType.BOARD_CREATED)
                .payload(payload)
                .send();

        return BoardResponse.from(saved);
    }

    public List<BoardResponse> getAllByOwner(String email) {

        User user = userService.getByEmail(email);

        return boardRepository.findAllByOwner(user)
                .stream()
                .map(BoardResponse::from)
                .toList();
    }

    public List<BoardResponse> getAllByUser(String email) {

        User user = userService.getByEmail(email);

        return boardMemberRepository.findAllByUser(user)
                .stream()
                .map(BoardMember::getBoard)
                .map(BoardResponse::from)
                .toList();
    }

    public BoardResponse getById(Long id, String email) {
        User user = userService.getByEmail(email);
        BoardResponse cached = boardCacheRepository.findById(id);
        if (cached != null) {
            if (
                    !cached.getOwnerId().equals(user.getId())
                            && !boardMemberRepository.existsByBoardIdAndUserId(
                            id,
                            user.getId()
                    )
            ) {
                throw new AccessDeniedException("Access denied");
            }

            log.info("[REDIS] CACHE HIT board:id:" + id);

            return cached;
        }

        log.info("[REDIS] CACHE MISS board:id:" + id);

        Board board = boardRepository.findById(id)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                "Board not found"
                        )
                );

        if (!hasAccess(board, user)) {
            throw new AccessDeniedException("Access denied");
        }

        BoardResponse response =
                BoardResponse.from(board);

        boardCacheRepository.save(response);

        return response;
    }

    public List<UserResponse> findBoardMembers(Long id, String email) {
        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Board not found")
                );

        if (!hasAccess(board, user)) {
            throw new AccessDeniedException("Access denied");
        }

        return boardMemberRepository.findAllByBoardId(id)
                .stream()
                .map(BoardMember::getUser)
                .filter(Objects::nonNull)
                .map(UserResponse::from)
                .toList();
    }

    public void delete(Long id, String email) {

        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Board not found"));

        if (!board.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        boardCacheRepository.delete(id);

        boardRepository.delete(board);

        BoardDeletedPayload payload = new BoardDeletedPayload(
                board.getId(),
                user.getId()
        );
        eventSender.eventType(EventType.BOARD_DELETED)
                .payload(payload)
                .send();
    }

    @Transactional
    public void addMember(Long boardId, String ownerEmail, String memberEmail) {
        User owner = userService.getByEmail(ownerEmail);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new DataNotFoundException("Board not found")
                );

        if (!board.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Only board owner can add users");
        }

        User member = userService.getByEmail(memberEmail);
        if (boardMemberRepository.existsByBoardIdAndUserId(boardId, member.getId())) {
            throw new DataAlreadyExistsException("User already added to board");
        }

        BoardMember boardMember = new BoardMember();

        boardMember.setBoard(board);
        boardMember.setUser(member);

        boardMemberRepository.save(boardMember);
    }

    @Transactional
    public void removeMember(Long boardId, String ownerEmail, String memberEmail) {
        User owner = userService.getByEmail(ownerEmail);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new DataNotFoundException("Board not found")
                );

        if (!board.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Only board owner can remove users");
        }

        User member = userService.getByEmail(memberEmail);
        if (!boardMemberRepository.existsByBoardIdAndUserId(boardId, member.getId())) {
            throw new DataNotFoundException("User is not a board member");
        }

        boardMemberRepository.deleteByBoardIdAndUserId(boardId, member.getId());
    }

    public boolean isOwner(Board board, User user) {
        return board.getOwner()
                .getId()
                .equals(user.getId());
    }

    public boolean isMember(Board board, User user) {
        return boardMemberRepository.existsByBoardIdAndUserId(
                board.getId(),
                user.getId()
        );
    }

    public boolean hasAccess(Board board, User user) {
        return isOwner(board, user)
                || isMember(board, user);
    }

    public BoardResponse rename(String ownerEmail, CreateBoardRequest request, Long boardId) {
        User owner = userService.getByEmail(ownerEmail);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new DataNotFoundException("Board not found")
                );

        if (!board.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Only board owner can rename the board");
        }

        if(request.getTitle()==null || request.getTitle().isEmpty()){
            throw new InvalidDataException("Board Title should not be empty");
        }

        board.setTitle(request.getTitle());
        Board saved = boardRepository.save(board);

        return BoardResponse.from(saved);
    }
}