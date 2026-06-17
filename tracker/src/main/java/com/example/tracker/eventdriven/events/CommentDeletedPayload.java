package com.example.tracker.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeletedPayload implements EventPayload{

    private Long commentId;

    private Long taskId;

    private Long boardId;

    private Long deletedBy;

}
