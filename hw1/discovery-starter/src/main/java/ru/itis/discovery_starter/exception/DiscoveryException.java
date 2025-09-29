package ru.itis.discovery_starter.exception;

public class DiscoveryException extends RuntimeException {
    public DiscoveryException(String message) {
        super(message);
    }

    public DiscoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
