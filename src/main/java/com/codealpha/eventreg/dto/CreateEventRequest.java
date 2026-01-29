package com.codealpha.eventreg.dto;

import com.codealpha.eventreg.domain.Event;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {
    @NotBlank
    private String title;

    private String description;
    private String location;

    @NotNull
    private OffsetDateTime startAt;

    @NotNull
    private OffsetDateTime endAt;

    @NotNull
    private OffsetDateTime registrationDeadline;

    @Min(1)
    private int capacity;

    @NotNull
    private Event.Status status;
}
