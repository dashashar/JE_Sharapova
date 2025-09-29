package ru.itis.discovery_starter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "discovery")
public class DiscoveryProperties {
    private String serverUrl = "http://localhost:8080";
    private String serviceName;
    private int servicePort;
}
