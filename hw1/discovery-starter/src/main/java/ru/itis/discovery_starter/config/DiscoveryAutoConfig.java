package ru.itis.discovery_starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.itis.discovery_starter.controller.HealthCheckController;
import ru.itis.discovery_starter.event.AutoRegistration;
import ru.itis.discovery_starter.client.DiscoveryClient;
import ru.itis.discovery_starter.client.DiscoveryClientImpl;
import ru.itis.discovery_starter.property.DiscoveryProperties;

@Configuration
@EnableConfigurationProperties(DiscoveryProperties.class)
public class DiscoveryAutoConfig {

    @Bean("discoveryWebClient")
    @ConditionalOnMissingBean(name = "discoveryWebClient")
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DiscoveryClient discoveryClient(DiscoveryProperties properties, WebClient webClient) {
        return new DiscoveryClientImpl(properties, webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AutoRegistration autoRegistration(DiscoveryProperties properties, WebClient webClient) {
        return new AutoRegistration(properties, webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public HealthCheckController healthCheckController() {
        return new HealthCheckController();
    }

}
