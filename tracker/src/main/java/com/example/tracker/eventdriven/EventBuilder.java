package com.example.tracker.eventdriven;

import com.example.tracker.config.RabbitConfig;
import com.example.tracker.eventdriven.events.EventPayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;
import java.time.LocalDateTime;

public class EventBuilder<T extends EventPayload> {

    private final EventType eventType;

    private T payload;

    private LocalDateTime occurredAt = LocalDateTime.now();

    private final RabbitTemplate rabbitTemplate;


    public EventBuilder(EventType eventType, RabbitTemplate rabbitTemplate) {
        this.eventType = eventType;
        this.rabbitTemplate = rabbitTemplate;
    }

    public EventBuilder<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public EventBuilder<T> occurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
        return this;
    }

    public void send() {

        if (payload == null) {
            throw new IllegalStateException("Payload is null");
        }

        EventMessage<T> message =
                new EventMessage<>(
                        eventType,
                        occurredAt,
                        payload
                );

        rabbitTemplate.convertAndSend(
                RabbitConfig.TASK_EXCHANGE,
                RabbitConfig.KEY,
                message
        );
    }

}
