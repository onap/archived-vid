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
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.onap.vid.testUtils.TestUtils.allPropertiesOf;

import org.onap.portalsdk.core.domain.support.DomainVo;
import org.testng.annotations.Test;

public class ServiceInfoTest {
    final private String[] excludedProperties = allPropertiesOf(DomainVo.class);

    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(ServiceInfo.class, hasValidGettersAndSetters());
    }

    @Test
    public void shouldHaveValidBeanConstructor() {
        assertThat(ServiceInfo.class, hasValidBeanConstructor());
    }

    @Test
    public void shouldHaveValidBeanHashCode() {
        assertThat(ServiceInfo.class, hasValidBeanHashCodeExcluding(excludedProperties));
    }

    @Test
    public void shouldHaveValidBeanEquals() {
        assertThat(ServiceInfo.class, hasValidBeanEqualsExcluding(excludedProperties));
    }

}


