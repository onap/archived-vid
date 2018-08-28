///*-
// * ============LICENSE_START=======================================================
// * VID
// * ================================================================================
// * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
// * ================================================================================
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * ============LICENSE_END=========================================================
// */
//
//package org.onap.vid.asdc.rest;
//
//import io.joshworks.restclient.http.HttpResponse;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.onap.vid.asdc.AsdcCatalogException;
//import org.onap.vid.asdc.beans.Service;
//import org.onap.vid.client.SyncRestClient;
//
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.util.UUID;
//
//import static org.hamcrest.CoreMatchers.notNullValue;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.core.Is.is;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyMapOf;
//import static org.mockito.Matchers.matches;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SdcRestClientTest {
//
//    private static final String SAMPLE_SERVICE_NAME = "sampleService";
//    private static final String SAMPLE_BASE_URL = "baseUrl";
//    private static final String SAMPLE_AUTH = "sampleAuth";
//    private static final String METADATA_URL_REGEX = ".*sdc/v1/catalog/services/%s/metadata";
//    private static final String MODEL_URL_REGEX = ".*sdc/v1/catalog/services/%s/toscaModel";
//
//
//    @Mock
//    private SyncRestClient mockedSyncRestClient;
//
//    @Mock
//    private HttpResponse<Object> httpResponse;
//
//    @Mock
//    private HttpResponse<InputStream> httpStreamResponse;
//
//    @Mock
//    private InputStream inputStream;
//
//    private UUID randomId;
//
//    private Service sampleService;
//
//    private SdcRestClient restClient;
//
//
//    @Before
//    public void setUp() {
//        randomId = UUID.randomUUID();
//        sampleService = createTestService();
//        restClient = new SdcRestClient(SAMPLE_BASE_URL, SAMPLE_AUTH, mockedSyncRestClient);
//    }
//
//
//    @Test
//    public void shouldReturnServiceForGivenUUID() throws AsdcCatalogException {
//        String url = String.format(METADATA_URL_REGEX, randomId);
//        when(mockedSyncRestClient.get(matches(url), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), any())).thenReturn(httpResponse);
//        when(httpResponse.getBody()).thenReturn(sampleService);
//
//        Service service = restClient.getService(randomId);
//
//
//        assertThat(service, is(sampleService));
//    }
//
//    @Test(expected = AsdcCatalogException.class)
//    public void shouldRaiseAsdcExceptionWhenClientFails() throws AsdcCatalogException {
//        String url = String.format(METADATA_URL_REGEX, randomId);
//        when(mockedSyncRestClient.get(matches(url), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), any())).thenThrow(new RuntimeException());
//
//        restClient.getService(randomId);
//    }
//
//
//    @Test
//    public void shouldProperlyDownloadAndCopyToscaArtifact() throws AsdcCatalogException {
//        String url = String.format(MODEL_URL_REGEX, randomId);
//        when(mockedSyncRestClient.getStream(matches(url), any(), any())).thenReturn(httpStreamResponse);
//        when(httpStreamResponse.getBody()).thenReturn(inputStream);
//
//
//        Path serviceToscaModel = restClient.getServiceToscaModel(randomId);
//
//
//        assertThat(serviceToscaModel, notNullValue());
//        assertThat(serviceToscaModel.toFile().isFile(), is(true));
//
//        serviceToscaModel.toFile().deleteOnExit();
//    }
//
//    @Test(expected = AsdcCatalogException.class)
//    public void shouldRaiseAsdcExceptionWhenDownloadFails() throws AsdcCatalogException {
//        String url = String.format(MODEL_URL_REGEX, randomId);
//        when(mockedSyncRestClient.getStream(matches(url), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class))).thenThrow(new RuntimeException());
//
//
//        restClient.getServiceToscaModel(randomId);
//    }
//
//
//    private Service createTestService() {
//        Service service = new Service();
//        service.setInvariantUUID(randomId.toString());
//        service.setUuid(randomId.toString());
//        service.setName(SAMPLE_SERVICE_NAME);
//        return service;
//    }
//
//}