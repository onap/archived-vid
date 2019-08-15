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

package org.onap.vid.services;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.ProcessingException;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class VidServiceImplTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    private AsdcClient asdcClientMock;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private ToscaParserImpl2 toscaParserMock;

    @Mock
    private FeatureManager featureManager;

    @Mock
    private HttpResponse<String> httpResponse;

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final UUID uuid3 = UUID.randomUUID();
    private final Map<UUID, Service> pseudoServiceByUuid = ImmutableMap.of(
            uuid1, mock(Service.class),
            uuid2, mock(Service.class),
            uuid3, mock(Service.class)
    );

    private final Map<Service, ServiceModel> pseudoModelByService =
            pseudoServiceByUuid.values().stream()
                    .collect(toMap(service -> service, service -> mock(ServiceModel.class)));
    private VidServiceImpl vidService;

    private ServiceModel expectedServiceModelForUuid(UUID uuid) {
        final ServiceModel serviceModel = pseudoModelByService.get(pseudoServiceByUuid.get(uuid));
        assertThat(serviceModel, is(not(nullValue())));
        return serviceModel;
    }

    @BeforeMethod
    public void initMocks() throws AsdcCatalogException, SdcToscaParserException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);

        vidService = new VidServiceImpl(asdcClientMock, toscaParserMock, featureManager);
        FieldUtils.writeField(vidService, "toscaParser", toscaParserMock, true);

        when(featureManager.isActive(Features.FLAG_SERVICE_MODEL_CACHE)).thenReturn(true);

        when(asdcClientMock.getService(any())).thenAnswer(invocation -> pseudoServiceByUuid.get(invocation.getArguments()[0]));
        when(toscaParserMock.makeServiceModel(any(), any())).thenAnswer(invocation -> pseudoModelByService.get(invocation.getArguments()[1]));
    }

    @Test
    public void whenGetServiceMultipleTimes_asdcIsCalledOnlyOnce() throws AsdcCatalogException {
        vidService.getService(uuid1.toString());
        vidService.getService(uuid1.toString());
        vidService.getService(uuid1.toString());

        verify(asdcClientMock, times(1)).getService(uuid1);
    }

    @Test
    public void whenGetServiceTwiceWithResetBetween_asdcIsCalledTwice() throws AsdcCatalogException {
        vidService.getService(uuid1.toString());
        vidService.invalidateServiceCache();
        vidService.getService(uuid1.toString());

        verify(asdcClientMock, times(2)).getService(uuid1);
    }

    @Test
    public void cache_saves_service_model_correctly() throws AsdcCatalogException {
        ServiceModel service1 = vidService.getService(uuid1.toString());
        ServiceModel service2 = vidService.getService(uuid1.toString());
        ServiceModel service3 = vidService.getService(uuid1.toString());

        assertThat(service1, sameInstance(expectedServiceModelForUuid(uuid1)));
        assertThat(service1, sameInstance(service2));
        assertThat(service1, sameInstance(service3));
    }

    @Test
    public void cache_provide_correct_serviceModel() throws AsdcCatalogException {
        ServiceModel service2 = vidService.getService(uuid2.toString());
        assertThat(service2, sameInstance(expectedServiceModelForUuid(uuid2)));

        ServiceModel service3 = vidService.getService(uuid3.toString());
        assertThat(service3, sameInstance(expectedServiceModelForUuid(uuid3)));

        UUID nonExisting = UUID.randomUUID();
        ServiceModel service4 = vidService.getService(nonExisting.toString());
        assertThat(service4, is(nullValue()));
    }

    @Test(expectedExceptions = AsdcCatalogException.class, expectedExceptionsMessageRegExp = "someMessage")
    public void whenAsdcClientThrowAsdcCatalogException_thenGetServiceAlsoThrowIt() throws AsdcCatalogException {
        when(asdcClientMock.getServiceToscaModel(any())).thenThrow(new AsdcCatalogException("someMessage"));
        vidService.getService(uuid1.toString());
    }

    @Test
    public void shouldCheckConnectionToSdc() {
        mockGoodSdcConnectivityResponse();

        ExternalComponentStatus externalComponentStatus = vidService.probeComponent();

        assertThat(externalComponentStatus.isAvailable(), is(true));
        assertThat(externalComponentStatus.getComponent(), is(ExternalComponentStatus.Component.SDC));
        HttpRequestMetadata metadata = (HttpRequestMetadata) externalComponentStatus.getMetadata();
        assertThat(metadata.getRawData(), is("sampleBody"));
    }

    private void mockGoodSdcConnectivityResponse() {
        when(asdcClientMock.checkSDCConnectivity()).thenReturn(httpResponse);
        when(httpResponse.isSuccessful()).thenReturn(true);
        when(httpResponse.getBody()).thenReturn("sampleBody");
    }

    @Test
    public void shouldProperlyHandleNotWorkingSDCConnection(){
        when(asdcClientMock.checkSDCConnectivity()).thenThrow(new RuntimeException("not working"));

        ExternalComponentStatus externalComponentStatus = vidService.probeComponent();

        assertThat(externalComponentStatus.isAvailable(), is(false));
        assertThat(externalComponentStatus.getMetadata().getDescription(),containsString("not working"));
    }

    @Test
    public void shouldNotProbeBySdcConnectionIfProbeUuidConfigured() throws Exception {
        TestUtils.testWithSystemProperty(
            "probe.sdc.model.uuid",
            UUID.randomUUID().toString(),
            ()->{
                mockGoodSdcConnectivityResponse(); //no mocking for probeSdcByGettingModel
                ExternalComponentStatus externalComponentStatus = vidService.probeComponent();
                assertThat(externalComponentStatus.isAvailable(), is(false));
            }
        );
    }


    @Test
    public void givenProbeUUID_whenAsdcClientReturnNormal_thenProbeComponentReturnAvailableAnswer() throws Exception {

        final UUID uuidForProbe = UUID.randomUUID();
        final HttpResponse<InputStream> mockResponse = mock(HttpResponse.class);
        responseSetupper(mockResponse, 200, new ByteArrayInputStream(RandomUtils.nextBytes(66000)));
        when(asdcClientMock.getServiceInputStream(eq(uuidForProbe), eq(true))).thenReturn(
            new HttpResponseWithRequestInfo(mockResponse, "my pretty url", HttpMethod.GET)
        );

        TestUtils.testWithSystemProperty(
            "probe.sdc.model.uuid",
            uuidForProbe.toString(),
            ()-> {

                ExternalComponentStatus sdcComponentStatus = vidService.probeComponent();
                assertTrue(sdcComponentStatus.isAvailable());
                assertThat(sdcComponentStatus.getComponent(), is(ExternalComponentStatus.Component.SDC));
                assertThat(sdcComponentStatus.getMetadata().getDescription(), equalTo("OK"));
                assertThat(sdcComponentStatus.getMetadata(), instanceOf(HttpRequestMetadata.class));

                HttpRequestMetadata componentStatusMetadata = ((HttpRequestMetadata) sdcComponentStatus.getMetadata());
                assertThat(componentStatusMetadata.getHttpMethod(), equalTo(HttpMethod.GET));
                assertThat(componentStatusMetadata.getHttpCode(), equalTo(200));
                assertThat(componentStatusMetadata.getUrl(), equalTo("my pretty url"));
            }
        );
    }

    private static void responseSetupper(HttpResponse<InputStream> r, Integer httpCode, InputStream body) {
        when(r.getStatus()).thenReturn(httpCode);
        when(r.getRawBody()).thenReturn(body);
    }

    @DataProvider
    public static Object[][] executionResults() {
        final BiConsumer<AsdcClient, HttpResponse<InputStream>> defaultClientSetup = (c, r) ->
            when(c.getServiceInputStream(any(), eq(true))).thenReturn(new HttpResponseWithRequestInfo(r, "foo url", HttpMethod.GET));

        final ByteArrayInputStream defaultResponseBody = new ByteArrayInputStream(RandomUtils.nextBytes(200));

        return Stream.<Triple<HttpRequestMetadata, BiConsumer<AsdcClient, HttpResponse<InputStream>>, Consumer<HttpResponse<InputStream>>>>of(

            Triple.of(
                new HttpRequestMetadata(null, 200, null, null, "IllegalStateException", 0),
                defaultClientSetup,
                r -> {
                    when(r.getStatus()).thenReturn(200);
                    when(r.getRawBody()).thenThrow(new IllegalStateException("good news for people who love bad news"));
                }
            ),

            Triple.of(
                new HttpRequestMetadata(null, 200, null, null, "error reading model", 0),
                defaultClientSetup,
                r -> responseSetupper(r, 200, new ByteArrayInputStream(new byte[0]))
            ),
//
            Triple.of(
                new HttpRequestMetadata(null, 200, null, null, "NullPointerException", 0),
                defaultClientSetup,
                r -> responseSetupper(r, 200, null)
            ),
//
            Triple.of(
                new HttpRequestMetadata(null, 0, "bar url", null, "java.net.ConnectException: Connection refused", 0),
                (c, r) ->
                    when(c.getServiceInputStream(any(), eq(true))).thenThrow(new ExceptionWithRequestInfo(HttpMethod.GET, "bar url",
                        new ProcessingException("java.net.ConnectException: Connection refused: connect"))),
                r -> responseSetupper(r, 200, defaultResponseBody)
            ),

            Triple.of(
                new HttpRequestMetadata(null, 500, null, null, "error while retrieving model", 0),
                defaultClientSetup,
                r -> responseSetupper(r, 500, defaultResponseBody)
            ),

            Triple.of(
                new HttpRequestMetadata(null, 404, null, null, "updating vid probe configuration", 0),
                defaultClientSetup,
                r -> responseSetupper(r, 404, defaultResponseBody)
            )

        ).map(t -> ImmutableList.of(t.getLeft(), t.getMiddle(), t.getRight()).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "executionResults")
    public void whenClientReturnWithError_thenProbeSdcByGettingModelDescribes(HttpRequestMetadata expectedMetadata,
        BiConsumer<AsdcClient, HttpResponse<InputStream>> clientSetup,
        Consumer<HttpResponse<InputStream>> responseSetup) {

        final HttpResponse<InputStream> mockResponse = mock(HttpResponse.class);
        clientSetup.accept(asdcClientMock, mockResponse);
        responseSetup.accept(mockResponse);

        ExternalComponentStatus sdcComponentStatus = vidService.probeSdcByGettingModel(UUID.randomUUID());
        assertThat(sdcComponentStatus.getComponent(), is(ExternalComponentStatus.Component.SDC));
        assertThat(sdcComponentStatus.getMetadata(), instanceOf(HttpRequestMetadata.class));

        HttpRequestMetadata componentStatusMetadata = ((HttpRequestMetadata) sdcComponentStatus.getMetadata());
        assertThat(componentStatusMetadata.getDescription(), containsString(defaultIfNull(expectedMetadata.getDescription(), "OK")));
        assertThat(componentStatusMetadata.getHttpMethod(), equalTo(defaultIfNull(expectedMetadata.getHttpMethod(), HttpMethod.GET)));
        assertThat(componentStatusMetadata.getHttpCode(), equalTo(expectedMetadata.getHttpCode()));
        assertThat(componentStatusMetadata.getUrl(), equalTo(defaultIfNull(expectedMetadata.getUrl(), "foo url")));

        assertThat(sdcComponentStatus.isAvailable(), is(false));
    }

}

