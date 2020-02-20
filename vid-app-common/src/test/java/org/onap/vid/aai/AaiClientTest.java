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

package org.onap.vid.aai;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.onap.vid.utils.Unchecked.toURI;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.ServletContext;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.mockito.Mockito;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.CustomQuerySimpleResult;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.ModelVersions;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.RelatedToProperty;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.aai.model.SimpleResult;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.CacheProvider;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.controller.LocalWebConfig;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.model.probes.StatusMetadata;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.security.provider.certpath.SunCertPathBuilderException;
import sun.security.validator.ValidatorException;

@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
@WebAppConfiguration
public class AaiClientTest {

    private final String NO_LCP_REGION_AND_TENANTS_MSG = "A&AI has no LCP Region & Tenants associated to subscriber 'subscriberId' and service type 'serviceType'";
    private AaiClient aaiClientMock;
    private ServletContext servletContext;

    @BeforeMethod
    public void initMocks(){
        aaiClientMock = mock(AaiClient.class);
        aaiClientMock.logger = mock(EELFLoggerDelegate.class);
        aaiClientMock.objectMapper = new ObjectMapper();
        servletContext = mock(ServletContext.class);

        when(servletContext.getRealPath(any(String.class))).thenReturn("");

        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(null);
        when(aaiClientMock.doAaiGet(any(URI.class), anyBoolean(), anyBoolean())).thenReturn(null);
    }

    @DataProvider
    public static Object[][] logicalLinkData() {
        return new Object[][] {
                {"", "network/logical-links/logical-link/"},
                {"link", "network/logical-links/logical-link/link"}
        };
    }

      private static String nfRoleOnly = "{\"start\":[\"/business/customers/customer/globalCustomerId1-360-as988q/service-subscriptions/service-subscription/TEST1-360/service-instances\"],\"query\":\"query/vnfs-fromServiceInstance-filter?nfRole=test360\"}";
    private static String nfRoleAndCloudRegion = "{\"start\":[\"/business/customers/customer/globalCustomerId1-360-as988q/service-subscriptions/service-subscription/TEST1-360/service-instances\"],\"query\":\"query/vnfs-fromServiceInstance-filterByCloudRegion?nfRole=test360&cloudRegionID=cloudRegion-1\"}";
    private static String cloudRegionOnly = "{\"start\":[\"/business/customers/customer/globalCustomerId1-360-as988q/service-subscriptions/service-subscription/TEST1-360/service-instances\"],\"query\":\"query/vnfs-fromServiceInstance-filterByCloudRegion?cloudRegionID=cloudRegion-1\"}";
    private static String withoutNfroleAndCloudRegion = "{\"start\":[\"/business/customers/customer/globalCustomerId1-360-as988q/service-subscriptions/service-subscription/TEST1-360/service-instances\"],\"query\":\"query/vnfs-fromServiceInstance-filter\"}";
    private static String withoutNfroleAndCloudRegionWithSpace = "{\"start\":[\"/business/customers/customer/globalCustomerId1with%20space%20360-as988q/service-subscriptions/service-subscription/TEST1%20360/service-instances\"],\"query\":\"query/vnfs-fromServiceInstance-filter\"}";

    private static String responseJsonNfRole = "/payload_jsons/changeManagement/vnfs-fromServiceInstance-filterNfRole.json";
    private static String responseJsonCloudRegion ="/payload_jsons/changeManagement/vnfs-fromServiceInstance-filterByCloudRegion.json";


    @DataProvider
    public static Object[][] aaiPutCustomQueryData() {
        return new Object[][] {
            {"globalCustomerId1-360-as988q", "TEST1-360", "test360", null, nfRoleOnly, responseJsonNfRole, "908419144", 200},
            {"globalCustomerId1-360-as988q", "TEST1-360", null, "cloudRegion-1", cloudRegionOnly, responseJsonCloudRegion, "1165906024", 200},
            {"globalCustomerId1-360-as988q", "TEST1-360", "test360", "cloudRegion-1", nfRoleAndCloudRegion,
                responseJsonCloudRegion, "1165906024", 200},
            {"globalCustomerId1with space 360-as988q", "TEST1 360", null, null, withoutNfroleAndCloudRegionWithSpace, responseJsonNfRole, "908419144", 200},
                {"globalCustomerId1-360-as988q", "TEST1-360", null, null, withoutNfroleAndCloudRegion, responseJsonNfRole, "908419144", 200},
        };
    }

