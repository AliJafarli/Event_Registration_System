package com.codealpha.eventreg.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventListItem {
    private Long id;
    private String title;
    private String location;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
}
