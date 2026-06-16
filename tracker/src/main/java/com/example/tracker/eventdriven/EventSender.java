package com.example.tracker.eventdriven;

import com.example.tracker.eventdriven.events.EventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventSender {

    private final RabbitTemplate rabbitTemplate;

    public <T extends EventPayload> EventBuilder<T>
    eventType(EventType eventType) {
        return new EventBuilder<>(eventType, rabbitTemplate);
    }

}