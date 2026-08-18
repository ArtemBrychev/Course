package notification.eventdriven;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage {

    private EventType eventType;

    private LocalDateTime occurredAt;

    private JsonNode payload;

}
