package ru.itis.discovery_server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record RegistrationRequest(
        @NotBlank(message = "Service name cannot be null")
        String serviceName,

        @NotNull(message = "Port cannot be null")
        @Range(min = 0, max = 65535, message = "Port must be between 0 and 65535")
        Integer port
) {
}
