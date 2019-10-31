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

import static org.onap.vid.logging.LoggingFilterHelper.updateInvocationIDInMdcWithHeaderValue;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import org.onap.logging.filter.base.MetricLogClientFilter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;

public class JaxRsMetricLogClientFilter extends MetricLogClientFilter {

    @Override
    protected void additionalPre(ClientRequestContext clientRequestContext, MultivaluedMap<String, Object> stringObjectMultivaluedMap) {
        updateInvocationIDInMdcWithHeaderValue(
            ()->(String)stringObjectMultivaluedMap.getFirst(ONAPLogConstants.Headers.INVOCATION_ID)
        );
    }
}
