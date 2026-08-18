package notification.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreatedPayload implements EventPayload{

    private Long boardId;

    private String title;

    private Long ownerId;

}
