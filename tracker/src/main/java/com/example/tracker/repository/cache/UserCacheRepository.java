package com.example.tracker.repository.cache;

import com.example.tracker.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, UserResponse> redisTemplate;

    private static final long TTL_MINUTES = 10;

    public UserResponse findById(Long id) {

        return redisTemplate.opsForValue()
                .get(
                        "user:id:" + id
                );
    }

    public UserResponse findByEmail(String email) {

        return redisTemplate.opsForValue()
                .get(
                        "user:email:" + email
                );
    }

    public void save(UserResponse user) {

        redisTemplate.opsForValue().set(
                "user:id:" + user.getId(),
                user,
                Duration.ofMinutes(TTL_MINUTES)
        );

        redisTemplate.opsForValue().set(
                "user:email:" + user.getEmail(),
                user,
                Duration.ofMinutes(TTL_MINUTES)
        );
    }

    public void delete(UserResponse user) {

        redisTemplate.delete(
                "user:id:" + user.getId()
        );

        redisTemplate.delete(
                "user:email:" + user.getEmail()
        );
    }
}
