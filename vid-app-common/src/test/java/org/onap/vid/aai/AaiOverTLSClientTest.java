/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia Intellectual Property. All rights reserved.
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
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.mockito.Answers;
import org.mockito.Mock;

import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AaiOverTLSClientTest {

    private static final String SEARCH_NODES_QUERY_SEARCH_NODE_TYPE = "nodes/generic-vnfs?vnf-name=name";
    private static final String SUBSCRIBERS = "business/customers?subscriber-type=INFRA&depth=0";
    private AaiOverTLSClient aaiRestClient;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private SyncRestClient syncRestClient;
    @Mock
    private AaiOverTLSPropertySupplier propertySupplier;

    @Mock
    private HttpResponse<SubscriberList> response;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        aaiRestClient = new AaiOverTLSClient(syncRestClient,  propertySupplier);
    }

    @Test
    public void testIsNodeTypeExistsByName() {
        mockPropertyReader();

        aaiRestClient.isNodeTypeExistsByName("name", ResourceType.GENERIC_VNF);
        verify(syncRestClient).get(contains(SEARCH_NODES_QUERY_SEARCH_NODE_TYPE),
            eq(getHeaders()), eq(Collections.emptyMap()));
    }

    @Test
    public void  testGetAllSubscribers(){
        mockPropertyReader();

        aaiRestClient.getAllSubscribers();
        verify(syncRestClient).get(contains(SUBSCRIBERS),
            eq(getHeaders()), eq(Collections.emptyMap()), eq(SubscriberList.class));
    }


    @Test
    public void probeMechanismShouldReturnAllSubscribers() {
        mockPropertyReader();
        List<Subscriber> subscribers = Lists.newArrayList(new Subscriber());

        SubscriberList subscriberList = new SubscriberList(subscribers);
        InputStream json = IOUtils.toInputStream(new Gson().toJson(subscriberList));
        when(syncRestClient.get(contains(SUBSCRIBERS), eq(getHeaders()), eq(Collections.emptyMap()),
                eq(SubscriberList.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(HttpStatus.OK.value());
        when(response.getRawBody()).thenReturn(json);
        when(response.isSuccessful()).thenReturn(true);


        ExternalComponentStatus externalComponentStatus = aaiRestClient.probeComponent();

        assertThat(externalComponentStatus.isAvailable()).isTrue();
        assertThat(externalComponentStatus.getComponent()).isEqualTo(ExternalComponentStatus.Component.AAI);
    }

    @Test
    public void probeMechanismShouldHandleExceptionProperly(){
        mockPropertyReader();
        when(syncRestClient.get(contains(SUBSCRIBERS), eq(getHeaders()), eq(Collections.emptyMap()),
                eq(SubscriberList.class))).thenThrow(new RuntimeException("call failed"));

        ExternalComponentStatus externalComponentStatus = aaiRestClient.probeComponent();

        assertThat(externalComponentStatus.isAvailable()).isFalse();
        assertThat(externalComponentStatus.getComponent()).isEqualTo(ExternalComponentStatus.Component.AAI);
        assertThat(externalComponentStatus.getMetadata().getDescription()).containsSequence("call failed");
    }

    private void mockPropertyReader() {
        when(propertySupplier.getPassword()).thenReturn("Pass");
        when(propertySupplier.getUsername()).thenReturn("User");
        when(propertySupplier.getRequestId()).thenReturn("1");
        when(propertySupplier.getRandomUUID()).thenReturn("2");
    }

    private Map<String,String> getHeaders(){
        return ImmutableMap.<String, String>builder().put("Authorization", "Basic VXNlcjpQYXNz").
            put("X-FromAppId", "VidAaiController").put("Accept", "application/json").put("X-ECOMP-RequestID", "1").
            put("X-TransactionId", "2").put("Content-Type", "application/json").build();
    }
}
