/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.mso.rest;

import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestStatusTest {

    private RequestStatus RequestStatus;

    private String propertyName = "testProperty";
    private String additionalProperty = "testAdditionalProperty";

    @BeforeMethod
    public void setUp() {
        RequestStatus = new RequestStatus();
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(RequestStatus.class, hasValidGettersAndSettersExcluding("additionalProperties"));
    }

    @Test
    public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
        //	when
        RequestStatus.setAdditionalProperty(propertyName,additionalProperty);

        //	then
        AssertionsForClassTypes.assertThat( RequestStatus.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
    }

    @Test
    public void shouldProperlyCheckIfObjectsAreEqual() {
        assertThat(RequestStatus.class, hasValidBeanEqualsExcluding("additionalProperties"));
    }

    @Test
    public void shouldProperlyConvertRelatedInstanceObjectToString() {
        //	given
        RequestStatus.setAdditionalProperty(propertyName,additionalProperty);
        RequestStatus.setRequestState("testState");
        RequestStatus.setTimestamp("100");

        //	when
        String response = RequestStatus.toString();

        //	then
        System.out.println(response);
        AssertionsForClassTypes.assertThat(response).contains(
               "percentProgress=<null>," +
                       "requestState=testState," +
                       "statusMessage=<null>," +
                       "timestamp=100," +
                       "wasRolledBack=<null>," +
                       "additionalProperties={"+propertyName+"="+additionalProperty+"}]"
        );
    }

}
