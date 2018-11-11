/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.aai;

import io.joshworks.restclient.http.HttpResponse;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;

public interface AaiOverTLSClientInterface {

    class URIS {
        static final String NODE_TYPE_BY_NAME = "search/nodes-query?search-node-type=%s&filter=%s:EQUALS:%s";
    }

    class HEADERS {
        static final String TRANSACTION_ID_HEADER = "X-TransactionId";
        static final String FROM_APP_ID_HEADER = "X-FromAppId";
        static final String CONTENT_TYPE = "Content-Type";
        static final String REQUEST_ID = SystemProperties.ECOMP_REQUEST_ID;
        static final String ACCEPT = "Accept";
    }

    void setUseClientCert(boolean useClientCert);

    HttpResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type);

}
