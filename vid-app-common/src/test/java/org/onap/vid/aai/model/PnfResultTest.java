package org.onap.vid.aai.model;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class PnfResultTest {

    private PnfResult pnfResult;

    @Before
    public void setUp(){
        pnfResult = new PnfResult();
        pnfResult.setAdditionalProperty("key1", "value1");
        pnfResult.setAdditionalProperty("key2", "value2");
    }

    @Test
    public void shouldHaveValidGettersAndSetters() throws IOException {
        String result = new ObjectMapper().writeValueAsString(pnfResult);
        assertThat(result, containsString("key1"));
        assertThat(result, containsString("value2"));
        assertThat(result, containsString("key2"));
        assertThat(result, containsString("value2"));
    }
}