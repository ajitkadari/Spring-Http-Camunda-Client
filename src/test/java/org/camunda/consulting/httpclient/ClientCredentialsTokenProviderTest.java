package org.camunda.consulting.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ClientCredentialsTokenProviderTest {

    @Test
    void fetchesTokenOnceAndReusesIt() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CamundaClientProperties properties = CamundaTestSupport.clientProperties();
        TokenProvider tokenProvider = new ClientCredentialsTokenProvider(
                builder.build(),
                properties.auth());
        RestClient apiRestClient = new CamundaClientConfig().camundaApiRestClient(builder, properties, tokenProvider);

        server.expect(requestTo(CamundaTestSupport.TOKEN_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"access_token\":\"token-1\",\"expires_in\":300}", MediaType.APPLICATION_JSON));

        server.expect(requestTo(CamundaTestSupport.TOPOLOGY_URL))
                .andRespond(withSuccess(CamundaTestSupport.TOPOLOGY_RESPONSE, MediaType.APPLICATION_JSON));
        server.expect(requestTo(CamundaTestSupport.TOPOLOGY_URL))
                .andRespond(withSuccess(CamundaTestSupport.TOPOLOGY_RESPONSE, MediaType.APPLICATION_JSON));

        assertEquals(CamundaTestSupport.TOPOLOGY_RESPONSE, apiRestClient.get().uri("/topology").retrieve().body(String.class));
        assertEquals(CamundaTestSupport.TOPOLOGY_RESPONSE, apiRestClient.get().uri("/topology").retrieve().body(String.class));
        server.verify();
    }
}




