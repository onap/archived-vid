/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

import org.mockito.Mock;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;


public class MsoResponseWrapperTest {

    private static final String PROPER_RESPONSE = "{ \"status\": 1, \"entity\": testEntity}";
    private static final String PROPER_RESPONSE_WITH_NO_ENTITY = "{ \"status\": 1, \"entity\": \"\"}";
    private static final String PROPER_TO_STRING = "[status=1,entity=testEntity]";

    @Mock
    private Response response;

    private MsoResponseWrapper responseWrapper;

    @BeforeSuite
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldHaveValidGettersAndSetters(){
        assertThat(MsoResponseWrapper.class, hasValidGettersAndSettersExcluding("response"));
    }

    @Test
    public void shouldProperlyConstructResponseWrapperWithParameters(){
        responseWrapper = new MsoResponseWrapper(1,"testEntity");

        assertThat(responseWrapper.getStatus()).isEqualTo(1);
        assertThat(responseWrapper.getEntity()).isEqualTo("testEntity");
    }

    @Test
    public void shouldProperlyConstructResponseWrapperFromResponse(){
        when(response.getStatus()).thenReturn(1);
        when(response.readEntity(String.class)).thenReturn("testEntity");

        responseWrapper = new MsoResponseWrapper(response);

        assertThat(responseWrapper.getStatus()).isEqualTo(1);
        assertThat(responseWrapper.getEntity()).isEqualTo("testEntity");
    }

    @Test
    public void shouldProperlyGetResponseWithEmptyEntity(){
        responseWrapper = new MsoResponseWrapper();
        responseWrapper.setStatus(1);

        assertThat(responseWrapper.getResponse()).isEqualToIgnoringWhitespace(PROPER_RESPONSE_WITH_NO_ENTITY);
    }

    @Test
    public void shouldProperlyGetResponse(){
        responseWrapper = new MsoResponseWrapper(1,"testEntity");

        assertThat(responseWrapper.getResponse()).isEqualToIgnoringWhitespace(PROPER_RESPONSE);
    }

    @Test
    public void shouldProperlyConvertToString(){
        responseWrapper = new MsoResponseWrapper(1,"testEntity");

        assertThat(responseWrapper.toString()).endsWith(PROPER_TO_STRING);
    }


}
