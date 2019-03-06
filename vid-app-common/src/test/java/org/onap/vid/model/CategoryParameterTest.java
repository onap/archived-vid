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

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.onap.vid.testUtils.TestUtils.allPropertiesOf;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.onap.portalsdk.core.domain.support.DomainVo;

public class CategoryParameterTest {


    final private String[] excludedProperties = allPropertiesOf(DomainVo.class);

    private ImmutableSet<CategoryParameterOption> optionsWithRefTo(CategoryParameter categoryParameter) {
        return ImmutableSet.of(
            new CategoryParameterOption("appId1", "name1", categoryParameter),
            new CategoryParameterOption("appId2", "name2", categoryParameter)
        );
    }

    @Test
    public void shouldHaveValidGettersAndSetters() {
        assertThat(CategoryParameter.class, hasValidGettersAndSettersExcluding("options"));
    }

    @Test
    public void testSetAndGetOptions() {
        CategoryParameter testSubject = new CategoryParameter();

        Set<CategoryParameterOption> options = optionsWithRefTo(testSubject);

        testSubject.setOptions(options);
        assertThat(testSubject.getOptions(), containsInAnyOrder(optionsWithRefTo(testSubject).toArray()));
    }

    @Test
    public void shouldHaveValidBeanHashCodeWithCycleReference() {
        assertThat(CategoryParameter.class,
            hasValidBeanHashCodeExcluding(ArrayUtils.addAll(new String[]{"options"}, excludedProperties)));
    }

    @Test
    public void hashCodeShouldNotExplodeWhenCycleReference() {
        CategoryParameter testSubject = new CategoryParameter();
        Set<CategoryParameterOption> options = optionsWithRefTo(testSubject);
        testSubject.setOptions(options);

        testSubject.hashCode(); // don't stackOverflow
    }

    @Test
    public void shouldHaveValidBeanEquals() {
        assertThat(CategoryParameter.class, hasValidBeanEqualsExcluding(excludedProperties));
    }

}
