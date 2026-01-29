package com.codealpha.eventreg.dto;

import com.codealpha.eventreg.domain.Registration;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationView {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private Registration.Status status;
    private OffsetDateTime registeredAt;
    private OffsetDateTime cancelledAt;
}
