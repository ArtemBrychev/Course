package com.example.tracker.repository;

import com.example.tracker.model.Board;
import com.example.tracker.model.BoardMember;
import com.example.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository
        extends JpaRepository<BoardMember, Long> {

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    void deleteByBoardIdAndUserId(Long boardId, Long userId);
    List<BoardMember> findAllByUser(User user);
    List<BoardMember> findAllByBoard(Board board);
    List<BoardMember> findAllByBoardId(Long boardId);
}
