package com.example.tracker.repository;

import com.example.tracker.model.Board;
import com.example.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOwner(User owner);

    List<Board> findAllByOwnerId(Long ownerId);
}