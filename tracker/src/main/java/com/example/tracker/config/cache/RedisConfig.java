package com.example.tracker.config.cache;

import com.example.tracker.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserResponse> userRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {

        ObjectMapper objectMapper =
                new ObjectMapper();

        objectMapper.registerModule(
                new JavaTimeModule()
        );

        objectMapper.disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        Jackson2JsonRedisSerializer<UserResponse> serializer =
                new Jackson2JsonRedisSerializer<>(
                        objectMapper,
                        UserResponse.class
                );

        RedisTemplate<String, UserResponse> template =
                new RedisTemplate<>();

        template.setConnectionFactory(
                connectionFactory
        );

        template.setKeySerializer(
                new StringRedisSerializer()
        );

        template.setValueSerializer(
                serializer
        );

        template.afterPropertiesSet();

        return template;
    }
}