package com.example.analytics.eventdriven;

import com.example.analytics.eventdriven.events.BoardCreatedPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDispatcher {

    private final ObjectMapper objectMapper;

    private final BoardCreatedHandler boardCreatedHandler;

    public void dispatch(EventMessage event) throws JsonProcessingException {

        switch (event.getEventType()) {

            case BOARD_CREATED -> {

                BoardCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardCreatedPayload.class
                        );

                boardCreatedHandler.handle(
                        payload,
                        event.getOccurredAt()
                );
            }
        }

    }

}