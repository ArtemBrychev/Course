package com.example.analytics.repository;

import com.example.analytics.eventdriven.EventType;
import com.example.analytics.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByTaskIdOrderByOccurredAt(Long taskId);

    List<EventEntity> findTop100ByOrderByOccurredAtDesc();

    long countByEventType(EventType eventType);

    long countByUserIdAndEventType(Long userId, EventType eventType);

    List<EventEntity> findByUserId(Long userId);

    List<EventEntity> findByUserIdAndBoardId(Long userId, Long boardId);

    List<EventEntity> findByUserIdAndTaskId(Long userId, Long taskId);
}
