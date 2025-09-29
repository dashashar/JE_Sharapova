package ru.itis.discovery_server.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.discovery_server.dto.RegistrationRequest;
import ru.itis.discovery_server.dto.ServiceResponse;
import ru.itis.discovery_server.dto.StatusResponse;
import ru.itis.discovery_server.model.ServiceEntity;
import ru.itis.discovery_server.service.DiscoveryService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusResponse register(@Valid @RequestBody RegistrationRequest regRequest, HttpServletRequest request) {
        return discoveryService.registerService(regRequest, request.getRemoteAddr());
    }

    @GetMapping("/discover/{serviceName}")
    public ServiceResponse getService(@PathVariable String serviceName) {
        return discoveryService.getService(serviceName);
    }

    @GetMapping
    public Map<String, List<ServiceEntity>> getAllServices() {
        return discoveryService.getAllServices();
    }
}
