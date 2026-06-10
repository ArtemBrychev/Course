package com.example.tracker.service;

import com.example.tracker.dto.ChangeTaskStatusRequest;
import com.example.tracker.dto.CreateTaskRequest;
import com.example.tracker.dto.TaskResponse;
import com.example.tracker.dto.UpdateTaskRequest;
import com.example.tracker.exceptions.AccessDeniedException;
import com.example.tracker.exceptions.DataNotFoundException;
import com.example.tracker.model.Board;
import com.example.tracker.model.Task;
import com.example.tracker.model.User;
import com.example.tracker.repository.BoardRepository;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.repository.cache.TaskCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (request.getAssigneeId() != null) {

            User assignee = userRepository.findById(
                    request.getAssigneeId()
            ).orElseThrow(() ->
                    new DataNotFoundException("User not found")
            );

            task.setAssignee(assignee);
        }

        Task updated = taskRepository.save(task);
        taskCacheRepository.delete(id);

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

        task.setStatus(request.getStatus());

        Task updated = taskRepository.save(task);

        TaskResponse response = TaskResponse.from(updated);

        taskCacheRepository.save(response);

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
    }
}