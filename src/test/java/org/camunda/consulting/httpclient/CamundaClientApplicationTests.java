package org.camunda.consulting.httpclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "camunda.base-url=https://your-camunda-endpoint",
        "camunda.auth.token-url=https://login.cloud.camunda.io/oauth/token",
        "camunda.auth.client-id=test-client-id",
        "camunda.auth.client-secret=test-client-secret",
        "camunda.auth.audience=zeebe.camunda.io",
        "camunda.auth.scope=Zeebe"
})
class CamundaClientApplicationTests {

    @Test
    void contextLoads() {
    }

}
