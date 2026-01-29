package com.codealpha.eventreg.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "registrations",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_event", columnNames = {"user_id", "event_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration {

    public enum Status { ACTIVE, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "registered_at", nullable = false)
    @Builder.Default
    private OffsetDateTime registeredAt = OffsetDateTime.now();

    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;
}
