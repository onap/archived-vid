package org.onap.vid.aai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.mockito.Mockito;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.model.probes.StatusMetadata;
import org.onap.vid.controllers.LocalWebConfig;
import org.onap.vid.testUtils.TestUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.security.provider.certpath.SunCertPathBuilderException;
import sun.security.validator.ValidatorException;

import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.ServletContext;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
@WebAppConfiguration
public class AaiClientTest {

    private AaiClient aaiClientMock;
    private ServletContext servletContext;

    @BeforeMethod
    public void initMocks(){
        aaiClientMock = mock(AaiClient.class);
        aaiClientMock.logger = mock(EELFLoggerDelegate.class);
        servletContext = mock(ServletContext.class);

        when(servletContext.getRealPath(any(String.class))).thenReturn("");

        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(null);
    }

    @DataProvider
    public static Object[][] logicalLinkData() {
        return new Object[][] {
                {"", "network/logical-links/logical-link/"},
                {"link", "network/logical-links/logical-link/link"}
        };
    }

    @Test(dataProvider = "logicalLinkData")
    public void getLogicalLink_Link_Is_Empty(String link, String expectedUrl) {

        when(aaiClientMock.getLogicalLink(any(String.class))).thenCallRealMethod();
        aaiClientMock.getLogicalLink(link);
        Mockito.verify(aaiClientMock).doAaiGet(argThat(equalToIgnoringCase(expectedUrl)),any(Boolean.class));
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
        Mockito.when(aaiClientMock.probeAaiGetAllSubscribers()).thenCallRealMethod();
        ExternalComponentStatus result  = aaiClientMock.probeAaiGetAllSubscribers();
        assertThat(statusDataReflected(result),is(statusDataReflected(expectedStatus)));
        assertThat(requestMetadataReflected(result.getMetadata()),is(requestMetadataReflected(expectedStatus.getMetadata())));
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
        Mockito.when(aaiClientMock.probeAaiGetAllSubscribers()).thenCallRealMethod();
        ExternalComponentStatus result  = aaiClientMock.probeAaiGetAllSubscribers();
        assertFalse(result.isAvailable());
        return result;
    }


    @Test
    public void getTenants_Arguments_Are_Null_Or_Empty() {

        when(aaiClientMock.getTenants(any(String.class), any(String.class))).thenCallRealMethod();

        AaiResponse response = aaiClientMock.getTenants("", "");

        assertEquals(response.getErrorMessage(), "{\"statusText\":\" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.\"}");


        response = aaiClientMock.getTenants(null, null);

        assertEquals(response.getErrorMessage(), "{\"statusText\":\" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.\"}");
    }

    @Test
    public void getTenants_Arguments_Are_Valid_But_Tenants_Not_Exist() {

        when(aaiClientMock.getTenants(any(String.class), any(String.class))).thenCallRealMethod();

        Response generalEmptyResponse = mock(Response.class);
        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(generalEmptyResponse);

        AaiResponse response = aaiClientMock.getTenants("subscriberId", "serviceType");

        assertEquals(response.getErrorMessage(), "{\"statusText\":\" A&AI has no LCP Region & Tenants associated to subscriber 'subscriberId' and service type 'serviceType'\"}");

    }

    @Test
    public void getTenants_Arguments_Are_Valid_Get_The_Tenanats() {

        when(aaiClientMock.getTenants(any(String.class), any(String.class))).thenCallRealMethod();


        Response generalEmptyResponse = mock(Response.class);

        when(generalEmptyResponse.readEntity(String.class)).thenReturn(tenantResponseRaw);
        when(generalEmptyResponse.getStatus()).thenReturn(200);
        when(generalEmptyResponse.getStatusInfo()).thenReturn(new Response.StatusType() {
            @Override
            public int getStatusCode() {
                return 200;
            }

            @Override
            public Response.Status.Family getFamily() {
                return Response.Status.Family.SUCCESSFUL;
            }

            @Override
            public String getReasonPhrase() {
                return null;
            }
        });


        when(aaiClientMock.doAaiGet(any(String.class),any(Boolean.class))).thenReturn(generalEmptyResponse);

        AaiResponse<GetTenantsResponse[]> response = aaiClientMock.getTenants("subscriberId", "serviceType");

        Assert.assertTrue(response.t.length> 0);
    }

