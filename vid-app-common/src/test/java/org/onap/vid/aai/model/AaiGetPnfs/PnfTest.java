package org.onap.vid.aai.model.AaiGetPnfs;

import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;

public class PnfTest {

    @Test
    public void shouldHaveValidGettersAndSetters(){
        assertThat(Pnf.class, hasValidGettersAndSetters());
    }

}