    @Test(dataProvider = "aaiPutCustomQueryData")
    public void testAaiPutCustomQueryByParams(String globalCustomerId, String serviceType, String nfRole, String cloudRegion, String expectedPayload, String responseBody, String expectedId, int responseHttpCode) {
        String queryFormat = "query?format=simple";
        final ResponseWithRequestInfo mockedResponseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK,
            TestUtils.readFileAsString(responseBody),
            "query?format=simple&Mock=True",
            HttpMethod.PUT);
        when(aaiClientMock.doAaiPut(eq(queryFormat), anyString(), anyBoolean(), anyBoolean())).thenReturn(mockedResponseWithRequestInfo);
        when(aaiClientMock.getVnfsByParamsForChangeManagement(anyString(), anyString(), nullable(String.class), nullable(String.class))).thenCallRealMethod();
        AaiResponse<AaiGetVnfResponse> response = aaiClientMock.getVnfsByParamsForChangeManagement(globalCustomerId, serviceType, nfRole, cloudRegion);
        verify(aaiClientMock).doAaiPut(eq(queryFormat), eq(expectedPayload), eq(false), eq(false));
        assertEquals(response.getHttpCode(), responseHttpCode);
        assertEquals(response.getT().getResults().get(0).id, expectedId);
    }

    @Test(dataProvider = "logicalLinkData")
    public void getLogicalLink_Link_Is_Empty(String link, String expectedUrl) {

        when(aaiClientMock.getLogicalLink(any(String.class))).thenCallRealMethod();
        aaiClientMock.getLogicalLink(link);
        verify(aaiClientMock).doAaiGet(argThat(s -> equalsIgnoreCase(s, expectedUrl)),any(Boolean.class));
    }

    @DataProvider
    public static Object[][] subscribersResults() {
        return new Object[][] {
                {new SubscriberList(new ArrayList<Subscriber>() {{ add(new Subscriber());  add(new Subscriber()); }}), true},
                {new SubscriberList(new ArrayList<Subscriber>() {{ add(new Subscriber()); }}), true},
                {new SubscriberList(new ArrayList<Subscriber>()), false}
        };
    }

    @Test(dataProvider = "subscribersResults")
    public void testProbeAaiGetAllSubscribers_returnsTwoToZeroSubscribers_ResultsAsExpected(SubscriberList subscribers, boolean isAvailable){
        ExternalComponentStatus expectedStatus = new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,isAvailable, new HttpRequestMetadata(
                HttpMethod.GET,
                200,
                "url",
                "rawData",
                isAvailable ? "OK" : "No subscriber received",
                0
        ));
        Mockito.when(aaiClientMock.getAllSubscribers(true)).thenReturn(
                new AaiResponseWithRequestInfo<>(
                        HttpMethod.GET, "url", new AaiResponse<>(subscribers, null, 200),
                        "rawData"));
        Mockito.when(aaiClientMock.probeComponent()).thenCallRealMethod();
        ExternalComponentStatus result  = aaiClientMock.probeComponent();
        assertThat(statusDataReflected(result),is(statusDataReflected(expectedStatus)));
        assertThat(requestMetadataReflected(result.getMetadata()),is(requestMetadataReflected(expectedStatus.getMetadata())));
    }

    @Test(expectedExceptions = Exception.class)
    public void typedAaiGet_aaiRestInterfaceRestGetReturnsError_exceptionIsThrown() {
        AAIRestInterface aaiRestInterface = mock(AAIRestInterface.class);
        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.INTERNAL_SERVER_ERROR, "entity");
        mockForGetRequest(aaiRestInterface, responseWithRequestInfo);
        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);

        try {

            aaiClient.typedAaiGet(toURI("/irrelevant/url"), RelatedToProperty.class);

        } catch (Exception e) {
            assertThat(ExceptionUtils.getStackTrace(e), e, instanceOf(ExceptionWithRequestInfo.class));
            ExceptionWithRequestInfo e2 = ((ExceptionWithRequestInfo) e);
            assertThat(e2.getHttpCode(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
            assertThat(e2.getRawData(), is("entity"));
            assertThat(e2.getHttpMethod(), is(HttpMethod.GET));
            assertThat(e2.getRequestedUrl(), is("/my/mocked/url"));

            throw e;
        }
    }

    @Test(expectedExceptions = Exception.class)
    public void typedAaiGet_aaiRestInterfaceRestGetReturnsInparsableResponse_exceptionIsThrown() {
        AAIRestInterface aaiRestInterface = mock(AAIRestInterface.class);
        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK, "entity");
        mockForGetRequest(aaiRestInterface, responseWithRequestInfo);
        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);

        try {

            aaiClient.typedAaiGet(toURI("/irrelevant/url"), RelatedToProperty.class);

        } catch (Exception e) {
            assertThat(ExceptionUtils.getStackTrace(e), e, instanceOf(ExceptionWithRequestInfo.class));
            assertThat(e.getCause(),
                    hasProperty("cause", is(instanceOf(com.fasterxml.jackson.core.JsonParseException.class)))
            );
            throw e;
        }
    }

    @Test
    public void typedAaiGet_aaiRestInterfaceRestGetReturns_objectIsFine() {
        AAIRestInterface aaiRestInterface = mock(AAIRestInterface.class);
        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK,
                "{ \"property-key\": \"foo\", \"property-value\": \"bar\" }");
        mockForGetRequest(aaiRestInterface, responseWithRequestInfo);

        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);

        final RelatedToProperty relatedToPropertyAaiResponse = aaiClient.typedAaiGet(toURI("/irrelevant/url"), RelatedToProperty.class);

        assertThat(relatedToPropertyAaiResponse.getPropertyKey(), is("foo"));
        assertThat(relatedToPropertyAaiResponse.getPropertyValue(), is("bar"));
    }

    private ResponseWithRequestInfo mockedResponseWithRequestInfo(Response.Status status, String entity) {
        return mockedResponseWithRequestInfo(status, entity, "/my/mocked/url", HttpMethod.GET);
    }

    private ResponseWithRequestInfo mockedResponseWithRequestInfo(Response.Status status, String entity, String requestUrl, HttpMethod method) {
        final Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(status.getStatusCode());
        when(mockResponse.getStatusInfo()).thenReturn(status);
        when(mockResponse.readEntity(String.class)).thenReturn(entity);
        return new ResponseWithRequestInfo(mockResponse, requestUrl, method);
    }

    //serialize fields except of fields we cannot know ahead of time
    private static String requestMetadataReflected(StatusMetadata metadata) {
        return new ReflectionToStringBuilder(metadata, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("duration")
                .toString();
    }

    private static String statusDataReflected(ExternalComponentStatus status) {
        return new ReflectionToStringBuilder(status, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("metadata")
                .toString();
    }

    @DataProvider
    public static Object[][] rawData() {
        return new Object[][]{
                {"errorMessage", }, {""}, {null}
        };
    }

    @Test(dataProvider = "rawData")
    public void testProbeAaiGetFullSubscribersWithNullResponse_returnsNotAvailableWithErrorRawData(String rawData){
        Mockito.when(aaiClientMock.getAllSubscribers(true)).thenReturn(
                new AaiResponseWithRequestInfo<>(HttpMethod.GET, "url", null,
                        rawData));
        ExternalComponentStatus result = callProbeAaiGetAllSubscribersAndAssertNotAvailable();
        assertThat(result.getMetadata(), instanceOf(HttpRequestMetadata.class));
        assertEquals(((HttpRequestMetadata) result.getMetadata()).getRawData(), rawData);
    }

    @DataProvider
    public static Object[][] exceptions() {
        return new Object[][] {
                {"NullPointerException", "errorMessage",
                        new ExceptionWithRequestInfo(HttpMethod.GET, "url",
                                "errorMessage", null, new NullPointerException())},
                {"RuntimeException", null,
                        new ExceptionWithRequestInfo(HttpMethod.GET, "url",
                                null, null, new RuntimeException())},
                {"RuntimeException", null,
                        new RuntimeException()},
        };
    }

    @Test(dataProvider = "exceptions")
    public void testProbeAaiGetFullSubscribersWithNullResponse_returnsNotAvailableWithErrorRawData(String description, String expectedRawData, Exception exception){
        Mockito.when(aaiClientMock.getAllSubscribers(true)).thenThrow(exception);
        ExternalComponentStatus result = callProbeAaiGetAllSubscribersAndAssertNotAvailable();
        if (exception instanceof ExceptionWithRequestInfo) {
            assertThat(result.getMetadata(), instanceOf(HttpRequestMetadata.class));
            assertEquals(((HttpRequestMetadata) result.getMetadata()).getRawData(), expectedRawData);
        }
        assertThat(result.getMetadata().getDescription(), containsString(description));
    }

    private ExternalComponentStatus callProbeAaiGetAllSubscribersAndAssertNotAvailable() {
        Mockito.when(aaiClientMock.probeComponent()).thenCallRealMethod();
        ExternalComponentStatus result  = aaiClientMock.probeComponent();
        assertFalse(result.isAvailable());
        return result;
    }


    @Test
    public void getTenants_Arguments_Are_Null_Or_Empty() {

        when(aaiClientMock.getTenants(any(), any())).thenCallRealMethod();

        AaiResponse response = aaiClientMock.getTenants("", "");

        assertEquals(response.getErrorMessage(), "{\"statusText\":\" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.\"}");


        response = aaiClientMock.getTenants(null, null);

        assertEquals(response.getErrorMessage(), "{\"statusText\":\" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.\"}");
    }

    @Test(expectedExceptions = AaiClient.ParsingGetTenantsResponseFailure.class, expectedExceptionsMessageRegExp = NO_LCP_REGION_AND_TENANTS_MSG)
    public void getTenants_Arguments_Are_Valid_But_Tenants_Not_Exist() {

        when(aaiClientMock.getTenantsNonCached(any(String.class),any(String.class))).thenCallRealMethod();

        Response generalEmptyResponse = mock(Response.class);
        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(generalEmptyResponse);

        aaiClientMock.getTenantsNonCached("subscriberId", "serviceType");
    }

    @Test
    public void whenCacheThrowException_thenGetTenantReturnAaiResponse() {
        CacheProvider mockCacheProvider = mock(CacheProvider.class);
        CacheProvider.Cache mockCache = mock(CacheProvider.Cache.class);
        AaiClient aaiClientUnderTest = new AaiClient(null, null, mockCacheProvider);

        when(mockCacheProvider.aaiClientCacheFor(any(), any())).thenReturn(mockCache);
        when(mockCache.get(any())).thenThrow(new AaiClient.ParsingGetTenantsResponseFailure(NO_LCP_REGION_AND_TENANTS_MSG));
        AaiResponse aaiResponse = aaiClientUnderTest.getTenants("subscriberId", "serviceType");
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, aaiResponse.getHttpCode());
        assertEquals("{\"statusText\":\""+NO_LCP_REGION_AND_TENANTS_MSG+"\"}", aaiResponse.getErrorMessage());
    }

    @Test
    public void getTenants_Arguments_Are_Valid_Get_The_Tenanats() {

        when(aaiClientMock.getTenantsNonCached(any(String.class),any(String.class))).thenCallRealMethod();


        Response generalEmptyResponse = mock(Response.class);

        when(generalEmptyResponse.readEntity(String.class)).thenReturn(tenantResponseRaw);
        when(generalEmptyResponse.getStatus()).thenReturn(200);
        when(generalEmptyResponse.getStatusInfo()).thenReturn(Response.Status.OK);


        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(generalEmptyResponse);

        AaiResponse<GetTenantsResponse[]> response = aaiClientMock.getTenantsNonCached("subscriberId", "serviceType");

        GetTenantsResponse[] tenants = response.getT();

        Assert.assertTrue(response.t.length> 0);

        Assert.assertEquals(tenants[0].cloudOwner,"irma-aic-cloud-owner");
    }

    final String tenantResponseRaw ="" +
            "{" +
            "\"service-type\": \"VIRTUAL USP\"," +
            "\"resource-version\": \"1494001841964\"," +
            "\"relationship-list\": {" +
            "\"relationship\": [{" +
            "\"related-to\": \"tenant\"," +
            "\"related-link\": \"/aai/v11/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/AAIAIC25/tenants/tenant/092eb9e8e4b7412e8787dd091bc58e86\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"cloud-region.cloud-owner\"," +
            "\"relationship-value\": \"irma-aic-cloud-owner\"" +
            "}," +
            "{" +
            "\"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "\"relationship-value\": \"AAIAIC25\"" +
            "}," +
            "{" +
            "\"relationship-key\": \"tenant.tenant-id\"," +
            "\"relationship-value\": \"092eb9e8e4b7412e8787dd091bc58e86\"" +
            "}" +
            "]," +
            "\"related-to-property\": [{" +
            "\"property-key\": \"tenant.tenant-name\"," +
            "\"property-value\": \"USP-SIP-IC-24335-T-01\"" +
            "}]" +
            "}]" +
            "}" +
            "}";

    final String vfModuleHomingResponseRaw ="{" +
            "    \"vf-module-id\": \"ed02354a-3217-45ce-a1cd-e0b69b7a8cea\"," +
            "    \"vf-module-name\": \"apndns_az_02_module_1\"," +
            "    \"heat-stack-id\": \"apndns_az_02_module_1/97a319f3-b095-4fff-befa-c657508ecaf8\"," +
            "    \"orchestration-status\": \"active\"," +
            "    \"is-base-vf-module\": false," +
            "    \"resource-version\": \"1530559380383\"," +
            "    \"model-invariant-id\": \"74450b48-0aa0-4743-8314-9163e92b7862\"," +
            "    \"model-version-id\": \"6bc01a2b-bc48-4991-b9fe-e22c2215d801\"," +
            "    \"model-customization-id\": \"74f638c2-0368-4212-8f73-e961005af17c\"," +
            "    \"module-index\": 0," +
            "    \"relationship-list\": {" +
            "        \"relationship\": [" +
            "            {" +
            "                \"related-to\": \"l3-network\"," +
            "                \"relationship-label\": \"org.onap.relationships.inventory.DependsOn\"," +
            "                \"related-link\": \"/aai/v12/network/l3-networks/l3-network/335e62be-73a3-41e8-930b-1a677bcafea5\"," +
            "                \"relationship-data\": [" +
            "                    {" +
            "                        \"relationship-key\": \"l3-network.network-id\"," +
            "                        \"relationship-value\": \"335e62be-73a3-41e8-930b-1a677bcafea5\"" +
            "                    }" +
            "                ]," +
            "                \"related-to-property\": [" +
            "                    {" +
            "                        \"property-key\": \"l3-network.network-name\"," +
            "                        \"property-value\": \"MNS-FN-25180-T-02Shared_oam_protected_net_1\"" +
            "                    }" +
            "                ]" +
            "            }," +
            "            {" +
            "                \"related-to\": \"l3-network\"," +
            "                \"relationship-label\": \"org.onap.relationships.inventory.DependsOn\"," +
            "                \"related-link\": \"/aai/v12/network/l3-networks/l3-network/2db4ee3e-2ac7-4fc3-8739-ecf53416459e\"," +
            "                \"relationship-data\": [" +
            "                    {" +
            "                        \"relationship-key\": \"l3-network.network-id\"," +
            "                        \"relationship-value\": \"2db4ee3e-2ac7-4fc3-8739-ecf53416459e\"" +
            "                    }" +
            "                ]," +
            "                \"related-to-property\": [" +
            "                    {" +
            "                        \"property-key\": \"l3-network.network-name\"," +
            "                        \"property-value\": \"Mobisupport-FN-27099-T-02_int_apn_dns_net_1\"" +
            "                    }" +
            "                ]" +
            "            }," +
            "            {" +
            "                \"related-to\": \"volume-group\"," +
            "                \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "                \"related-link\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/rdm5b/volume-groups/volume-group/66013ebe-0c81-44b9-a24f-7c6acba73a39\"," +
            "                \"relationship-data\": [" +
            "                    {" +
            "                        \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                        \"relationship-value\": \"irma-aic\"" +
            "                    }," +
            "                    {" +
            "                        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                        \"relationship-value\": \"rdm5b\"" +
            "                    }," +
            "                    {" +
            "                        \"relationship-key\": \"volume-group.volume-group-id\"," +
            "                        \"relationship-value\": \"66013ebe-0c81-44b9-a24f-7c6acba73a39\"" +
            "                    }" +
            "                ]" +
            "            }," +
            "            {" +
            "                \"related-to\": \"vserver\"," +
            "                \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "                \"related-link\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/rdm5b/tenants/tenant/db1818f7f2e34862b378bfb2cc520f91/vservers/vserver/5eef9f6d-9933-4bc6-9a1a-862d61309437\"," +
            "                \"relationship-data\": [" +
            "                    {" +
            "                        \"relationship-key\": \"cloud-region.cloud-owner\"," +
            "                        \"relationship-value\": \"irma-aic\"" +
            "                    }," +
            "                    {" +
            "                        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
            "                        \"relationship-value\": \"rdm5b\"" +
            "                    }," +
            "                    {" +
            "                        \"relationship-key\": \"tenant.tenant-id\"," +
            "                        \"relationship-value\": \"db1818f7f2e34862b378bfb2cc520f91\"" +
            "                    }," +
            "                    {" +
            "                        \"relationship-key\": \"vserver.vserver-id\"," +
            "                        \"relationship-value\": \"5eef9f6d-9933-4bc6-9a1a-862d61309437\"" +
            "                    }" +
            "                ]," +
            "                \"related-to-property\": [" +
            "                    {" +
            "                        \"property-key\": \"vserver.vserver-name\"," +
            "                        \"property-value\": \"zrdm5bfapn01dns002\"" +
            "                    }" +
            "                ]" +
            "            }" +
            "        ]" +
            "    }" +
            "}";
    @Test
    public void get_homingDataForVfModule() {
        when(aaiClientMock.getHomingDataByVfModule(any(String.class), any(String.class))).thenCallRealMethod();

        Response homingResponse = mock(Response.class);

        when(homingResponse.readEntity(String.class)).thenReturn(vfModuleHomingResponseRaw);
        when(homingResponse.getStatus()).thenReturn(200);
        when(homingResponse.getStatusInfo()).thenReturn(Response.Status.OK);


        when(aaiClientMock.doAaiGet(any(String.class), any(Boolean.class))).thenReturn(homingResponse);

        GetTenantsResponse tenant = aaiClientMock.getHomingDataByVfModule("vnfInstanceId", "vfModuleId");

        Assert.assertEquals(tenant.cloudOwner,"irma-aic" );
        Assert.assertEquals(tenant.cloudRegionID,"rdm5b");
        Assert.assertEquals(tenant.tenantID,"db1818f7f2e34862b378bfb2cc520f91");

    }
    @Test(expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = "A&AI has no homing data associated to vfModule 'vfModuleId' of vnf 'vnfInstanceId'")
    public void getVfModule_Homing_Arguments_Are_Valid_But_Not_Exists() {
        when(aaiClientMock.getHomingDataByVfModule(any(String.class), any(String.class))).thenCallRealMethod();

        Response generalEmptyResponse = mock(Response.class);
        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(generalEmptyResponse);

        aaiClientMock.getHomingDataByVfModule("vnfInstanceId", "vfModuleId");
    }

    @DataProvider
    public static Object[][] invalidDataId() {
        return new String[][] {
                {""},
                {null}
        };
    }

    @Test(dataProvider = "invalidDataId", expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = "Failed to retrieve homing data associated to vfModule from A&AI, VNF InstanceId or VF Module Id is missing.")
    public void getVfModule_Homing_Arguments_Are_Empty_Or_Null(String data) {
        when(aaiClientMock.getHomingDataByVfModule(any(), any())).thenCallRealMethod();
             aaiClientMock.getHomingDataByVfModule(data, data);
    }

    @DataProvider
    public static Object[][] resourceTypesProvider() {
        return new Object[][] {
                {"service-instance", ResourceType.SERVICE_INSTANCE},
                {"generic-vnf", ResourceType.GENERIC_VNF},
                {"vf-module", ResourceType.VF_MODULE}
        };
    }

    @DataProvider
    public static Object[][] nameAndResourceTypeProvider() {
        return new Object[][] {
                {"SRIOV_SVC", ResourceType.SERVICE_INSTANCE, "nodes/service-instances?service-instance-name=SRIOV_SVC"},
                {"b1707vidnf", ResourceType.GENERIC_VNF, "nodes/generic-vnfs?vnf-name=b1707vidnf"},
                {"connectivity_test", ResourceType.VF_MODULE, "nodes/vf-modules?vf-module-name=connectivity_test"},
                {"ByronPace", ResourceType.INSTANCE_GROUP, "nodes/instance-groups?instance-group-name=ByronPace"},
                {"MjVg1234", ResourceType.VOLUME_GROUP, "nodes/volume-groups?volume-group-name=MjVg1234"}
        };
    }

    @Test(dataProvider = "nameAndResourceTypeProvider")
    public void whenSearchNodeTypeByName_callRightAaiPath(String name, ResourceType type, String expectedUrl) {
        AAIRestInterface aaiRestInterface = mock(AAIRestInterface.class);
        ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK, "{}");

        when(aaiRestInterface.RestGet(anyString(), anyString(), eq(toURI(expectedUrl)), anyBoolean(), anyBoolean()))
                .thenReturn(responseWithRequestInfo);

        AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);

        aaiClient.isNodeTypeExistsByName(name, type);
    }

    @DataProvider
    public static Object[][] aaiClientInternalExceptions() {
        return Stream.<Pair<Class<? extends Throwable>, UncheckedBiConsumer<HttpsAuthClient, Client>>>of(

                // Exception out of httpsAuthClientMock
                Pair.of(CertificateException.class, (httpsAuthClientMock, javaxClientMock) -> {
                    final CertificateException e0 = new CertificateException("No X509TrustManager implementation available");
                    SSLHandshakeException e = new SSLHandshakeException(e0.toString());
                    e.initCause(e0);

                    when(httpsAuthClientMock.getClient(any())).thenThrow(e);
                }),

                Pair.of(StringIndexOutOfBoundsException.class, mockExceptionOnClientProvider(new StringIndexOutOfBoundsException(4))),

                Pair.of(NullPointerException.class, mockExceptionOnClientProvider(new NullPointerException("null"))),

                Pair.of(FileNotFoundException.class, mockExceptionOnClientProvider(new FileNotFoundException("vid/WEB-INF/cert/aai-client-cert.p12"))),

                Pair.of(BadPaddingException.class, mockExceptionOnClientProvider(
                        new IOException("keystore password was incorrect", new BadPaddingException("Given final block not properly padded")))
                ),
                Pair.of(GenericUncheckedException.class, mockExceptionOnClientProvider(new GenericUncheckedException("basa"))),

                Pair.of(NullPointerException.class, (httpsAuthClientMock, javaxClientMock) ->
                        when(httpsAuthClientMock.getClient(any())).thenReturn(null)),


                // Exception out of javax's Client
                Pair.of(SSLHandshakeException.class, (httpsAuthClientMock, javaxClientMock) -> {
                    when(javaxClientMock.target(nullable(String.class))).thenThrow(
                            new ProcessingException(new SSLHandshakeException("Received fatal alert: certificate_expired"))
                    );
                }),

                Pair.of(SunCertPathBuilderException.class, (httpsAuthClientMock, javaxClientMock) -> {
                    SunCertPathBuilderException e0 = new SunCertPathBuilderException("unable to find valid certification path to requested target");
                    when(javaxClientMock.target(nullable(String.class))).thenThrow(
                            new ProcessingException(new ValidatorException("PKIX path building failed: " + e0.toString(), e0))
                    );
                }),

                Pair.of(GenericUncheckedException.class, (httpsAuthClientMock, javaxClientMock) ->
                        when(javaxClientMock.target(nullable(String.class))).thenThrow(new GenericUncheckedException("basa")))

        ).flatMap(l -> Stream.of(
                // double each case to propagateExceptions = true/false, to verify that "don't propagate" really still work
                ImmutableList.of(l.getLeft(), l.getRight(), true).toArray(),
                ImmutableList.of(l.getLeft(), l.getRight(), false).toArray()
        )).collect(toList()).toArray(new Object[][]{});
    }

    private static UncheckedBiConsumer<HttpsAuthClient, Client> mockExceptionOnClientProvider(Exception e) {
        return (httpsAuthClientMock, javaxClientMock) ->
                when(httpsAuthClientMock.getClient(any())).thenThrow(e);
    }

    @Test(dataProvider = "aaiClientInternalExceptions")
    public void propagateExceptions_internalsThrowException_ExceptionRethrown(Class<? extends Throwable> expectedType, BiConsumer<HttpsAuthClient, Client> setupMocks, boolean propagateExceptions) throws Exception {
        /*
        Call chain is like:
            this test -> AaiClient -> AAIRestInterface -> HttpsAuthClient -> javax's Client

        In this test, *AaiClient* and *AAIRestInterface* are under test (actual
        implementation is used), while HttpsAuthClient and the javax's Client are
        mocked to return pseudo-responses or - better- throw exceptions.
         */

        // prepare mocks
        HttpsAuthClient httpsAuthClientMock = mock(HttpsAuthClient.class);
        TestUtils.JavaxRsClientMocks mocks = new TestUtils.JavaxRsClientMocks();
        Client javaxClientMock = mocks.getFakeClient();
        Response responseMock = mocks.getFakeResponse();

        // prepare real AAIRestInterface and AaiClient, and wire mocks
        AAIRestInterface aaiRestInterface = new AAIRestInterface(httpsAuthClientMock,
            mock(ServletRequestHelper.class),
            mock(SystemPropertyHelper.class),
            mock(Logging.class));
        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);
        when(httpsAuthClientMock.getClient(any())).thenReturn(javaxClientMock);

        // define atomic method under test, including reset of "aaiRestInterface.client"
        final Function<Boolean, Response> doAaiGet = (propagateExceptions1) -> {
            try {
                FieldUtils.writeField(aaiRestInterface, "client", null, true);
                return aaiClient.doAaiGet("uri", false, propagateExceptions1).getResponse();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        // verify setup again
        assertThat("mocks setup should make doAaiGet return our responseMock", doAaiGet.apply(true), is(sameInstance(responseMock)));


        /// TEST:
        setupMocks.accept(httpsAuthClientMock, javaxClientMock);

        try {
            final Response response = doAaiGet.apply(propagateExceptions);
        } catch (Exception e) {
            if (propagateExceptions) {
                assertThat("root cause incorrect for " + ExceptionUtils.getStackTrace(e), ExceptionUtils.getRootCause(e), instanceOf(expectedType));
                return; // ok, done
            } else {
                // Verify that "don't propagate" really still work
                Assert.fail("calling doAaiGet when propagateExceptions is false must result with no exception", e);
            }
        }

        // If no exception caught
        // We're asserting that the legacy behaviour is still in place. Hopefully
        // one day we will remove the non-propagateExceptions case
        assertFalse(propagateExceptions, "calling doAaiGet when propagateExceptions is 'true' must result with an exception (in this test)");
    }

    @DataProvider
    public static Object[][] aaiClientGetCloudOwnerByCloudRegionId() {

        final String cloudRegion = "{" +
                "      \"cloud-owner\": \"mure-royo-ru22\"," +
                "      \"cloud-region-id\": \"ravitu\"," +
                "      \"cloud-type\": \"openstack\"," +
                "      \"resource-version\": \"1523631256125\"," +
                "      \"relationship-list\": {" +
                "        \"relationship\": [{" +
                "            \"related-to\": \"pserver\"" +
                "          }" +
                "        ]" +
                "      }" +
                "    }";

        String bodyWith0 = "{  \"cloud-region\": [" + "  ]}";
        String bodyWith1 = "{  \"cloud-region\": [" + cloudRegion + "  ]}";
        String bodyWith2 = "{  \"cloud-region\": [" + cloudRegion + ", " + cloudRegion + "  ]}";
        String bodyWithDifferent2 = "{  \"cloud-region\": [" + cloudRegion + ", " +
                cloudRegion.replace("mure-royo-ru22", "nolay-umaxo") +
                "]}";

        return new Object[][] {
                { "regular single result", bodyWith1, false },
                { "exceptional empty result", bodyWith0, true },
                { "two same results", bodyWith2, false },
                { "two incoherent results", bodyWithDifferent2, true },
        };
    }

    @Test(dataProvider = "aaiClientGetCloudOwnerByCloudRegionId")
    public void getCloudOwnerByCloudRegionIdNonCached(String desc, String body, boolean expectingException) {
        final String cloudRegion = "ravitu";
        AAIRestInterface aaiRestInterface = mock(AAIRestInterface.class);
        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK, body);
        when(aaiRestInterface.doRest(anyString(), anyString(), eq(Unchecked.toURI("cloud-infrastructure/cloud-regions?cloud-region-id=" + cloudRegion)),
                isNull(), eq(HttpMethod.GET), anyBoolean(), anyBoolean()))
                .thenReturn(responseWithRequestInfo);

        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);

        try {
            final String result = aaiClient.getCloudOwnerByCloudRegionIdNonCached(cloudRegion);
            if (expectingException) fail("expected failure on " + desc + ", got " + result);
            else {
                assertThat(result, is("mure-royo-ru22"));
            }
        } catch (Exception e) {
            if (!expectingException) throw e;
            else {
                assertThat(e.toString(), either(
                        containsString("No cloud-owner found for " + cloudRegion))
                        .or(containsString("Conflicting cloud-owner found for " + cloudRegion)));
            }
        }
    }

    @DataProvider
    public static Object[][]  cloudRegionAndTenantDataProvider() {
        return new Object[][] {
                { "APPC-24595-T-IST-02C", "mtn23b" },
                { "APPC-24595-T-IST-02C", null },
                { null, "mtn23b" },
                { null, null },
        };
    }

    @DataProvider
    public static Object[][]  versionsDataProvider() {
        return new Object[][] {
                { Stream.of("20","10","30"), Stream.of("30","20","10"), "30" },
                { Stream.of("10","20","20"), Stream.of("20","20","10"), "20" },
                { Stream.of("c","b","a"), Stream.of("c","b","a"), "c" },
                { Stream.of("1.0","2.0","1.8"), Stream.of("2.0","1.8","1.0"), "2.0" },
                { Stream.of("1.0.7","2.0.9","2.0.2"), Stream.of("2.0.9","2.0.2","1.0.7"), "2.0.9" },
                { Stream.of("0","0","0"), Stream.of("0","0","0"), "0" },
                { Stream.of("","10"), Stream.of("10",""), "10" },
        };
    }

    @Test(dataProvider = "versionsDataProvider")
    public void sortedModelVer(Stream<String> input, Stream<String> expectedSorted, String expectedMax) {
        Stream<ModelVer> modelVerStream = input.map(version -> {
            ModelVer mv = new ModelVer();
            mv.setModelVersion(version);
            return mv;
        });

        final AaiClient aaiClient = new AaiClient(null, null, null);

        assertThat(aaiClient.sortedModelVer(modelVerStream),
            contains(
                expectedSorted.map(it -> hasProperty("modelVersion", is(it))).toArray(Matcher[]::new)
            ));
    }

    @Test(dataProvider = "versionsDataProvider")
    public void maxModelVer(Stream<String> input, Stream<String> expectedSorted, String expectedMax) {
        Stream<ModelVer> modelVerStream = input.map(version -> {
            ModelVer mv = new ModelVer();
            mv.setModelVersion(version);
            return mv;
        });

        final AaiClient aaiClient = new AaiClient(null, null, null);

        assertThat(aaiClient.maxModelVer(modelVerStream), hasProperty("modelVersion", is(expectedMax)));
    }

    @Test(expectedExceptions = GenericUncheckedException.class)
    public void maxModelVerException() {
        final AaiClient aaiClient = new AaiClient(null, null, null);
        aaiClient.maxModelVer(Stream.of(new ModelVer()));
    }
    @Test(dataProvider = "cloudRegionAndTenantDataProvider")
    public void getCloudRegionAndTenantByVnfId(String tenantName, String cloudRegionId) throws JsonProcessingException {
        SimpleResult tenant = new SimpleResult();
        if (tenantName != null) {
            tenant.setJsonNodeType("tenant");
            Properties tenantProps = new Properties();
            tenantProps.setTenantName(tenantName);
            tenant.setJsonProperties(tenantProps);
        }

        SimpleResult cloudRegion = new SimpleResult();
        if (cloudRegionId != null) {
            cloudRegion.setJsonNodeType("cloud-region");
            Properties cloudRegionProps = new Properties();
            cloudRegionProps.setCloudRegionId(cloudRegionId);
            cloudRegion.setJsonProperties(cloudRegionProps);
        }

        CustomQuerySimpleResult customQuerySimpleResult = new CustomQuerySimpleResult(ImmutableList.of(tenant, cloudRegion));
        String mockedBody = new ObjectMapper().writeValueAsString(customQuerySimpleResult);

        AAIRestInterface aaiRestInterface = mock(AAIRestInterface.class);
        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK, mockedBody, "query?format=simple", HttpMethod.PUT);
        when(aaiRestInterface.doRest(anyString(), anyString(), eq(Unchecked.toURI("query?format=simple")),
                any(), eq(HttpMethod.PUT), anyBoolean(), anyBoolean()))
                .thenReturn(responseWithRequestInfo);

        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null, null);
        Map<String, Properties> result = aaiClient.getCloudRegionAndTenantByVnfId("anyVnfId");
        if (tenantName != null) {
            assertEquals(result.get("tenant").getTenantName(), tenantName);
        } else {
            assertNull(result.get("tenant"));
        }

        if (cloudRegionId != null) {
            assertEquals(result.get("cloud-region").getCloudRegionId(), cloudRegionId);
        } else {
            assertNull(result.get("cloud-region"));
        }
    }

    protected void mockForGetRequest(AAIRestInterface aaiRestInterface, ResponseWithRequestInfo responseWithRequestInfo) {
        when(aaiRestInterface.doRest(anyString(), anyString(), any(URI.class), isNull(), eq(HttpMethod.GET) ,anyBoolean(), anyBoolean()))
                .thenReturn(responseWithRequestInfo);
    }

    @Test
    public void shouldProperlyReadResponseOnceWhenSubscribersAreNotPresent() {
        AAIRestInterface restInterface = mock(AAIRestInterface.class);
        PortDetailsTranslator portDetailsTranslator = mock(PortDetailsTranslator.class);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(404);
        when(response.readEntity(String.class)).thenReturn("sampleEntity");
        when(response.getStatusInfo()).thenReturn(Response.Status.NOT_FOUND);
        ResponseWithRequestInfo responseWithRequestInfo = new ResponseWithRequestInfo(response, "test", HttpMethod.GET);
        when(restInterface.RestGet(eq("VidAaiController"), any(String.class),
                eq(Unchecked.toURI("business/customers?subscriber-type=INFRA&depth=0")), eq(false), eq(true))).thenReturn(responseWithRequestInfo);
        AaiClient aaiClient = new AaiClient(restInterface, portDetailsTranslator, null);


        aaiClient.getAllSubscribers(true);

        verify(response).readEntity(String.class);
    }

    @FunctionalInterface
    public interface UncheckedBiConsumer<T, U> extends BiConsumer<T, U> {
        @Override
        default void accept(T t, U u) {
            try {
                acceptThrows(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        void acceptThrows(T t, U u) throws Exception;
    }

    @Test
    public void getLatestVersionByInvariantId_verifyCallingExpectedApi(){

        when(aaiClientMock.getLatestVersionByInvariantId(anyString())).thenCallRealMethod();

        aaiClientMock.getLatestVersionByInvariantId("model-invariant-id");

        Mockito.verify(aaiClientMock).doAaiPut(argThat(url -> url.endsWith("query?format=resource&depth=0")),argThat(payload -> payload.contains("service-design-and-creation/models/model/model-invariant-id")),anyBoolean());

    }

    @DataProvider
    public static Object[][]  getSubscriberDataDataProvider() {
        return new Object[][] {
            { "Some-ID", true },
            { "another id 123", false },
        };
    }

    @Test(dataProvider = "getSubscriberDataDataProvider")
    public void getSubscriberDataParams(String subscriberId, boolean omitServiceInstances) {
        String depth = omitServiceInstances ? "1" : "2";
        when(aaiClientMock.getSubscriberData(anyString(),anyBoolean())).thenCallRealMethod();
        aaiClientMock.getSubscriberData(subscriberId, omitServiceInstances);
        Mockito.verify(aaiClientMock).doAaiGet(argThat(s -> s.contains("customer/" + subscriberId + "?") && s.contains("depth=" + depth)),any(Boolean.class));
    }

    @Test
    public void testToModelVerStream() throws IOException {

        ModelVersions modelVersions = JACKSON_OBJECT_MAPPER.readValue("" +
            "{\n" +
            "    \"results\": [\n" +
            "        {\n" +
            "            \"model\": {\n" +
            "                \"model-invariant-id\": \"f6342be5-d66b-4d03-a1aa-c82c3094c4ea\",\n" +
            "                \"model-type\": \"service\",\n" +
            "                \"resource-version\": \"1534274421300\"\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"model-ver\": {\n" +
            "                \"model-version-id\": \"a92f899d-a3ec-465b-baed-1663b0a5aee1\",\n" +
            "                \"model-name\": \"NCM_VLAN_SVC_ym161f\",\n" +
            "                \"model-version\": \"bbb\",\n" +
            "                \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "                \"model-description\": \"Network Collection service for vLAN tagging\",\n" +
            "                \"resource-version\": \"1534788756086\"\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"model-ver\": {\n" +
            "                \"model-version-id\": \"d2fda667-e92e-4cfa-9620-5da5de01a319\",\n" +
            "                \"model-name\": \"NCM_VLAN_SVC_ym161f\",\n" +
            "                \"model-version\": \"aaa\",\n" +
            "                \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
            "                \"model-description\": \"Network Collection service for vLAN tagging\",\n" +
            "                \"resource-version\": \"1534444087221\"\n" +
            "            }\n" +
            "        }]}", ModelVersions.class);


        final AaiClient aaiClient = new AaiClient(null, null, null);

        assertThat(aaiClient.toModelVerStream(modelVersions).collect(toList()),
            containsInAnyOrder(
                hasProperty("modelVersionId", is("a92f899d-a3ec-465b-baed-1663b0a5aee1")),
                hasProperty("modelVersionId", is("d2fda667-e92e-4cfa-9620-5da5de01a319"))
            ));

    }
}
