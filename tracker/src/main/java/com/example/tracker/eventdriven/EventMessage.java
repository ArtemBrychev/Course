package com.example.tracker.eventdriven;

import com.example.tracker.eventdriven.events.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage<T extends EventPayload> {

    private EventType eventType;

    private LocalDateTime occurredAt;

    private T payload;

}