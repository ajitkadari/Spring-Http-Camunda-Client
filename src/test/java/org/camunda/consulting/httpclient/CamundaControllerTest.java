package org.camunda.consulting.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CamundaControllerTest {

    @Test
    void exposesCamundaTopologyEndpoint() throws Exception {
        CamundaService camundaService = mock(CamundaService.class);
        when(camundaService.topology()).thenReturn(CamundaTestSupport.TOPOLOGY_RESPONSE);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
                new CamundaController(camundaService))
                .build();

        mockMvc.perform(get("/api/camunda/topology"))
                .andExpect(status().isOk())
                .andExpect(content().json(CamundaTestSupport.TOPOLOGY_RESPONSE));
    }
}




