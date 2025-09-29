package ru.itis.discovery_server.service;

import ru.itis.discovery_server.dto.RegistrationRequest;
import ru.itis.discovery_server.dto.ServiceResponse;
import ru.itis.discovery_server.dto.StatusResponse;
import ru.itis.discovery_server.model.ServiceEntity;

import java.util.List;
import java.util.Map;

public interface DiscoveryService {

    StatusResponse registerService(RegistrationRequest regRequest, String host);

    ServiceResponse getService(String serviceName);

    Map<String, List<ServiceEntity>> getAllServices();

    void removeInstance(String serviceName, ServiceEntity service);

}
