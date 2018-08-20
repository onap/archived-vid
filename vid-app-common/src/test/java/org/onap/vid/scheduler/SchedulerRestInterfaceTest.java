package org.onap.vid.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xebialabs.restito.semantics.Action;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.testUtils.StubServerUtil;
import org.onap.vid.utils.Logging;
import org.testng.annotations.BeforeMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;


@RunWith(MockitoJUnitRunner.class)
public class SchedulerRestInterfaceTest {

    private static final String USR_PWD_AUTH_STRING = "c2FtcGxlOnBhUyR3MFJk";
    private static final String SAMPLE_SCHEDULER_SERVER_URL = "http://localhost";
    private static final String APPLICATION_JSON = "application/json";
    private static final JSONParser JSON_PARSER = new JSONParser();
    static MultivaluedHashMap<String, Object> commonHeaders = new MultivaluedHashMap<>();
    private static StubServerUtil serverUtil;
    @Mock
    private HttpsAuthClient mockedHttpsAuthClient;
    @Mock
    private Client mockedClient;
    @Mock
    private Invocation.Builder mockedBuilder;
    @Mock
    private Response mockedResponse;
    @Mock
    private WebTarget mockedWebTarget;

    @InjectMocks
    private SchedulerRestInterface schedulerInterface = new SchedulerRestInterface();

    @BeforeClass
    public static void setUpClass() {
        serverUtil = new StubServerUtil();
        serverUtil.runServer();
        commonHeaders.put("Authorization", Collections.singletonList("Basic " + USR_PWD_AUTH_STRING));
    }

    @AfterClass
    public static void tearDownClass() {
        serverUtil.stopServer();
    }

    @BeforeMethod
    public void setUp() throws GeneralSecurityException, IOException {
        System.out.println("IN EXTENDED SETUP");
        MockitoAnnotations.initMocks(this);
        doReturn(mockedClient).when(mockedHttpsAuthClient).getClient(HttpClientMode.WITHOUT_KEYSTORE);

        schedulerInterface.initRestClient();
    }

    @Test
    public void testShouldGetOKWhenStringIsExpected() throws JsonProcessingException, ParseException {
        String sampleTargetUrl = serverUtil.constructTargetUrl("http", "test");
        String sampleSourceId = "AAI";
        RestObject<String> sampleRestObj = new RestObject<>();
        String resultHolder = "";

        String responseContent = "\"sample\": \"SAMPLE RESULT STRING\"";

        doReturn(200).when(mockedResponse).getStatus();
        doReturn(responseContent).when(mockedResponse).readEntity(String.class);
        doReturn(mockedResponse).when(mockedBuilder).get();
        doReturn(mockedBuilder).when(mockedBuilder).header(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId());
        doReturn(mockedBuilder).when(mockedBuilder).headers(commonHeaders);
        doReturn(mockedBuilder).when(mockedBuilder).accept(APPLICATION_JSON);
        doReturn(mockedBuilder).when(mockedWebTarget).request();
        doReturn(mockedWebTarget).when(mockedClient).target(SAMPLE_SCHEDULER_SERVER_URL + sampleTargetUrl);

        serverUtil.prepareGetCall("/test", responseContent, Action.ok());

        schedulerInterface.Get(resultHolder, sampleSourceId, sampleTargetUrl, sampleRestObj);

        assertResponseData(sampleRestObj, responseContent, 200);
    }


    @Test
    public void testShouldDeleteSuccessfully() throws JsonProcessingException, ParseException {
        String sampleTargetUrl = serverUtil.constructTargetUrl("http", "test");
        String sampleSourceId = "AAI";
        RestObject<String> sampleRestObj = new RestObject<>();
        String resultHolder = "";

        String responseContent = "\"sample\": \"SAMPLE RESULT STRING\"";

        doReturn(200).when(mockedResponse).getStatus();
        doReturn(responseContent).when(mockedResponse).readEntity(String.class);
        doReturn(mockedResponse).when(mockedBuilder).delete();
        doReturn(mockedBuilder).when(mockedBuilder).header(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId());
        doReturn(mockedBuilder).when(mockedBuilder).headers(commonHeaders);
        doReturn(mockedBuilder).when(mockedBuilder).accept(APPLICATION_JSON);
        doReturn(mockedBuilder).when(mockedWebTarget).request();
        doReturn(mockedWebTarget).when(mockedClient).target(SAMPLE_SCHEDULER_SERVER_URL + sampleTargetUrl);

        serverUtil.prepareDeleteCall("/test", responseContent, Action.ok());

        schedulerInterface.Delete(resultHolder, sampleSourceId, sampleTargetUrl, sampleRestObj);

        assertResponseData(sampleRestObj, responseContent, 200);
    }


    private void assertResponseData(RestObject<String> sampleRestObj, String expectedResponse, int expectedStatusCode) throws ParseException {
        Object parsedResult = JSON_PARSER.parse(sampleRestObj.get());

        assertThat(sampleRestObj.getStatusCode()).isEqualTo(expectedStatusCode);
        assertThat(parsedResult).isInstanceOf(String.class).isEqualTo(expectedResponse);
        assertThat(sampleRestObj.getUUID()).isNull();

    }

}
