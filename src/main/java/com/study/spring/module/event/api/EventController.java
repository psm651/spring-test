package com.study.spring.module.event.api;

import com.study.spring.module.event.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping
public class EventController {
    private final EventService eventService;

    @PostMapping("board/{value}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@PathVariable String value) {
        log.info("EventController Thread ID : {}", Thread.currentThread().getId());
        eventService.saveBoard(value);
    }
}
