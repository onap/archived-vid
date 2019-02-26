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

package org.onap.vid.aai.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class AaiGetPnfResponseTest {

    private AaiGetPnfResponse aaiGetPnfResponse;

    @Before
    public void setUp(){
        aaiGetPnfResponse = new AaiGetPnfResponse();
        aaiGetPnfResponse.results = new ArrayList<>();
        aaiGetPnfResponse.setAdditionalProperty("key1", "value1");
        aaiGetPnfResponse.setAdditionalProperty("key2", "value2");
    }

    @Test
    public void shouldHaveValidGettersAndSetters() throws IOException {
        String result = new ObjectMapper().writeValueAsString(aaiGetPnfResponse);
        assertThat(result, containsString("key1"));
        assertThat(result, containsString("value2"));
        assertThat(result, containsString("key2"));
        assertThat(result, containsString("value2"));
    }

    @Test
    public void shouldHaveValidToString(){
        assertThat(aaiGetPnfResponse.toString(),
                equalTo("AaiGetPnfResponse{results=[], additionalProperties={key1=value1, key2=value2}}"));
    }

}
