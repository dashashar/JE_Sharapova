package ru.itis.discovery_starter.client;

import java.net.URI;

public interface DiscoveryClient {

    URI getInstance(String serviceName);

}
