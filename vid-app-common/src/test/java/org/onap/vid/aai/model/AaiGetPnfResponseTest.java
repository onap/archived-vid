package org.onap.vid.aai.model;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

public class AaiGetPnfResponseTest {

    private AaiGetPnfResponse aaiGetPnfResponse;

    @Before
    public void setUp(){
        aaiGetPnfResponse = new AaiGetPnfResponse();
        aaiGetPnfResponse.results = new ArrayList<>();
        aaiGetPnfResponse.setAdditionalProperty("key1", "value1");
        aaiGetPnfResponse.setAdditionalProperty("key2", "value2");
    }

    @Test
    public void shouldHaveValidGettersAndSetters() throws IOException {
        String result = new ObjectMapper().writeValueAsString(aaiGetPnfResponse);
        assertThat(result, containsString("key1"));
        assertThat(result, containsString("value2"));
        assertThat(result, containsString("key2"));
        assertThat(result, containsString("value2"));
    }

    @Test
    public void shouldHaveValidToString(){
        assertThat(aaiGetPnfResponse.toString(),
                equalTo("AaiGetPnfResponse{results=[], additionalProperties={key1=value1, key2=value2}}"));
    }

}