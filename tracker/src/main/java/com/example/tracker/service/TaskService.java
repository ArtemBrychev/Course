package com.example.tracker.service;

import com.example.tracker.dto.ChangeTaskStatusRequest;
import com.example.tracker.dto.CreateTaskRequest;
import com.example.tracker.dto.TaskResponse;
import com.example.tracker.dto.UpdateTaskRequest;
import com.example.tracker.eventdriven.EventSender;
import com.example.tracker.eventdriven.EventType;
import com.example.tracker.eventdriven.events.*;
import com.example.tracker.exceptions.AccessDeniedException;
import com.example.tracker.exceptions.DataNotFoundException;
import com.example.tracker.model.Board;
import com.example.tracker.model.Task;
import com.example.tracker.model.User;
import com.example.tracker.observabilty.Metrics;
import com.example.tracker.repository.BoardRepository;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.repository.cache.TaskCacheRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* Add sending to:
* Assigning/Unassigning user
* That;s all I guess
*
* Example
* TaskCreatedPayload payload = new TaskCreatedPayload(
                saved.getId(),
                saved.getBoard().getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStatus().toString(),
                assigneeId,
                user.getId()
        );

        log.info("Creating tasks");
        meterRegistry.counter(Metrics.TASKS_CREATED).increment();
        log.info("Creating tasks after increment");

        eventSender.eventType(EventType.TASK_CREATED)
                .payload(payload)
                .send();
* */

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final TaskCacheRepository taskCacheRepository;

    private final BoardService boardService;

    private final EventSender eventSender;

    private final MeterRegistry meterRegistry;

    public TaskResponse create(String email, CreateTaskRequest request) {
        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() ->
                        new DataNotFoundException("Board not found")
                );

        if (!boardService.hasAccess(board, user)) {
            throw new AccessDeniedException("Access denied");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .board(board)
                .build();

        Task saved = taskRepository.save(task);

        long assigneeId = (saved.getAssignee()!=null)? saved.getAssignee().getId() : -1;
        TaskCreatedPayload payload = new TaskCreatedPayload(
                saved.getId(),
                saved.getBoard().getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStatus().toString(),
                assigneeId,
                user.getId()
        );

        log.info("Creating tasks");
        meterRegistry.counter(Metrics.TASKS_CREATED).increment();
        log.info("Creating tasks after increment");

        eventSender.eventType(EventType.TASK_CREATED)
                .payload(payload)
                .send();
        return TaskResponse.from(saved);
    }

    public TaskResponse getById(Long id, String email) {
        User user = userService.getByEmail(email);
        TaskResponse cached = taskCacheRepository.findById(id);
        if (cached != null) {
            Board board = boardRepository.findById(
                    cached.getBoardId()
            ).orElseThrow(
                    () -> new DataNotFoundException("Board not found")
            );

            if (!boardService.hasAccess(board, user)) {
                throw new AccessDeniedException("Access denied");
            }

            log.info("[REDIS] CACHE HIT task:id:" + id);

            return cached;
        }

        log.info("[REDIS] CACHE MISS task:id:" + id);

        Task task = taskRepository.findById(id)
                .orElseThrow(
                        () -> new DataNotFoundException("Task not found")
                );

        if (!boardService.hasAccess(task.getBoard(), user)) {
            throw new AccessDeniedException("Access denied");
        }

        TaskResponse response = TaskResponse.from(task);
        taskCacheRepository.save(response);
        return response;
    }

    public List<TaskResponse> getAllByBoard(Long boardId, String email) {
        User user = userService.getByEmail(email);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new DataNotFoundException("Board not found")
                );

        if (!boardService.hasAccess(board, user)) {
            throw new AccessDeniedException("Access denied");
        }

        return taskRepository.findAllByBoardId(boardId)
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    public TaskResponse update(Long id, String email, UpdateTaskRequest request) {
        User user = userService.getByEmail(email);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Task not found")
                );

        if (!boardService.hasAccess(task.getBoard(), user)) {
            throw new AccessDeniedException("Access denied");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        User assignee = null;
        //Назначение другого пользователя
        if (request.getAssigneeId() != null && request.getAssigneeId()!=0) {
            assignee = userRepository.findById(
                    request.getAssigneeId()
            ).orElseThrow(() ->
                    new DataNotFoundException("User not found")
            );
        }

        //Снятие с задачи
        if(task.getAssignee()!=null
                && (assignee==null
                || !assignee.getId().equals(task.getAssignee().getId()))){
            var payload = new TaskUnassignedPayload(
                    task.getId(),
                    task.getBoard().getId(),
                    task.getAssignee().getId(),
                    task.getBoard().getOwner().getId()
            );

            eventSender.eventType(EventType.TASK_UNASSIGNED).payload(payload).send();
        }

        //Назначение на задачу
        if(assignee!=null
                && (task.getAssignee()==null
                || !assignee.getId().equals(task.getAssignee().getId()))){

            var payload = new TaskAssignedPayload(
                    task.getId(),
                    task.getBoard().getId(),
                    assignee.getId(),
                    task.getBoard().getOwner().getId()
            );

            eventSender.eventType(EventType.TASK_ASSIGNED).payload(payload).send();
        }

        if(assignee!=null && !boardService.hasAccess(task.getBoard(), assignee)){
            throw new AccessDeniedException("User have no access to the board");
        }
        task.setAssignee(assignee);
        Task updated = taskRepository.save(task);
        taskCacheRepository.delete(id);

        TaskUpdatedPayload payload = new TaskUpdatedPayload(
                updated.getId(),
                updated.getBoard().getId(),
                updated.getTitle(),
                updated.getDescription(),
                user.getId()
        );
        eventSender.eventType(EventType.TASK_UPDATED)
                .payload(payload)
                .send();

        return TaskResponse.from(updated);
    }

    public TaskResponse changeStatus(
            Long id,
            String email,
            ChangeTaskStatusRequest request
    ) {

        User user = userService.getByEmail(email);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Task not found")
                );

        if (!boardService.hasAccess(task.getBoard(), user)) {
            throw new AccessDeniedException("Access denied");
        }

        String oldStatus = task.getStatus().toString();
        task.setStatus(request.getStatus());

        Task updated = taskRepository.save(task);

        TaskResponse response = TaskResponse.from(updated);

        taskCacheRepository.save(response);

        TaskStatusChangedPayload payload = new TaskStatusChangedPayload(
                updated.getId(),
                updated.getBoard().getId(),
                oldStatus,
                updated.getStatus().toString(),
                user.getId()
        );
        eventSender.eventType(EventType.TASK_STATUS_CHANGED)
                .payload(payload)
                .send();
        meterRegistry.counter(
                Metrics.TASK_STATUS_CHANGES,
                Metrics.STATUS_TAG,
                request.getStatus().name()
        ).increment();

        return response;
    }

    public void delete(Long id, String email) {
        User user = userService.getByEmail(email);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Task not found")
                );

        if (!boardService.isOwner(task.getBoard(), user)) {
            throw new AccessDeniedException("Access denied");
        }

        taskCacheRepository.delete(id);
        taskRepository.delete(task);

        TaskDeletedPayload payload = new TaskDeletedPayload(
                task.getId(),
                task.getBoard().getId(),
                user.getId()
        );

        eventSender.eventType(EventType.TASK_DELETED)
                .payload(payload)
                .send();
    }
}