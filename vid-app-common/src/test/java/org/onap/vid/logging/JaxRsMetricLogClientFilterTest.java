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

import static org.testng.Assert.assertEquals;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.MDC;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JaxRsMetricLogClientFilterTest {

    JaxRsMetricLogClientFilter metricLogClientFilter;

    @BeforeMethod
    public void setup() {
        this.metricLogClientFilter = new JaxRsMetricLogClientFilter();
        MDC.clear();
    }

    @Test
    public void testAdditionalPre() {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add(ONAPLogConstants.Headers.INVOCATION_ID, "xyz");
        metricLogClientFilter.additionalPre(null, headers);
        assertEquals(MDC.get(ONAPLogConstants.MDCs.INVOCATION_ID), "xyz");
    }
}
