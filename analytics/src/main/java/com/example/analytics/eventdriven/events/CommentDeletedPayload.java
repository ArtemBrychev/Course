package com.example.analytics.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeletedPayload implements EventPayload{

    private Long commentId;

    private Long taskId;

    private Long deletedBy;

}
