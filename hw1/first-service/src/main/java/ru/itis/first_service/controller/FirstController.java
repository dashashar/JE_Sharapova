package ru.itis.first_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.discovery_starter.client.DiscoveryClient;
import ru.itis.first_service.dto.Dto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FirstController {

    private final DiscoveryClient discoveryClient;

    @PostMapping("/message")
    public Dto replyToMessage(@RequestBody Dto request) {
        log.info("I got the message: {}", request.message());
        return new Dto(String.format("First says, I got the message: %s", request.message()));
    }

}
