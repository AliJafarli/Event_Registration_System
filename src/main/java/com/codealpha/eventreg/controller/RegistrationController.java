package com.codealpha.eventreg.controller;

import com.codealpha.eventreg.dto.RegistrationView;
import com.codealpha.eventreg.security.UserPrincipal;
import com.codealpha.eventreg.service.RegistrationService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) { this.registrationService = registrationService; }

    @PostMapping("/api/events/{eventId}/registrations")
    @PreAuthorize("hasRole('USER')")
    public RegistrationView register(@PathVariable("eventId") Long eventId,
                                                      @AuthenticationPrincipal UserPrincipal me) {
        return registrationService.register(eventId, me.getUser());
    }

    @GetMapping("/api/me/registrations")
    @PreAuthorize("hasRole('USER')")
    public Page<RegistrationView> my(Pageable pageable,
                                                      @AuthenticationPrincipal UserPrincipal me) {
        return registrationService.myRegistrations(me.getUser(), pageable);
    }

    @DeleteMapping("/api/me/registrations/{registrationId}")
    @PreAuthorize("hasRole('USER')")
    public void cancel(@PathVariable("registrationId") Long registrationId,
                       @AuthenticationPrincipal UserPrincipal me) {
        registrationService.cancelMine(registrationId, me.getUser());
    }
}
