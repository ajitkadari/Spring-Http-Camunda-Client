package org.camunda.consulting.httpclient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/camunda")
@Tag(name = "Camunda", description = "Camunda API proxy endpoints")
public class CamundaController {

    private final CamundaService camundaService;

    public CamundaController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }

    @GetMapping(path = "/topology", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Camunda Cluster topology", description = "Returns Camunda Cluster topology payload from the configured Camunda base URL")
    public String topology() {
        return this.camundaService.topology();
    }
}



