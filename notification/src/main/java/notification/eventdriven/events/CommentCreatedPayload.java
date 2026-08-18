package notification.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedPayload implements EventPayload{

    private Long commentId;

    private Long taskId;

    private Long boardId;

    private Long authorId;

    private String text;

}
