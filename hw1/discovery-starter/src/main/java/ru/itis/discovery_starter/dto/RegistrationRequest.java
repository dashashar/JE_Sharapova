package ru.itis.discovery_starter.dto;

public record RegistrationRequest(
        String serviceName,
        int port
) {
}
