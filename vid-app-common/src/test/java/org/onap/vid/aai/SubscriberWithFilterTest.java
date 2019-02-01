package org.onap.vid.aai;


import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;

public class SubscriberWithFilterTest {
    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(SubscriberWithFilter.class, hasValidGettersAndSetters());
    }
}