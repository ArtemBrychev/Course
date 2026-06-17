package com.example.analytics.service;

import com.example.analytics.dto.*;
import com.example.analytics.eventdriven.EventType;
import com.example.analytics.exceptions.DataNotFoundException;
import com.example.analytics.repository.EventRepository;
import com.example.analytics.repository.StatusTransitionRepository;
import com.example.analytics.repository.TaskStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAnalyticsService {

    private final EventRepository eventRepository;
    private final StatusTransitionRepository statusTransitionRepository;
    private final TaskStateRepository taskStateRepository;

    public List<TaskHistoryResponse> getTaskHistory(Long taskId) {

        return eventRepository.findByTaskIdOrderByOccurredAt(taskId)
                .stream()
                .map(e -> new TaskHistoryResponse(
                        e.getEventType(),
                        e.getOccurredAt(),
                        e.getPayload()
                ))
                .toList();
    }

    public List<StatusHistoryResponse> getStatusHistory(Long taskId) {

        return statusTransitionRepository.findByTaskIdOrderByOccurredAt(taskId)
                .stream()
                .map(t -> new StatusHistoryResponse(
                        t.getOldStatus(),
                        t.getNewStatus(),
                        t.getOccurredAt()
                ))
                .toList();
    }

    public TaskStateResponse getTaskState(Long taskId) {
        var task = taskStateRepository.findById(taskId).orElse(null);
        if(task == null){
            log.info("Task is null");
            if(
                    eventRepository
                            .findByTaskIdOrderByOccurredAt(taskId)
                            .stream().anyMatch(event -> event.getEventType() == EventType.TASK_DELETED)
            )throw new DataNotFoundException("Задача удалена");

            log.info("Задача не найдена");
            throw new DataNotFoundException("Задача не найдена");
        }

        return new TaskStateResponse(
                task.getTaskId(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getCompletedAt()
        );
    }
}
