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

import static org.mockito.Mockito.when;

import io.joshworks.restclient.http.HttpResponse;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class MsoResponseWrapper2Test {

    @Mock
    RestObject<String> msoResponse;

    @Mock
    HttpResponse<String> httpResponse;

    private MsoResponseWrapper2<String> responseWrapper;

    private int status = 202;
    private String entity = "testEntity";
    private String rawString = "testRawString";

    @BeforeClass
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldProperlyCreateInstanceFromRestObject() {
        //  given
        when(msoResponse.getStatusCode()).thenReturn(status);
        when(msoResponse.get()).thenReturn(entity);
        when(msoResponse.getRaw()).thenReturn(rawString);

        //  when
        responseWrapper = new MsoResponseWrapper2<>(msoResponse);

        //  then
        assertThat(responseWrapper.getStatus()).isEqualTo(status);
        assertThat(responseWrapper.getEntity()).isEqualTo(entity);
        assertThat(responseWrapper.getResponse()).isEqualTo("{\"status\":"+status+",\"entity\":\""+entity+"\"}");
    }

    @Test
    public void shouldProperlyCreateInstanceFromHttpResponse() {
        //  given
        when(httpResponse.getStatus()).thenReturn(status);
        when(httpResponse.getBody()).thenReturn(entity);

        //  when
        responseWrapper = new MsoResponseWrapper2<>(httpResponse);

        //  then
        assertThat(responseWrapper.getStatus()).isEqualTo(status);
        assertThat(responseWrapper.getEntity()).isEqualTo(entity);
        assertThat(responseWrapper.getResponse()).isEqualTo("{\"status\":"+status+",\"entity\":\""+entity+"\"}");
    }

    @Test
    public void shouldProperlyCreateInstanceFromStatusAndEntity() {
        //  when
        responseWrapper = new MsoResponseWrapper2<>(status,entity);

        //  then
        assertThat(responseWrapper.getStatus()).isEqualTo(status);
        assertThat(responseWrapper.getEntity()).isEqualTo(entity);
        assertThat(responseWrapper.getResponse()).isEqualTo("{\"status\":"+status+",\"entity\":\""+entity+"\"}");
    }

}