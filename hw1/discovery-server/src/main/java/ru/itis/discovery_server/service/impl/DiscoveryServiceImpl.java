package ru.itis.discovery_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.discovery_server.dto.RegistrationRequest;
import ru.itis.discovery_server.dto.ServiceResponse;
import ru.itis.discovery_server.dto.StatusResponse;
import ru.itis.discovery_server.exception.NotFoundException;
import ru.itis.discovery_server.mapper.UserServiceMapper;
import ru.itis.discovery_server.model.ServiceEntity;
import ru.itis.discovery_server.service.DiscoveryService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscoveryServiceImpl implements DiscoveryService {

    private final UserServiceMapper mapper;
    private final Map<String, List<ServiceEntity>> allServices = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Override
    public StatusResponse registerService(RegistrationRequest regRequest, String host) {
        ServiceEntity newInstance = mapper.toEntity(regRequest, host);
        allServices.computeIfAbsent(regRequest.serviceName(), k -> new CopyOnWriteArrayList<>()).add(newInstance);
        log.info("Instance {} {}:{} is registered", regRequest.serviceName(), host, regRequest.port());
        return new StatusResponse("Service has been successfully registered in discovery server");
    }

    @Override
    public ServiceResponse getService(String serviceName) {
        List<ServiceEntity> services = allServices.get(serviceName);
        if (services == null || services.isEmpty()) {
            throw new NotFoundException(String.format("Services under the name %s are not registered", services));
        }
        AtomicInteger counter = counters.computeIfAbsent(serviceName, k -> new AtomicInteger(0));
        int currentIndex = counter.getAndUpdate(i -> (i + 1) % services.size());
        ServiceEntity service = services.get(currentIndex);
        return mapper.toResponse(service);
    }

    @Override
    public Map<String, List<ServiceEntity>> getAllServices() {
        return Collections.unmodifiableMap(allServices);
    }

    @Override
    public void removeInstance(String serviceName, ServiceEntity instance) {
        List<ServiceEntity> services = allServices.get(serviceName);
        if (services != null) {
            boolean isRemoved = services.remove(instance);
            if (isRemoved) {
                log.info("Removed instance: {}", instance);
                AtomicInteger counter = counters.get(serviceName);
                if (services.isEmpty()) {
                    allServices.remove(serviceName);
                    counters.remove(serviceName);
                    log.info("All instances of the {} have been deleted", serviceName);
                } else if (counter != null && counter.get() >= services.size()) {
                    counter.set(0);
                }
            }
        }
    }
}
