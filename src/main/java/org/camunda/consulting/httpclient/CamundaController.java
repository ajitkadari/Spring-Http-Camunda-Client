package org.camunda.consulting.httpclient;

import java.util.Objects;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/camunda")
@Tag(name = "Camunda", description = "Camunda API proxy endpoints")
public class CamundaController {

    private final RestClient camundaApiRestClient;

    public CamundaController(RestClient camundaApiRestClient) {
        this.camundaApiRestClient = camundaApiRestClient;
    }

    @GetMapping(path = "/topology", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Camunda topology", description = "Returns topology payload from the configured Camunda base URL")
    public String topology() {
        String response = this.camundaApiRestClient.get()
                .uri("/topology")
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda topology response must not be null");
    }
}



