package com.codealpha.eventreg.dto;

import com.codealpha.eventreg.domain.Event;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetails {
    private Long id;
    private String title;
    private String description;
    private String location;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private OffsetDateTime registrationDeadline;
    private int capacity;
    private long takenSeats;
    private Event.Status status;
}
