package ru.itis.discovery_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    private String serviceName;
    private String host;
    private int port;

}
