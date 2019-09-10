/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.asdc.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.testUtils.TestUtils.mockGetRawBodyWithStringBody;
import static org.testng.AssertJUnit.fail;

import io.joshworks.restclient.http.HttpResponse;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.utils.Logging;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SdcRestClientTest {

    private static final String SAMPLE_SERVICE_NAME = "sampleService";
    private static final String SAMPLE_BASE_URL = "baseUrl";
    private static final String SAMPLE_AUTH = "sampleAuth";
    private static final String METADATA_URL_REGEX = ".*sdc/v1/catalog/services/%s/metadata";
    private static final String MODEL_URL_REGEX = ".*sdc/v1/catalog/services/%s/toscaModel";


    @Mock
    private SyncRestClient mockedSyncRestClient;

    @Mock
    private HttpResponse<Object> httpResponse;

    @Mock
    private HttpResponse<InputStream> httpStreamResponse;

    @Mock
    private HttpResponse<String> httpStringResponse;

    @Mock
    private InputStream inputStream;

    @Mock
    private Logging loggingService;

    private UUID randomId;

    private Service sampleService;

    private SdcRestClient restClient;


    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        randomId = UUID.randomUUID();
        sampleService = createTestService();
        restClient = new SdcRestClient(SAMPLE_BASE_URL, SAMPLE_AUTH, mockedSyncRestClient, loggingService);
    }


    @Test
    public void shouldReturnServiceForGivenUUID() throws AsdcCatalogException {
        String url = String.format(METADATA_URL_REGEX, randomId);
        when(mockedSyncRestClient.get(matches(url), anyMap(), anyMap(), any())).thenReturn(httpResponse);
        when(httpResponse.getBody()).thenReturn(sampleService);

        Service service = restClient.getService(randomId);


        assertThat(service, is(sampleService));
    }

    @Test( expectedExceptions = AsdcCatalogException.class)
    public void shouldRaiseAsdcExceptionWhenClientFails() throws AsdcCatalogException {
        String url = String.format(METADATA_URL_REGEX, randomId);
        when(mockedSyncRestClient.get(matches(url), anyMap(), anyMap(), any())).thenThrow(new RuntimeException());

        restClient.getService(randomId);
    }


    @Test
    public void shouldProperlyDownloadAndCopyToscaArtifact() throws AsdcCatalogException {
        String url = String.format(MODEL_URL_REGEX, randomId);
        when(mockedSyncRestClient.getStream(matches(url), any(), any())).thenReturn(httpStreamResponse);
        when(httpStreamResponse.getBody()).thenReturn(inputStream);


        Path serviceToscaModel = restClient.getServiceToscaModel(randomId);


        assertThat(serviceToscaModel, notNullValue());
        assertThat(serviceToscaModel.toFile().isFile(), is(true));

        serviceToscaModel.toFile().deleteOnExit();
    }

    @Test(expectedExceptions = AsdcCatalogException.class)
    public void shouldRaiseAsdcExceptionWhenDownloadFails() throws AsdcCatalogException {
        String url = String.format(MODEL_URL_REGEX, randomId);
        when(mockedSyncRestClient.getStream(matches(url), anyMap(), anyMap())).thenThrow(new RuntimeException());


        restClient.getServiceToscaModel(randomId);
    }

    @Test
    public void shouldCallSDCHealthCheck() {
        when(mockedSyncRestClient.get(contains(AsdcClient.URIS.HEALTH_CHECK_ENDPOINT), anyMap(),
                eq(Collections.emptyMap()), eq(String.class))).thenReturn(httpStringResponse);


        HttpResponse<String> stringHttpResponse = restClient.checkSDCConnectivity();

        assertThat(httpStringResponse, is(stringHttpResponse));
    }

    private Service createTestService() {
        Service service = new Service();
        service.setInvariantUUID(randomId.toString());
        service.setUuid(randomId.toString());
        service.setName(SAMPLE_SERVICE_NAME);
        return service;
    }

    @DataProvider
    public static Object[][] javaxExceptions() {

        return new Object[][] {
            {NotFoundException.class, (Consumer<SyncRestClient>) restClient ->
                when(restClient.getStream(anyString(), anyMap(), anyMap())).thenThrow(
                    new NotFoundException("HTTP 404 Not Found"))
            },
            {ProcessingException.class, (Consumer<SyncRestClient>) restClient ->
                when(restClient.getStream(anyString(), anyMap(), anyMap())).thenThrow(
                    new ProcessingException("java.net.ConnectException: Connection refused: connect"))},
        };
    }


    @Test(dataProvider = "javaxExceptions")
    public void whenJavaxClientThrowException_then_getServiceToscaModelRethrowException(Class<? extends Throwable> expectedType, Consumer<SyncRestClient> setupMocks) throws Exception {
        /*
        Call chain is like:
            this test -> SdcRestClient ->  SdcRestClient -> joshworks client which return  joshworks HttpResponse

        In this test, *SdcRestClient* is under test (actual implementation is used), while SdcRestClient is
        mocked to return pseudo joshworks HttpResponse or - better - throw exceptions.
         */

        /// TEST:
        SyncRestClient syncRestClient = mock(SyncRestClient.class);
        setupMocks.accept(syncRestClient);

        try {
            new SdcRestClient(SAMPLE_BASE_URL, SAMPLE_AUTH, syncRestClient, loggingService).getServiceToscaModel(UUID.randomUUID());
        } catch (Exception e) {
            assertThat("root cause incorrect for " + ExceptionUtils.getStackTrace(e), ExceptionUtils.getRootCause(e), instanceOf(expectedType));
            return; //OK
        }

        fail("exception shall rethrown by getServiceToscaModel once javax client throw exception ");
    }

    @DataProvider
    public static Object[][] badResponses() {
        return new Object[][] {
            {(Consumer<HttpResponse>) response -> {
                when(response.getStatus()).thenReturn(404);
                mockGetRawBodyWithStringBody(response,"");},
                ""
            },
            {(Consumer<HttpResponse>) response -> {
                when(response.getStatus()).thenReturn(405);
                when(response.getRawBody()).thenThrow(ClassCastException.class);},
                ""
            },
            {(Consumer<HttpResponse>) response -> {
                when(response.getStatus()).thenReturn(500);
                mockGetRawBodyWithStringBody(response,"some message");},
                "some message"
            },
        };
    }

    @Test(dataProvider = "badResponses")
    public void whenJavaxClientReturnBadCode_then_getServiceToscaModelThrowException(Consumer<HttpResponse> setupMocks, String exceptedBody) throws Exception {
        /*
        Call chain is like:
            this test -> SdcRestClient ->  SdcRestClient -> joshworks client which return  joshworks HttpResponse

        In this test, *SdcRestClient* is under test (actual implementation is used), while SdcRestClient is
        mocked to return pseudo joshworks HttpResponse
         */

        HttpResponse<InputStream> mockResponse = mock(HttpResponse.class);
        SyncRestClient syncRestClient = mock(SyncRestClient.class);
        when(syncRestClient.getStream(anyString(), anyMap(), anyMap())).thenReturn(mockResponse);

        // prepare real RestfulAsdcClient (Under test)

        setupMocks.accept(mockResponse);

        try {
            new SdcRestClient(SAMPLE_BASE_URL, SAMPLE_AUTH, syncRestClient, loggingService).getServiceToscaModel(UUID.randomUUID());
        } catch (AsdcCatalogException e) {
            assertThat(e.getMessage(), containsString(String.valueOf(mockResponse.getStatus())));
            assertThat(e.getMessage(), containsString(exceptedBody));
            return; //OK
        }

        fail("exception shall be thrown by getServiceToscaModel once response contains error code ");
    }


}
