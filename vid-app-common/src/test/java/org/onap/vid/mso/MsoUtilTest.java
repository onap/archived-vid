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

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

import io.joshworks.restclient.http.HttpResponse;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        HttpResponse<String> httpResponse = TestUtils.createTestHttpResponse(SC_OK, null);
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
        HttpResponse<String> httpResponse = TestUtils.createTestHttpResponse(SC_OK, entity);
        // when
        MsoResponseWrapper result = MsoUtil.wrapResponse(httpResponse);
        // then
        assertThat(result.getEntity()).isEqualTo(entity);
        assertThat(result.getStatus()).isEqualTo(SC_OK);
    }

    @DataProvider
    public static Object[][] formatExceptionAdditionalInfo() {
        return new Object[][]{
            {"message", "Http Code:400, message"},

            {null, "Http Code:400"},

            {"{\"requestError\":{\"serviceException\":{\"messageId\":\"SVC0002\",\"text\":\"message\"}}}",
                "Http Code:400, \"messageId\":\"SVC0002\",\"text\":\"message\""},

            {"{\"validJson\": \"Error: message\"}", "Http Code:400, {\"validJson\": \"Error: message\"}"},

            {"{\"serviceException\":{\"messageId\":\"SVC0002\",\"text\":\"Error: message\"}}",
                "Http Code:400, \"messageId\":\"SVC0002\",\"text\":\"Error: message\""},
        };
    }

    @Test(dataProvider = "formatExceptionAdditionalInfo")
    public void formatExceptionAdditionalInfo_payloadWithError400_doNotReturnNull(String payload, String expected) {
        assertThat(MsoUtil.formatExceptionAdditionalInfo(400, payload))
            .isEqualTo(expected);
    }
}
