package org.onap.vid.aai.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class PnfPropertiesTest {

    private PnfProperties pnfProperties;

    @Before
    public void setUp(){
        pnfProperties = new PnfProperties();
        pnfProperties.setAdditionalProperty("key1", "value1");
        pnfProperties.setAdditionalProperty("key2", "value2");
    }

    @Test
    public void shouldHaveValidGettersAndSetters() throws IOException {
        String result = new ObjectMapper().writeValueAsString(pnfProperties);
        assertThat(result, containsString("key1"));
        assertThat(result, containsString("value2"));
        assertThat(result, containsString("key2"));
        assertThat(result, containsString("value2"));
    }
}