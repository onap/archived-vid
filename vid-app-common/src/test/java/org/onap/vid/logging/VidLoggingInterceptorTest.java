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

package org.onap.vid.logging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.logging.VidLoggingInterceptor.INBOUND_INVO_ID;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.logging.filter.base.SimpleHashMap;
import org.onap.logging.filter.base.SimpleMap;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.logging.ref.slf4j.ONAPLogConstants.MDCs;
import org.onap.vid.controller.ControllersUtils;
import org.slf4j.MDC;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VidLoggingInterceptorTest {

    private VidLoggingInterceptor interceptor;

    @Mock
    private ControllersUtils controllersUtils;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        interceptor = new VidLoggingInterceptor(controllersUtils);
        MDC.clear();
    }

    @Test
    public void testAdditionalPreHandling() {

        //given
        final String invoID = "987";
        MDC.put(ONAPLogConstants.MDCs.INVOCATION_ID, invoID);
        MDC.put(MDCs.PARTNER_NAME, "wrongPartnerName");
        final String myUserId = "myUserId";
        when(controllersUtils.extractUserId(any(HttpServletRequest.class)))
            .thenReturn(myUserId);

        //when
        interceptor.additionalPreHandling(mock(HttpServletRequest.class));

        //then
        assertEquals(MDC.get(INBOUND_INVO_ID), invoID);
        assertEquals(MDC.get(MDCs.PARTNER_NAME), myUserId);
    }

    @Test
    public void whenNoUserId_previousPartnerNameIsPreserved() {
        //given
        final String prevPartnerName = "prevPartnerName";
        MDC.put(MDCs.PARTNER_NAME, prevPartnerName);
        final HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        when(controllersUtils.extractUserId(any(HttpServletRequest.class)))
            .thenReturn("");

        //when
        interceptor.additionalPreHandling(mockedRequest);

        //then
        assertEquals(MDC.get(MDCs.PARTNER_NAME), prevPartnerName);

    }

    @Test
    public void givenNotValidAuthorizationHeader_whenGetBasicAuthUserName_noExceptionIsThrown() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Authorization","abcdefghi");
        SimpleMap headers = new SimpleHashMap(hashMap);
        assertNull(interceptor.getBasicAuthUserName(headers));
    }

}
