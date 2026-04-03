package org.camunda.consulting.httpclient;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CamundaService {

    private final RestClient camundaApiRestClient;

    public CamundaService(RestClient camundaApiRestClient) {
        this.camundaApiRestClient = camundaApiRestClient;
    }

    public String topology() {
        String response = this.camundaApiRestClient.get()
                .uri("/topology")
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda topology response must not be null");
    }
}
