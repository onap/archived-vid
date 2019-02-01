package org.onap.vid.aai;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class OperationalEnvironmentTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String OPERATIONAL_ENVIRONMENT_TEST = "{\n" +
            "\"operational-environment-id\": \"sample\",\n" +
            "\"operational-environment-name\": \"sample\",\n" +
            "\"operational-environment-type\": \"sample\",\n" +
            "\"operational-environment-status\": \"sample\",\n" +
            "\"tenant-context\": \"sample\",\n" +
            "\"workload-context\": \"sample\",\n" +
            "\"resource-version\": \"sample\",\n" +
            "\"relationship-list\": {\n" +
            "\"relationship\": []\n" +
            "}\n" +
            "}";

    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(OperationalEnvironment.class, hasValidGettersAndSetters());
    }


    @Test
    public void shouldProperlyConvertJsonToOperationalEnvironment() throws IOException {
        OperationalEnvironment operationalEnvironment = OBJECT_MAPPER.readValue(OPERATIONAL_ENVIRONMENT_TEST, OperationalEnvironment.class);

        assertThat(operationalEnvironment.getOperationalEnvironmentId(), is("sample"));
        assertThat(operationalEnvironment.getWorkloadContext(), is("sample"));
        assertThat(operationalEnvironment.getRelationshipList().getRelationship(), hasSize(0));
    }

}