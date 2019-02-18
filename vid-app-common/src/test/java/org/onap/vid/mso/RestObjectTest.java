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

package org.onap.vid.mso;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.Assertions.assertThat;

public class RestObjectTest {

    private RestObject restObject;

    @BeforeSuite
    private void setUp() {
        restObject = new RestObject();
    }

    @Test
    public void shouldHaveValidGettersAndSetters(){
        MatcherAssert.assertThat(RestObject.class, hasValidGettersAndSettersExcluding("t"));
    }

    @Test
    public void shouldHaveValidGetterAndSetterForBody() {
        //  given
        String testString = "set/get_testString";

        //  when
        restObject.set(testString);

        //  then
        assertThat(testString).isSameAs(restObject.get());
    }

    @Test
    public void shouldProperlyCopyRestObject() {
        //  given
        MsoResponseWrapper testResponseWraper = new MsoResponseWrapper();
        String rawTestString = "rawTestString";
        int statusCode = 404;

        RestObject restObjectToCopyFrom = new RestObject<>();
        restObjectToCopyFrom.set(testResponseWraper);
        restObjectToCopyFrom.setRaw(rawTestString);
        restObjectToCopyFrom.setStatusCode(statusCode);

        //  when
        restObject.copyFrom(restObjectToCopyFrom);

        //  then
        assertThat(restObject).isEqualToComparingFieldByField(restObjectToCopyFrom);
    }

    @Test
    public void shouldProperlyConvertRestObjectToString() {
        //  given
        String testString = "testString";
        String rawTestString = "rawTestString";
        int statusCode = 202;

        restObject.set(testString);
        restObject.setRaw(rawTestString);
        restObject.setStatusCode(statusCode);

        String properString = "RestObject{t=testString, rawT=rawTestString, statusCode=202}";

        //  when
        String toStringResponse = restObject.toString();

        //  then
        assertThat(toStringResponse).isEqualTo(properString);
    }
}
