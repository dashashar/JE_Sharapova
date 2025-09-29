package ru.itis.second_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import ru.itis.discovery_starter.client.DiscoveryClient;
import ru.itis.second_service.dto.Dto;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SecondController {

    private final DiscoveryClient discoveryClient;
    private final WebClient webClient;

    @PostMapping("/message")
    public Dto sendMessage(@RequestBody Dto request){
        URI firstServiceIri = discoveryClient.getInstance("first");
        return webClient.post()
                .uri(firstServiceIri.resolve("/message"))
                .bodyValue(new Dto(request.message()))
                .retrieve()
                .bodyToMono(Dto.class)
                .block();
    }

}
