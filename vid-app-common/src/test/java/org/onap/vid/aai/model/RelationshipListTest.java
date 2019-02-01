package org.onap.vid.aai.model;


import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;

public class RelationshipListTest {

    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(RelationshipList.class, hasValidGettersAndSetters());
    }
}