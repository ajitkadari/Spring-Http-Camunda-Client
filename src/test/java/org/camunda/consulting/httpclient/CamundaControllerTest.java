package org.camunda.consulting.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CamundaControllerTest {

    @Test
    void exposesCamundaTopologyEndpoint() throws Exception {
        TokenProvider tokenProvider = () -> CamundaTestSupport.TEST_TOKEN;

        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = CamundaTestSupport.apiRestClient(builder, tokenProvider);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
                new CamundaController(restClient))
                .build();

        server.expect(requestTo(CamundaTestSupport.TOPOLOGY_URL))
                .andRespond(withSuccess(CamundaTestSupport.TOPOLOGY_RESPONSE, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/camunda/topology"))
                .andExpect(status().isOk())
                .andExpect(content().json(CamundaTestSupport.TOPOLOGY_RESPONSE));

        server.verify();
    }
}




