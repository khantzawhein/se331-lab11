package se331.lab.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se331.lab.entity.Event;
import se331.lab.service.EventService;
import se331.lab.util.LabMapper;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventController {
    final EventService eventService;
    HttpHeaders responseHeader = new HttpHeaders();

    @GetMapping("events")
    public ResponseEntity<?> getEventLists(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page,
            @RequestParam(value = "title", required = false) String title
    ) {
        perPage = perPage == null ? 3 : perPage;
        page = page == null ? 1 : page;
        Page<Event> output = eventService.getEvents(perPage, page);
        if (title == null) {
            output = eventService.getEvents(perPage, page);
        } else {
            output = eventService.getEvents(title, PageRequest.of(page - 1, perPage));
        }

        responseHeader.set("x-total-count", String.valueOf(output.getTotalElements()));
        return new ResponseEntity<>(LabMapper.INSTANCE.getEventDto(output.getContent()), responseHeader, HttpStatus.OK);
    }

    @GetMapping("events/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") Long id) {
        Event output = eventService.getEvent(id);

        if (output == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given ID is not found");
        }
        return ResponseEntity.ok(LabMapper.INSTANCE.getEventDto(output));
    }

    @PostMapping("events")
    public ResponseEntity<?> addEvent(@RequestBody Event event) {
        Event output = eventService.save(event);
        return ResponseEntity.ok(LabMapper.INSTANCE.getEventDto(output));
    }
}
