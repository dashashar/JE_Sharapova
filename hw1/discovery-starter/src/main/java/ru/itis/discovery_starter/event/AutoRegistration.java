package ru.itis.discovery_starter.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.reactive.function.client.WebClient;
import ru.itis.discovery_starter.dto.RegistrationRequest;
import ru.itis.discovery_starter.dto.StatusResponse;
import ru.itis.discovery_starter.property.DiscoveryProperties;

@Slf4j
public class AutoRegistration implements ApplicationListener<ApplicationReadyEvent> {

    private final DiscoveryProperties properties;
    private final WebClient webClient;

    public AutoRegistration(DiscoveryProperties properties, @Qualifier("discoveryWebClient") WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (properties.getServiceName() == null || properties.getServiceName().isEmpty()) {
            log.warn("No service name provided, skipping auto registration");
            return;
        }
        if (properties.getServicePort() == 0){
            log.warn("No service port provided, skipping auto registration");
            return;
        }
        String url = String.format("%s/register", properties.getServerUrl());
        webClient.post()
                .uri(url)
                .bodyValue(new RegistrationRequest(properties.getServiceName(), properties.getServicePort()))
                .retrieve()
                .bodyToMono(StatusResponse.class)
                .doOnSuccess(response -> log.info("{}", response.status()))
                .doOnError(error -> log.error("Service is not registered: {}", error.getMessage()))
                .block();
    }
}
