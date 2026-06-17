package com.example.tracker.controller;

import com.example.tracker.dto.ChangeTaskStatusRequest;
import com.example.tracker.dto.CreateTaskRequest;
import com.example.tracker.dto.TaskResponse;
import com.example.tracker.dto.UpdateTaskRequest;
import com.example.tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tracker.observabilty.Metrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final MeterRegistry meterRegistry;

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> create(
            @RequestBody CreateTaskRequest request,
            Principal principal
    ) {

        return ResponseEntity.ok(
                taskService.create(
                        principal.getName(),
                        request
                )
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskResponse> getById(
            @PathVariable Long id,
            Principal principal
    ) {

        return ResponseEntity.ok(
                taskService.getById(
                        id,
                        principal.getName()
                )
        );
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<TaskResponse>> getAllByBoard(
            @PathVariable Long boardId,
            Principal principal
    ) {
        Timer timer = meterRegistry.timer(Metrics.BOARD_TASKS_REQUEST_DURATION);
        return ResponseEntity.ok(
                taskService.getAllByBoard(
                        boardId,
                        principal.getName()
                )
        );
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request,
            Principal principal
    ) {

        return ResponseEntity.ok(
                taskService.update(
                        id,
                        principal.getName(),
                        request
                )
        );
    }

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<TaskResponse> changeStatus(
            @PathVariable Long id,
            @RequestBody ChangeTaskStatusRequest request,
            Principal principal
    ) {

        return ResponseEntity.ok(
                taskService.changeStatus(
                        id,
                        principal.getName(),
                        request
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            Principal principal
    ) {

        taskService.delete(
                id,
                principal.getName()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Task deleted"
                )
        );
    }
}