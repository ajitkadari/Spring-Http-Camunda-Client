package org.camunda.consulting.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void exposesDecisionDefinitionsSearchEndpoint() throws Exception {
        CamundaService camundaService = mock(CamundaService.class);
        when(camundaService.searchDecisionDefinitions(CamundaTestSupport.DECISION_DEFINITIONS_SEARCH_REQUEST))
                .thenReturn(CamundaTestSupport.DECISION_DEFINITIONS_SEARCH_RESPONSE);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
                new CamundaController(camundaService))
                .build();

        mockMvc.perform(post("/api/camunda/decision-definitions/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CamundaTestSupport.DECISION_DEFINITIONS_SEARCH_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().json(CamundaTestSupport.DECISION_DEFINITIONS_SEARCH_RESPONSE));
    }

    @Test
    void exposesDecisionDefinitionsEvaluationEndpoint() throws Exception {
        CamundaService camundaService = mock(CamundaService.class);
        when(camundaService.evaluateDecisionDefinition(CamundaTestSupport.DECISION_DEFINITIONS_EVALUATION_REQUEST))
                .thenReturn(CamundaTestSupport.DECISION_DEFINITIONS_EVALUATION_RESPONSE);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
                new CamundaController(camundaService))
                .build();

        mockMvc.perform(post("/api/camunda/decision-definitions/evaluation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CamundaTestSupport.DECISION_DEFINITIONS_EVALUATION_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().json(CamundaTestSupport.DECISION_DEFINITIONS_EVALUATION_RESPONSE));
    }
}