    final String tenantResponseRaw ="" +
            "{" +
            "\"service-type\": \"VIRTUAL USP\"," +
            "\"resource-version\": \"1494001841964\"," +
            "\"relationship-list\": {" +
            "\"relationship\": [{" +
            "\"related-to\": \"tenant\"," +
            "\"related-link\": \"/aai/v11/cloud-infrastructure/cloud-regions/cloud-region/att-aic/AAIAIC25/tenants/tenant/092eb9e8e4b7412e8787dd091bc58e86\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"cloud-region.cloud-owner\"," +
            "\"relationship-value\": \"att-aic\"" +
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

    @DataProvider
    public static Object[][] resourceTypesProvider() {
        return new Object[][] {
                {"service-instance", ResourceType.SERVICE_INSTANCE},
                {"generic-vnf", ResourceType.GENERIC_VNF},
                {"vf-module", ResourceType.VF_MODULE}
        };
    }

    @Test(dataProvider = "resourceTypesProvider")
    public void aaiNodeQueryResponseDeserializationTest(String resourceType, ResourceType expectedResourceType) throws IOException {
        String link = "/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Nimbus/service-instances/service-instance/7131d483-b450-406f-8e30-0c650645fc67";
        String json =
                "{\"result-data\": [{" +
                    "\"resource-type\": \""+resourceType+"\"," +
                    "\"resource-link\": \""+ link+ "\"" +
                "}]}";

        AaiNodeQueryResponse nodeQueryResponse = new ObjectMapper().readValue(json, AaiNodeQueryResponse.class);
        assertThat(nodeQueryResponse.resultData.get(0).resourceLink, equalTo(link));
        assertThat(nodeQueryResponse.resultData.get(0).resourceType, is(expectedResourceType));
    }

    @Test
    public void aaiNodeQueryEmptyResponseDeserializationTest() throws IOException{
        String json = "{}";
        AaiNodeQueryResponse nodeQueryResponse = new ObjectMapper().readValue(json, AaiNodeQueryResponse.class);
        assertNull(nodeQueryResponse.resultData);
    }

    @DataProvider
    public static Object[][] nameAndResourceTypeProvider() {
        return new Object[][] {
                {"SRIOV_SVC", ResourceType.SERVICE_INSTANCE, "search/nodes-query?search-node-type=service-instance&filter=service-instance-name:EQUALS:SRIOV_SVC"},
                {"b1707vidnf", ResourceType.GENERIC_VNF, "search/nodes-query?search-node-type=generic-vnf&filter=vnf-name:EQUALS:b1707vidnf"},
                {"connectivity_test", ResourceType.VF_MODULE, "search/nodes-query?search-node-type=vf-module&filter=vf-module-name:EQUALS:connectivity_test"},
                {"MjVg1234", ResourceType.VOLUME_GROUP, "search/nodes-query?search-node-type=volume-group&filter=volume-group-name:EQUALS:MjVg1234"}
        };
    }

    @Test(dataProvider = "nameAndResourceTypeProvider")
    public void whenSearchNodeTypeByName_callRightAaiPath(String name, ResourceType type, String expectedUrl) {
        when(aaiClientMock.searchNodeTypeByName(any(String.class), any(ResourceType.class))).thenCallRealMethod();
        aaiClientMock.searchNodeTypeByName(name, type);
        Mockito.verify(aaiClientMock).doAaiGet(eq(expectedUrl), eq(false));
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
                    when(javaxClientMock.target(anyString())).thenThrow(
                            new ProcessingException(new SSLHandshakeException("Received fatal alert: certificate_expired"))
                    );
                }),

                Pair.of(SunCertPathBuilderException.class, (httpsAuthClientMock, javaxClientMock) -> {
                    SunCertPathBuilderException e0 = new SunCertPathBuilderException("unable to find valid certification path to requested target");
                    when(javaxClientMock.target(anyString())).thenThrow(
                            new ProcessingException(new ValidatorException("PKIX path building failed: " + e0.toString(), e0))
                    );
                }),

                Pair.of(GenericUncheckedException.class, (httpsAuthClientMock, javaxClientMock) ->
                        when(javaxClientMock.target(anyString())).thenThrow(new GenericUncheckedException("basa")))

        ).flatMap(l -> Stream.of(
                // double each case to propagateExceptions = true/false, to verify that "don't propagate" really still work
                ImmutableList.of(l.getLeft(), l.getRight(), true).toArray(),
                ImmutableList.of(l.getLeft(), l.getRight(), false).toArray()
        )).collect(Collectors.toList()).toArray(new Object[][]{});
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
        AAIRestInterface aaiRestInterface = new AAIRestInterface(httpsAuthClientMock);
        final AaiClient aaiClient = new AaiClient(aaiRestInterface, null);
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
}
