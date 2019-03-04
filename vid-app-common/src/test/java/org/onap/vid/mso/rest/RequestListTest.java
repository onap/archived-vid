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

package org.onap.vid.mso.rest;

import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestListTest {

    private RequestList requestList;

    private String propertyName = "testProperty";
    private String additionalProperty = "testAdditionalProperty";

    @BeforeMethod
    public void setUp() {
        requestList = new RequestList();
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(RequestList.class, hasValidGettersAndSettersExcluding("additionalProperties"));
    }

    @Test
    public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
        //	when
        requestList.setAdditionalProperty(propertyName,additionalProperty);

        //	then
        AssertionsForClassTypes.assertThat( requestList.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
    }

    @Test
    public void shouldProperlyCheckIfObjectsAreEqual() {
        //	given
        RequestList sameRequestList = new RequestList();
        RequestList differentRequestList= new RequestList();

        List<RequestWrapper> testList = new LinkedList<>();
        testList.add(new RequestWrapper());

        requestList.setRequestList(testList);
        sameRequestList.setRequestList(testList);

        //	when
        boolean sameResponse = requestList.equals(requestList);
        boolean equalResponse = requestList.equals(sameRequestList);
        boolean differentResponse = requestList.equals(differentRequestList);
        boolean differentClassResponse = requestList.equals("RequestList");

        //	then
        assertThat(sameResponse).isEqualTo(true);
        assertThat(equalResponse).isEqualTo(true);

        assertThat(differentResponse).isEqualTo(false);
        assertThat(differentClassResponse).isEqualTo(false);
    }

    @Test
    public void shouldProperlyConvertRelatedInstanceObjectToString() {
        //	given
        requestList.setAdditionalProperty(propertyName,additionalProperty);

        //	when
        String response = requestList.toString();

        //	then
        System.out.println(response);
        AssertionsForClassTypes.assertThat(response).contains(
                        "additionalProperties={"+propertyName+"="+additionalProperty+"}]"
        );
    }
}
