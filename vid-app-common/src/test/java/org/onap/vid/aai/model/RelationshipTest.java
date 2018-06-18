package org.onap.vid.aai.model;

import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;


public class RelationshipTest {

    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(Relationship.class, hasValidGettersAndSetters());
    }
}