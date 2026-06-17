package com.example.analytics.service;

import com.example.analytics.dto.ActivityResponse;
import com.example.analytics.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final EventRepository eventRepository;

    public List<ActivityResponse> getActivity(Long boardId) {

        return eventRepository.findAll()
                .stream()
                .filter(e -> e.getBoardId() != null && e.getBoardId().equals(boardId))
                .sorted(Comparator.comparing(e -> e.getOccurredAt()))
                .skip(Math.max(0, eventRepository.count() - 100))
                .map(e -> new ActivityResponse(
                        e.getEventType(),
                        e.getUserId(),
                        e.getTaskId(),
                        e.getOccurredAt(),
                        e.getPayload()
                ))
                .toList();
    }
}
