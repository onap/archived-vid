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


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestTest {


    private Request request;

    private String propertyName = "testProperty";
    private String additionalProperty = "testAdditionalProperty";

    @BeforeMethod
    public void setUp() {
        request = new Request();
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(Request.class, hasValidGettersAndSettersExcluding("additionalProperties"));
    }

    @Test
    public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
        //	when
        request.setAdditionalProperty(propertyName,additionalProperty);

        //	then
        assertThat( request.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
    }

    @Test
    public void shouldProperlyConvertRelatedInstanceObjectToString() {
        //	given
        request.setFinishTime("100");
        request.setRequestId("testRequest");
        request.setAdditionalProperty(propertyName,additionalProperty);

        //	when
        String response = request.toString();

        //	then
        assertThat(response).contains(
                "[instanceIds=<null>," +
                        "requestDetails=<null>," +
                        "requestStatus=<null>," +
                        "finishTime="+100+"," +
                        "requestId=testRequest," +
                        "requestScope=<null>," +
                        "requestType=<null>," +
                        "startTime=<null>," +
                        "additionalProperties={"+propertyName+"="+additionalProperty+"}]"
        );
    }

    @Test
    public void shouldProperlyCheckIfObjectsAreEqual() {
        //	given
        Request sameRequest = new Request();
        Request differentRequest = new Request();

        request.setFinishTime("100");
        sameRequest.setFinishTime("100");
        request.setRequestId("testRequest");
        sameRequest.setRequestId("testRequest");

        //	when
        boolean sameResponse = request.equals(request);
        boolean equalResponse = request.equals(sameRequest);
        boolean differentResponse = request.equals(differentRequest);
        boolean differentClassResponse = request.equals("RelatedInstance");

        //	then
        assertThat(sameResponse).isEqualTo(true);
        assertThat(equalResponse).isEqualTo(true);

        assertThat(differentResponse).isEqualTo(false);
        assertThat(differentClassResponse).isEqualTo(false);
    }
}
