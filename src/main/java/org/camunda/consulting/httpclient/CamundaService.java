package org.camunda.consulting.httpclient;

import java.util.Objects;

import org.springframework.http.MediaType;
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

    public String getDecisionDefinition(long decisionDefinitionKey) {
        String response = this.camundaApiRestClient.get()
                .uri("/decision-definitions/{decisionDefinitionKey}", decisionDefinitionKey)
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda decision-definitions/" + decisionDefinitionKey + " response must not be null");
    }

    public String getDecisionDefinitionXml(long decisionDefinitionKey) {
        String response = this.camundaApiRestClient.get()
                .uri("/decision-definitions/{decisionDefinitionKey}/xml", decisionDefinitionKey)
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda decision-definitions/" + decisionDefinitionKey + "/xml response must not be null");
    }

    public String searchDecisionDefinitions(Object requestBody) {
        String response = this.camundaApiRestClient.post()
                .uri("/decision-definitions/search")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda decision-definitions/search response must not be null");
    }

    public String evaluateDecisionDefinition(Object requestBody) {
        String response = this.camundaApiRestClient.post()
                .uri("/decision-definitions/evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
        return Objects.requireNonNull(response, "Camunda decision-definitions/evaluation response must not be null");
    }
}
