package com.example.analytics.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentChangedPayload implements EventPayload{

    private Long commentId;

    private Long taskId;

    private Long boardId;

    private Long authorId;

    private String oldText;

    private String newText;
}
