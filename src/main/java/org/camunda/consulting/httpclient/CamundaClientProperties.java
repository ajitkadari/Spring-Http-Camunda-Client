package org.camunda.consulting.httpclient;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "camunda")
public record CamundaClientProperties(
        @DefaultValue("http://localhost:8080") String baseUrl,
        @DefaultValue("/v2") String apiPath,
        @DefaultValue AuthProperties auth) {

    public record AuthProperties(
            @DefaultValue("") String tokenUrl,
            @DefaultValue("") String clientId,
            @DefaultValue("") String clientSecret,
            @DefaultValue("") String audience,
            @DefaultValue("") String scope,
            @DefaultValue("PT30S") Duration refreshSkew) {
    }
}




