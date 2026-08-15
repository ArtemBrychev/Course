package com.example.tracker.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRenamedPayload  implements EventPayload{

    private Long boardId;

    private Long ownerId;

    private String newTitle;

    private String oldTitle;
}
