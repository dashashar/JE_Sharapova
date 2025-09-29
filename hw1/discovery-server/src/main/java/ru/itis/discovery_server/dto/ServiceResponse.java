package ru.itis.discovery_server.dto;

public record ServiceResponse(
        String host,
        int port
) {
}