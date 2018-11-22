/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
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

import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.ACCEPT;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.CONTENT_TYPE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.FROM_APP_ID_HEADER;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.REQUEST_ID;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.TRANSACTION_ID_HEADER;

import io.joshworks.restclient.http.HttpResponse;
import io.vavr.collection.HashMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.aai.util.AAIProperties;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.model.SubscriberList;

public class AaiOverTLSClient implements AaiOverTLSClientInterface {

    private final AaiOverTLSPropertySupplier propertySupplier;
    private SyncRestClientInterface syncRestClient;
    private boolean useClientCert;
    private static final String CALLER_APP_ID = "VidAaiController";
    private String urlBase;

    public AaiOverTLSClient(SyncRestClientInterface syncRestClient, AaiOverTLSPropertySupplier propertySupplier) {
        this(syncRestClient, propertySupplier, SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL));
    }

    AaiOverTLSClient(SyncRestClientInterface syncRestClient, AaiOverTLSPropertySupplier propertySupplier, String baseUrl) {
        this.syncRestClient = syncRestClient;
        this.propertySupplier = propertySupplier;
        this.urlBase = baseUrl;
    }

    @Override
    public void setUseClientCert(boolean useClientCert) {
        this.useClientCert = useClientCert;
    }

    @Override
    public HttpResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type) {
        String uri = urlBase + String.format(URIS.NODE_TYPE_BY_NAME, type.getAaiFormat(), type.getNameFilter(), name);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), AaiNodeQueryResponse.class);
    }

    @Override
    public HttpResponse<SubscriberList> getAllSubscribers() {
        String uri = urlBase + String.format(URIS.SUBSCRIBERS, 0);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), SubscriberList.class);
    }

    private Map<String, String> getRequestHeaders() {
        Map<String, String> result = HashMap.of(
                TRANSACTION_ID_HEADER, propertySupplier.getRandomUUID(),
                FROM_APP_ID_HEADER, CALLER_APP_ID,
                CONTENT_TYPE, MediaType.APPLICATION_JSON,
                REQUEST_ID, propertySupplier.getRequestId(),
                ACCEPT, MediaType.APPLICATION_JSON)
                .toJavaMap();
        result.putAll(getAuthorizationHeader());
        return result;
    }

    private Map<String, String> getAuthorizationHeader() {
        if (!useClientCert) {
            String vidUsername = propertySupplier.getUsername();
            String vidPassword = propertySupplier.getPassword();
            String encoded = Base64.getEncoder()
                    .encodeToString((vidUsername + ":" + vidPassword).getBytes(StandardCharsets.UTF_8));
            return HashMap.of("Authorization", "Basic " + encoded).toJavaMap();
        }
        return HashMap.<String, String>empty().toJavaMap();
    }

}
