package com.example.analytics.сontroller;

import com.example.analytics.dto.ActivityResponse;
import com.example.analytics.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;

    @GetMapping
    public List<ActivityResponse> getActivity(@RequestParam Long boardId) {
        return service.getActivity(boardId);
    }
}
