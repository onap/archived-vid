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

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.SubscriberList;

@RunWith(MockitoJUnitRunner.class)
public class AaiOverTLSClientTest {

    private static final String SEARCH_NODES_QUERY_SEARCH_NODE_TYPE = "search/nodes-query?search-node-type=generic-vnf&filter=vnf-name:EQUALS:name";
    private static final String SUBSCRIBERS = "business/customers?subscriber-type=INFRA&depth=0";
    private AaiOverTLSClient aaiRestClient;

    @Mock
    private SyncRestClient syncRestClient;
    @Mock
    private AaiOverTLSPropertySupplier propertySupplier;

    @Before
    public void setUp() {
        aaiRestClient = new AaiOverTLSClient(syncRestClient,  propertySupplier);
    }

    @Test
    public void testSearchNodeTypeByName() {
        mockPropertyReader();

        aaiRestClient.searchNodeTypeByName("name", ResourceType.GENERIC_VNF);
        Mockito.verify(syncRestClient).get(Matchers.contains(SEARCH_NODES_QUERY_SEARCH_NODE_TYPE),
            Matchers.eq(getHeaders()), Matchers.eq(Collections.emptyMap()), Matchers.eq(AaiNodeQueryResponse.class));
    }

    @Test
    public void  testGetAllSubscribers(){
        mockPropertyReader();

        aaiRestClient.getAllSubscribers();
        Mockito.verify(syncRestClient).get(Matchers.contains(SUBSCRIBERS),
            Matchers.eq(getHeaders()), Matchers.eq(Collections.emptyMap()), Matchers.eq(SubscriberList.class));
    }

    private void mockPropertyReader() {
        Mockito.when(propertySupplier.getPassword()).thenReturn("Pass");
        Mockito.when(propertySupplier.getUsername()).thenReturn("User");
        Mockito.when(propertySupplier.getRequestId()).thenReturn("1");
        Mockito.when(propertySupplier.getRandomUUID()).thenReturn("2");
    }

    private Map<String,String> getHeaders(){
        return ImmutableMap.<String, String>builder().put("Authorization", "Basic VXNlcjpQYXNz").
            put("X-FromAppId", "VidAaiController").put("Accept", "application/json").put("X-ECOMP-RequestID", "1").
            put("X-TransactionId", "2").put("Content-Type", "application/json").build();
    }

}