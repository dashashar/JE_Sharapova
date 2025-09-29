package ru.itis.discovery_server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.itis.discovery_server.dto.RegistrationRequest;
import ru.itis.discovery_server.dto.ServiceResponse;
import ru.itis.discovery_server.model.ServiceEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserServiceMapper {

    @Mapping(target = "host", source = "host")
    ServiceEntity toEntity(RegistrationRequest request, String host);

    ServiceResponse toResponse(ServiceEntity entity);

}
