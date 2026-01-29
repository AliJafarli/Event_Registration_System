package com.codealpha.eventreg.controller;

import com.codealpha.eventreg.domain.Event;
import com.codealpha.eventreg.dto.CreateEventRequest;
import com.codealpha.eventreg.dto.EventDetails;
import com.codealpha.eventreg.dto.UpdateEventRequest;
import com.codealpha.eventreg.dto.UpdateStatusRequest;
import com.codealpha.eventreg.dto.RegistrationView;
import com.codealpha.eventreg.service.EventService;
import com.codealpha.eventreg.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    public AdminEventController(EventService eventService, RegistrationService registrationService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
    }

    @PostMapping("/events")
    public EventDetails create(@Valid @RequestBody CreateEventRequest req,
                                         @org.springframework.security.core.annotation.AuthenticationPrincipal com.codealpha.eventreg.security.UserPrincipal me) {
        return eventService.adminCreate(req, me.getUser());
    }

    @PutMapping("/events/{id}")
    public EventDetails update(@PathVariable("id") Long id,
                                         @Valid @RequestBody UpdateEventRequest req) {
        return eventService.adminUpdate(id, req);
    }

    @PatchMapping("/events/{id}/status")
    public EventDetails updateStatus(@PathVariable("id") Long id,
                                               @Valid @RequestBody UpdateStatusRequest req) {
        return eventService.adminUpdateStatus(id, req.getStatus());
    }

    @DeleteMapping("/events/{id}")
    public void cancelEvent(@PathVariable("id") Long id) {
        eventService.adminCancel(id);
    }

    @GetMapping("/events/{id}/registrations")
    public Page<RegistrationView> registrations(@PathVariable("id") Long id, Pageable pageable) {
        return registrationService.adminEventRegistrations(id, pageable);
    }

    @DeleteMapping("/registrations/{registrationId}")
    public void cancelRegistration(@PathVariable("registrationId") Long registrationId) {
        registrationService.adminCancelRegistration(registrationId);
    }
}
