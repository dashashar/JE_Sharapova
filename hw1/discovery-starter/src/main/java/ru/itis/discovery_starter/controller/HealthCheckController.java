package ru.itis.discovery_starter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.discovery_starter.dto.StatusResponse;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public StatusResponse healthCheck() {
        return new StatusResponse("I'm healthy");
    }
}
