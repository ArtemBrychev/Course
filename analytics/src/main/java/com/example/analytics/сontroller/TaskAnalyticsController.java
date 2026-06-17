package com.example.analytics.сontroller;

import com.example.analytics.dto.*;
import com.example.analytics.service.TaskAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskAnalyticsController {

    private final TaskAnalyticsService service;

    @GetMapping("/{taskId}/history")
    public List<TaskHistoryResponse> getHistory(@PathVariable Long taskId) {
        return service.getTaskHistory(taskId);
    }

    @GetMapping("/{taskId}/status-history")
    public List<StatusHistoryResponse> getStatusHistory(@PathVariable Long taskId) {
        return service.getStatusHistory(taskId);
    }

    @GetMapping("/{taskId}/state")
    public TaskStateResponse getState(@PathVariable Long taskId) {
        return service.getTaskState(taskId);
    }
}
