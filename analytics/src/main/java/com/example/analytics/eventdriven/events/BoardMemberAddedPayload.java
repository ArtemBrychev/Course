package com.example.analytics.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMemberAddedPayload implements EventPayload {

    private long boardId;
    private long ownerId;
    private long memberId;
}
