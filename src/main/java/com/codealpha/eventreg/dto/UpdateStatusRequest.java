package com.codealpha.eventreg.dto;

import com.codealpha.eventreg.domain.Event;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusRequest {
    @NotNull
    private Event.Status status;
}
