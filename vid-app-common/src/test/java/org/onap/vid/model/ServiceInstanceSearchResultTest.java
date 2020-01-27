/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonNodeAbsent;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.apache.commons.lang3.ArrayUtils.toArray;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

public class ServiceInstanceSearchResultTest {

    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(ServiceInstanceSearchResult.class, hasValidGettersAndSetters());
    }

    @Test
    public void shouldHaveValidConstructor() {
        assertThat(ServiceInstanceSearchResult.class, hasValidBeanConstructor());
    }

    @Test
    public void shouldHaveValidEqualsAndHashCode() {
        String[] propertiesToEqualBy = toArray("serviceInstanceId");

        assertThat(ServiceInstanceSearchResult.class, allOf(
            hasValidBeanHashCodeFor(propertiesToEqualBy),
            hasValidBeanEqualsFor(propertiesToEqualBy))
        );
    }

    @Test
    public void subscriberId_shouldBeSerializedAsGlobalCustomerId() {
        ServiceInstanceSearchResult underTest = new ServiceInstanceSearchResult();
        underTest.setSubscriberId("example");

        assertThat(underTest, jsonPartEquals("globalCustomerId", "example"));
        assertThat(underTest, jsonNodeAbsent("subscriberId"));
    }

}
