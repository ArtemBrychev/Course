package com.example.tracker.repository.cache;

import com.example.tracker.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class BoardCacheRepository {

    private final RedisTemplate<String, BoardResponse> redisTemplate;

    private static final long TTL_MINUTES = 10;

    public BoardResponse findById(Long id) {

        return redisTemplate.opsForValue()
                .get("board:id:" + id);
    }

    public void save(BoardResponse board) {

        redisTemplate.opsForValue().set(
                "board:id:" + board.getId(),
                board,
                Duration.ofMinutes(TTL_MINUTES)
        );
    }

    public void delete(Long id) {

        redisTemplate.delete(
                "board:id:" + id
        );
    }
}
