package com.codealpha.eventreg.service;

import com.codealpha.eventreg.domain.Event;
import com.codealpha.eventreg.domain.Registration;
import com.codealpha.eventreg.domain.User;
import com.codealpha.eventreg.dto.CreateEventRequest;
import com.codealpha.eventreg.dto.EventDetails;
import com.codealpha.eventreg.dto.EventListItem;
import com.codealpha.eventreg.dto.UpdateEventRequest;
import com.codealpha.eventreg.exception.ApiExceptions;
import com.codealpha.eventreg.repo.EventRepository;
import com.codealpha.eventreg.repo.RegistrationRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public Page<EventListItem> listUpcoming(Pageable pageable) {
        Page<Event> page = eventRepository.findByStatusAndStartAtAfterOrderByStartAtAsc(
                Event.Status.PUBLISHED, OffsetDateTime.now().minusYears(1), pageable
        );
        return page.map(e -> EventListItem.builder()
                .id(e.getId())
                .title(e.getTitle())
                .location(e.getLocation())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .build());
    }

    public EventDetails getPublishedDetails(Long id) {
        Event e = eventRepository.findByIdAndStatus(id, Event.Status.PUBLISHED)
                .orElseThrow(() -> new ApiExceptions.NotFound("Event not found"));
        long taken = registrationRepository.countByEventIdAndStatus(id, Registration.Status.ACTIVE);
        return EventDetails.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .location(e.getLocation())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .registrationDeadline(e.getRegistrationDeadline())
                .capacity(e.getCapacity())
                .takenSeats(taken)
                .status(e.getStatus())
                .build();
    }

    @Transactional
    public EventDetails adminCreate(CreateEventRequest req, User admin) {
        validateDates(req.getStartAt(), req.getEndAt(), req.getRegistrationDeadline());
        Event e = new Event();
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setLocation(req.getLocation());
        e.setStartAt(req.getStartAt());
        e.setEndAt(req.getEndAt());
        e.setRegistrationDeadline(req.getRegistrationDeadline());
        e.setCapacity(req.getCapacity());
        e.setStatus(req.getStatus());
        e.setCreatedBy(admin);
        eventRepository.save(e);
        return adminDetails(e.getId());
    }

    @Transactional
    public EventDetails adminUpdate(Long id, UpdateEventRequest req) {
        validateDates(req.getStartAt(), req.getEndAt(), req.getRegistrationDeadline());
        Event e = eventRepository.findById(id).orElseThrow(() -> new ApiExceptions.NotFound("Event not found"));
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setLocation(req.getLocation());
        e.setStartAt(req.getStartAt());
        e.setEndAt(req.getEndAt());
        e.setRegistrationDeadline(req.getRegistrationDeadline());
        e.setCapacity(req.getCapacity());
        e.setStatus(req.getStatus());
        return adminDetails(id);
    }

    @Transactional
    public EventDetails adminUpdateStatus(Long id, Event.Status status) {
        Event e = eventRepository.findById(id).orElseThrow(() -> new ApiExceptions.NotFound("Event not found"));
        e.setStatus(status);
        return adminDetails(id);
    }

    @Transactional
    public void adminCancel(Long id) {
        Event e = eventRepository.findById(id).orElseThrow(() -> new ApiExceptions.NotFound("Event not found"));
        e.setStatus(Event.Status.CANCELLED);
    }

    public EventDetails adminDetails(Long id) {
        Event e = eventRepository.findById(id).orElseThrow(() -> new ApiExceptions.NotFound("Event not found"));
        long taken = registrationRepository.countByEventIdAndStatus(id, Registration.Status.ACTIVE);
        return EventDetails.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .location(e.getLocation())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .registrationDeadline(e.getRegistrationDeadline())
                .capacity(e.getCapacity())
                .takenSeats(taken)
                .status(e.getStatus())
                .build();
    }

    private void validateDates(OffsetDateTime start, OffsetDateTime end, OffsetDateTime deadline) {
        if (!end.isAfter(start)) {
            throw new ApiExceptions.BadRequest("endAt must be after startAt");
        }
        if (deadline.isAfter(start)) {
            throw new ApiExceptions.BadRequest("registrationDeadline must be <= startAt");
        }
    }
}
