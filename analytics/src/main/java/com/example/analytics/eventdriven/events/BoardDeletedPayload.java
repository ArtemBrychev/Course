package com.example.analytics.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDeletedPayload implements EventPayload{

    private Long boardId;

    private Long deletedBy;

}