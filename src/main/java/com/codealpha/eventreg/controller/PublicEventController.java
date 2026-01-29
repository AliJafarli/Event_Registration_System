package com.codealpha.eventreg.controller;

import com.codealpha.eventreg.dto.EventDetails;
import com.codealpha.eventreg.dto.EventListItem;
import com.codealpha.eventreg.service.EventService;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class PublicEventController {

    private final EventService eventService;
    public PublicEventController(EventService eventService) { this.eventService = eventService; }

    @GetMapping
    public Page<EventListItem> list(Pageable pageable) {
        return eventService.listUpcoming(pageable);
    }

    @GetMapping("/{id}")
    public EventDetails details(@PathVariable("id") Long id) {
        return eventService.getPublishedDetails(id);
    }
}
