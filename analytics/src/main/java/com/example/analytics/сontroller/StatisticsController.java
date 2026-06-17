package com.example.analytics.сontroller;

import com.example.analytics.dto.*;
import com.example.analytics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService service;

    @GetMapping
    public StatisticsResponse getGeneral() {
        return service.getGeneralStatistics();
    }

    @GetMapping("/statuses")
    public StatusStatisticsResponse getStatuses() {
        return service.getStatusStatistics();
    }

    @GetMapping("/users/top")
    public List<TopUserResponse> getTopUsers() {
        return service.getTopUsers();
    }

    @GetMapping("/users/{userId}")
    public UserStatisticsResponse getUser(@PathVariable Long userId) {
        return service.getUserStatistics(userId);
    }

    @GetMapping("/users/{userId}/boards/{boardId}")
    public UserBoardStatisticsResponse getUserBoard(
            @PathVariable Long userId,
            @PathVariable Long boardId
    ) {
        return service.getUserBoardStatistics(userId, boardId);
    }

    @GetMapping("/users/{userId}/tasks/{taskId}")
    public UserTaskStatisticsResponse getUserTask(
            @PathVariable Long userId,
            @PathVariable Long taskId
    ) {
        return service.getUserTaskStatistics(userId, taskId);
    }
}