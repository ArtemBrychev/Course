package notification.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMemberRemovedPayload implements EventPayload {

    private long boardId;
    private long ownerId;
    private long memberId;
}
