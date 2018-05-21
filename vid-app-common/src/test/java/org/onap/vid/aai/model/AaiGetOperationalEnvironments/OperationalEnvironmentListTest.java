package org.onap.vid.aai.model.AaiGetOperationalEnvironments;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class OperationalEnvironmentListTest {

    @Test
    public void shouldHaveValidGettersAndSetters(){
        assertThat(OperationalEnvironmentList.class, hasValidGettersAndSetters());
    }

}