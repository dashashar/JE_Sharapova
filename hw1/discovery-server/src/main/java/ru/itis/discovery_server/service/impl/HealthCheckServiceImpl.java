package ru.itis.discovery_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.itis.discovery_server.model.ServiceEntity;
import ru.itis.discovery_server.service.DiscoveryService;
import ru.itis.discovery_server.service.HealthCheckService;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthCheckServiceImpl implements HealthCheckService {

    private final WebClient webClient;
    private final DiscoveryService discoveryService;

    @Scheduled(fixedRate = 10000)
    @Override
    public void healthCheckAllServices() {
        Map<String, List<ServiceEntity>> allServices = discoveryService.getAllServices();
        for (Map.Entry<String, List<ServiceEntity>> entry : allServices.entrySet()) {
            String serviceName = entry.getKey();
            List<ServiceEntity> services = entry.getValue();
            for (ServiceEntity instance : services) {
                healthCheckInstance(serviceName, instance);
            }
        }
    }

    private void healthCheckInstance(String serviceName, ServiceEntity instance) {
        String url = String.format("http://%s:%d/health", instance.getHost(), instance.getPort());
        webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .doOnSuccess(response ->
                        log.debug("Health check passed: {}", instance))
                .doOnError(error -> {
                    log.warn("Failed health check: {}, {}", instance, error.getMessage());
                })
                .onErrorResume(error -> {
                    discoveryService.removeInstance(serviceName, instance);
                    return Mono.empty();
                })
                .subscribe();
    }
}
