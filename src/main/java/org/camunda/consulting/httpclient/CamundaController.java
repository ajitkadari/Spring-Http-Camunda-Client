package org.camunda.consulting.httpclient;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/camunda")
public class CamundaController {

    @Autowired
    private final RestClient camundaApiRestClient;

    public CamundaController(RestClient camundaApiRestClient) {
        this.camundaApiRestClient = camundaApiRestClient;
    }

    @GetMapping(path = "/topology", produces = MediaType.APPLICATION_JSON_VALUE)
    public String topology() {
        String response = this.camundaApiRestClient.get()
                .uri("/topology")
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda topology response must not be null");
    }
}



