package com.example.tracker.repository.cache;

import com.example.tracker.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class TaskCacheRepository {

    private final RedisTemplate<String, TaskResponse> redisTemplate;

    private static final long TTL_MINUTES = 10;

    public TaskResponse findById(Long id) {

        return redisTemplate.opsForValue()
                .get(
                        "task:id:" + id
                );
    }

    public void save(TaskResponse task) {

        redisTemplate.opsForValue().set(
                "task:id:" + task.getId(),
                task,
                Duration.ofMinutes(TTL_MINUTES)
        );
    }

    public void delete(Long id) {

        redisTemplate.delete(
                "task:id:" + id
        );
    }
}