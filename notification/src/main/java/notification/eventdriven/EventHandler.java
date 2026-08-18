package notification.eventdriven;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

    private final ObjectMapper objectMapper;

    public void handle(EventMessage event) throws JsonProcessingException {
        log.info("Event with type: " + event.getEventType() + "is recieved");
    }
}