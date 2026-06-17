package com.example.analytics.service;

import com.example.analytics.dto.*;
import com.example.analytics.eventdriven.EventType;
import com.example.analytics.model.TaskStateEntity;
import com.example.analytics.repository.EventRepository;
import com.example.analytics.repository.TaskStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final EventRepository eventRepository;
    private final TaskStateRepository taskStateRepository;

    public StatisticsResponse getGeneralStatistics() {

        long tasksCreated = eventRepository.countByEventType(EventType.TASK_CREATED);
        long tasksDeleted = eventRepository.countByEventType(EventType.TASK_DELETED);
        long tasksCompleted = eventRepository.countByEventType(EventType.TASK_STATUS_CHANGED);

        long commentsCreated = eventRepository.countByEventType(EventType.COMMENT_CREATED);
        long boardsCreated = eventRepository.countByEventType(EventType.BOARD_CREATED);

        long tasksInProgress = taskStateRepository.countByStatus("IN_PROGRESS");

        return new StatisticsResponse(
                tasksCreated,
                tasksDeleted,
                tasksCompleted,
                tasksInProgress,
                commentsCreated,
                boardsCreated
        );
    }

    public StatusStatisticsResponse getStatusStatistics() {

        long todo = taskStateRepository.countByStatus("TODO");
        long inProgress = taskStateRepository.countByStatus("IN_PROGRESS");
        long done = taskStateRepository.countByStatus("DONE");

        return new StatusStatisticsResponse(todo, inProgress, done);
    }

    public List<TopUserResponse> getTopUsers() {

        Map<Long, Long> map = new HashMap<>();

        eventRepository.findAll().forEach(e ->
                map.merge(e.getUserId(), 1L, Long::sum)
        );

        return map.entrySet().stream()
                .map(e -> new TopUserResponse(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.getActions(), a.getActions()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public UserStatisticsResponse getUserStatistics(Long userId) {

        List<EventType> events = eventRepository.findByUserId(userId)
                .stream()
                .map(e -> e.getEventType())
                .toList();

        long tasksCreated = count(events, EventType.TASK_CREATED);
        long commentsCreated = count(events, EventType.COMMENT_CREATED);
        long statusChanges = count(events, EventType.TASK_STATUS_CHANGED);
        long tasksCompleted = count(events, EventType.TASK_STATUS_CHANGED); // упрощение

        return new UserStatisticsResponse(
                tasksCreated,
                commentsCreated,
                statusChanges,
                tasksCompleted
        );
    }

    public UserBoardStatisticsResponse getUserBoardStatistics(Long userId, Long boardId) {

        var events = eventRepository.findByUserIdAndBoardId(userId, boardId);

        long tasksCreated = events.stream()
                .filter(e -> e.getEventType() == EventType.TASK_CREATED)
                .count();

        long commentsCreated = events.stream()
                .filter(e -> e.getEventType() == EventType.COMMENT_CREATED)
                .count();

        long statusChanges = events.stream()
                .filter(e -> e.getEventType() == EventType.TASK_STATUS_CHANGED)
                .count();

        return new UserBoardStatisticsResponse(
                userId,
                boardId,
                tasksCreated,
                commentsCreated,
                statusChanges
        );
    }

    public UserTaskStatisticsResponse getUserTaskStatistics(Long userId, Long taskId) {

        var actions = eventRepository.findByUserIdAndTaskId(userId, taskId)
                .stream()
                .map(e -> new UserTaskActionResponse(
                        e.getEventType(),
                        e.getOccurredAt()
                ))
                .toList();

        return new UserTaskStatisticsResponse(
                userId,
                taskId,
                actions
        );
    }

    private long count(List<EventType> events, EventType type) {
        return events.stream().filter(e -> e == type).count();
    }
}