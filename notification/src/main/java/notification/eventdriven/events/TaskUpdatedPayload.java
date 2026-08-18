package notification.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdatedPayload implements EventPayload {

    private Long taskId;

    private Long boardId;

    private String title;

    private String description;

    private Long updatedBy;

}
