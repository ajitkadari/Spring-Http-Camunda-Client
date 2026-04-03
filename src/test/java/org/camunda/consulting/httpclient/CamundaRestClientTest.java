package org.camunda.consulting.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class CamundaRestClientTest {

    @Test
    void addsBearerTokenAndFetchesTopology() {
        TokenProvider tokenProvider = () -> CamundaTestSupport.TEST_TOKEN;

        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = CamundaTestSupport.apiRestClient(builder, tokenProvider);
        CamundaService camundaService = new CamundaService(restClient);

        server.expect(requestTo(CamundaTestSupport.TOPOLOGY_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + CamundaTestSupport.TEST_TOKEN))
                .andRespond(withSuccess(CamundaTestSupport.TOPOLOGY_RESPONSE, MediaType.APPLICATION_JSON));

        String response = camundaService.topology();

        assertEquals(CamundaTestSupport.TOPOLOGY_RESPONSE, response);
        server.verify();
    }
}





