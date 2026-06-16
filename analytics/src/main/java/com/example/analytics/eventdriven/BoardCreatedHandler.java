package com.example.analytics.eventdriven;

import com.example.analytics.eventdriven.events.BoardCreatedPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardCreatedHandler {

    public void handle(
            BoardCreatedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Is this shit working? " + payload.toString());
    }

}