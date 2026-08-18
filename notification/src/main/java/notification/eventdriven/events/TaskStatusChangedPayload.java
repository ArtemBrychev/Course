package notification.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusChangedPayload implements EventPayload{

    private Long taskId;

    private Long boardId;

    private String oldStatus;

    private String newStatus;

    private Long changedBy;

}
