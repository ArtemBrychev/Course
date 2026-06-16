package com.example.analytics.eventdriven;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventListener {

    private final EventDispatcher dispatcher;

    @RabbitListener(
            queues = RabbitConfig.ANALYTICS_QUEUE
    )
    public void receive(EventMessage event) throws JsonProcessingException {

        dispatcher.dispatch(event);

    }

}