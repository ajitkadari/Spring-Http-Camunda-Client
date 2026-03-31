package org.camunda.consulting.httpclient;

import java.time.Duration;

import org.springframework.web.client.RestClient;

final class CamundaTestSupport {

    static final String BASE_URL = "https://camunda8.example.com";
    static final String TOPOLOGY_URL = BASE_URL + "/v2/topology";
    static final String TOKEN_URL = "https://login.camunda.io/oauth/token";
    static final String TOPOLOGY_RESPONSE = "{\"clusterSize\":3}";
    static final String TEST_TOKEN = "test-token";

    private CamundaTestSupport() {
    }

    static CamundaClientProperties clientProperties() {
        return new CamundaClientProperties(
                BASE_URL,
                "/v2",
                new CamundaClientProperties.AuthProperties(
                        TOKEN_URL,
                        "test-client",
                        "test-secret",
                        "zeebe.camunda.io",
                        "Zeebe",
                        Duration.ofSeconds(30)));
    }

    static RestClient apiRestClient(RestClient.Builder builder, TokenProvider tokenProvider) {
        return new CamundaClientConfig().camundaApiRestClient(builder, clientProperties(), tokenProvider);
    }
}

