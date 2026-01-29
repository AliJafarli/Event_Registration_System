package com.codealpha.eventreg.service;

import com.codealpha.eventreg.domain.*;
import com.codealpha.eventreg.dto.RegistrationView;
import com.codealpha.eventreg.exception.ApiExceptions;
import com.codealpha.eventreg.repo.EventRepository;
import com.codealpha.eventreg.repo.RegistrationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public RegistrationView register(Long eventId, User currentUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiExceptions.NotFound("Event not found"));

        if (event.getStatus() != Event.Status.PUBLISHED) {
            throw new ApiExceptions.BadRequest("Event is not open for registration");
        }
        if (OffsetDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new ApiExceptions.BadRequest("Registration deadline has passed");
        }

        Registration existing = registrationRepository
                .findByUserIdAndEventId(currentUser.getId(), eventId)
                .orElse(null);

        if (existing != null) {
            if (existing.getStatus() == Registration.Status.ACTIVE) {
                throw new ApiExceptions.Conflict("Already registered");
            }
            existing.setStatus(Registration.Status.ACTIVE);
            existing.setRegisteredAt(OffsetDateTime.now());
            existing.setCancelledAt(null);

            return RegistrationView.builder()
                    .id(existing.getId())
                    .eventId(event.getId())
                    .eventTitle(event.getTitle())
                    .status(existing.getStatus())
                    .registeredAt(existing.getRegisteredAt())
                    .cancelledAt(null)
                    .build();
        }

        long taken = registrationRepository.countByEventIdAndStatus(
                eventId, Registration.Status.ACTIVE);

        if (taken >= event.getCapacity()) {
            throw new ApiExceptions.Conflict("No seats available");
        }

        Registration r = new Registration();
        r.setUser(currentUser);
        r.setEvent(event);
        r.setStatus(Registration.Status.ACTIVE);
        r.setRegisteredAt(OffsetDateTime.now());
        r = registrationRepository.save(r);

        return RegistrationView.builder()
                .id(r.getId())
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .status(r.getStatus())
                .registeredAt(r.getRegisteredAt())
                .cancelledAt(r.getCancelledAt())
                .build();
    }


    public Page<RegistrationView> myRegistrations(User me, Pageable pageable) {
        return registrationRepository
                .findByUserIdWithEvent(me.getId(), pageable)
                .map(r -> RegistrationView.builder()
                        .id(r.getId())
                        .eventId(r.getEvent().getId())
                        .eventTitle(r.getEvent().getTitle())
                        .status(r.getStatus())
                        .registeredAt(r.getRegisteredAt())
                        .cancelledAt(r.getCancelledAt())
                        .build()
                );
    }


    @Transactional
    public void cancelMine(Long registrationId, User me) {
        Registration r = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ApiExceptions.NotFound("Registration not found"));

        if (!r.getUser().getId().equals(me.getId())) {
            throw new ApiExceptions.Forbidden("Not your registration");
        }
        if (r.getStatus() == Registration.Status.CANCELLED) return;

        r.setStatus(Registration.Status.CANCELLED);
        r.setCancelledAt(OffsetDateTime.now());
    }

    public Page<RegistrationView> adminEventRegistrations(Long eventId, Pageable pageable) {
        return registrationRepository.findByEventIdWithEvent(eventId, pageable)
                .map(r -> RegistrationView.builder()
                        .id(r.getId())
                        .eventId(r.getEvent().getId())
                        .eventTitle(r.getEvent().getTitle())
                        .status(r.getStatus())
                        .registeredAt(r.getRegisteredAt())
                        .cancelledAt(r.getCancelledAt())
                        .build());
    }


    @Transactional
    public void adminCancelRegistration(Long registrationId) {
        Registration r = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ApiExceptions.NotFound("Registration not found"));
        if (r.getStatus() == Registration.Status.CANCELLED) return;
        r.setStatus(Registration.Status.CANCELLED);
        r.setCancelledAt(OffsetDateTime.now());
    }
}
