/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

import io.joshworks.restclient.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.assertj.core.api.Assertions.assertThat;

public class MsoUtilTest {

    @Test
    public void shouldWrapRestObject() {
        // given
        String entity = "entity";
        RestObject<String> restObject = new RestObject<>();
        restObject.set(entity);
        restObject.setStatusCode(SC_OK);
        // when
        MsoResponseWrapper result = MsoUtil.wrapResponse(restObject);
        // then
        assertThat(result.getEntity()).isEqualTo(entity);
        assertThat(result.getStatus()).isEqualTo(SC_OK);
    }

    @Test
    public void shouldWrapHttpResponse() throws Exception {
        // given
        HttpResponse<String> httpResponse = createTestHttpResponse(SC_OK, null);
        // when
        MsoResponseWrapper result = MsoUtil.wrapResponse(httpResponse);
        // then
        assertThat(result.getEntity()).isEqualTo(null);
        assertThat(result.getStatus()).isEqualTo(SC_OK);
    }

    @Test
    public void shouldWrapHttpResponseWithEntity() throws Exception {
        // given
        String entity = "entity";
        HttpResponse<String> httpResponse = createTestHttpResponse(SC_OK, entity);
        // when
        MsoResponseWrapper result = MsoUtil.wrapResponse(httpResponse);
        // then
        assertThat(result.getEntity()).isEqualTo(entity);
        assertThat(result.getStatus()).isEqualTo(SC_OK);
    }

    private HttpResponse<String> createTestHttpResponse(int statusCode, String entity) throws Exception {
        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        org.apache.http.HttpResponse response = factory.newHttpResponse(new BasicStatusLine(HTTP_1_1, statusCode, null), null);
        if (entity != null) {
            response.setEntity(new StringEntity(entity));
        }
        return new HttpResponse<>(response, String.class, null);
    }
}