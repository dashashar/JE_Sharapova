package ru.itis.discovery_starter.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.itis.discovery_starter.dto.ServiceResponse;
import ru.itis.discovery_starter.exception.DiscoveryException;
import ru.itis.discovery_starter.property.DiscoveryProperties;

import java.net.URI;

public class DiscoveryClientImpl implements DiscoveryClient {

    private final DiscoveryProperties properties;
    private final WebClient webClient;

    public DiscoveryClientImpl(DiscoveryProperties properties, @Qualifier("discoveryWebClient") WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    @Override
    public URI getInstance(String serviceName) {
        String discoverUrl = String.format("%s/discover/%s", properties.getServerUrl(), serviceName);
        try {
            ServiceResponse service = webClient.get()
                    .uri(discoverUrl)
                    .retrieve()
                    .bodyToMono(ServiceResponse.class)
                    .block();
            if (service == null) {
                throw new DiscoveryException("Service not found");
            }
            return URI.create(String.format("http://%s:%d", service.host(), service.port()));
        } catch (WebClientResponseException.NotFound e) {
            throw new DiscoveryException(String.format("Service %s not found", serviceName), e);
        } catch (Exception e) {
            throw new DiscoveryException(String.format("Failed to get instance for service %s", serviceName), e);
        }
    }
}
