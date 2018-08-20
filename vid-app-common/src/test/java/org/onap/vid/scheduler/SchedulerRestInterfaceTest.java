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


package org.onap.vid.scheduler;

import com.xebialabs.restito.semantics.Action;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.testUtils.StubServerUtil;
import org.testng.annotations.BeforeMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class SchedulerRestInterfaceTest {

    private static final String USR_PWD_AUTH_STRING = "c2FtcGxlOnBhUyR3MFJk";
    private static final String APPLICATION_JSON = "application/json";
    private static MultivaluedHashMap<String, Object> commonHeaders = new MultivaluedHashMap<>();
    private static StubServerUtil serverUtil;
    private String sampleBaseUrl;
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

    @Mock
    private Function<String, String> propertyGetter;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sampleBaseUrl = serverUtil.constructTargetUrl("http", "");
    }

    @Test
    public void testShouldGetOKWhenStringIsExpected() throws IOException, GeneralSecurityException {
        String sampleSourceId = "AAI";
        RestObject<String> sampleRestObj = new RestObject<>();
        String resultHolder = "";

        String responseContent = "sample : SAMPLE RESULT STRING";
        Mockito.doReturn(mockedClient).when(mockedHttpsAuthClient).getClient(HttpClientMode.WITHOUT_KEYSTORE);
        Mockito.doReturn("sample").when(propertyGetter).apply(SchedulerProperties.SCHEDULER_USER_NAME_VAL);
        Mockito.doReturn("paS$w0Rd").when(propertyGetter).apply(SchedulerProperties.SCHEDULER_PASSWORD_VAL);
        Mockito.doReturn(sampleBaseUrl).when(propertyGetter).apply(SchedulerProperties.SCHEDULER_SERVER_URL_VAL);
        Mockito.doReturn(200).when(mockedResponse).getStatus();
        Mockito.doReturn(responseContent).when(mockedResponse).readEntity(String.class);
        Mockito.doReturn(mockedResponse).when(mockedBuilder).get();
        Mockito.when(mockedBuilder.header(Matchers.any(), Matchers.any())).thenReturn(mockedBuilder);
        Mockito.doReturn(mockedBuilder).when(mockedBuilder).headers(commonHeaders);
        Mockito.doReturn(mockedBuilder).when(mockedBuilder).accept(APPLICATION_JSON);
        Mockito.doReturn(mockedBuilder).when(mockedWebTarget).request();
        Mockito.doReturn(mockedWebTarget).when(mockedClient).target(sampleBaseUrl + "test");

        serverUtil.prepareGetCall("/test", responseContent, Action.ok());

        schedulerInterface.Get(resultHolder, sampleSourceId, "test", sampleRestObj);

        assertResponseData(sampleRestObj, responseContent, 200);
    }


    @Test
    public void testShouldDeleteSuccessfully() throws IOException, GeneralSecurityException {
        String sampleTargetUrl = serverUtil.constructTargetUrl("http", "");
        String sampleSourceId = "AAI";
        RestObject<String> sampleRestObj = new RestObject<>();
        String resultHolder = "";

        String responseContent = "sample : SAMPLE RESULT STRING";
        Mockito.doReturn(mockedClient).when(mockedHttpsAuthClient).getClient(HttpClientMode.WITHOUT_KEYSTORE);
        Mockito.doReturn("sample").when(propertyGetter).apply(SchedulerProperties.SCHEDULER_USER_NAME_VAL);
        Mockito.doReturn("paS$w0Rd").when(propertyGetter).apply(SchedulerProperties.SCHEDULER_PASSWORD_VAL);
        Mockito.doReturn(sampleTargetUrl).when(propertyGetter).apply(SchedulerProperties.SCHEDULER_SERVER_URL_VAL);
        Mockito.doReturn(200).when(mockedResponse).getStatus();
        Mockito.doReturn(responseContent).when(mockedResponse).readEntity(String.class);
        Mockito.doReturn(mockedResponse).when(mockedBuilder).delete();
        Mockito.when(mockedBuilder.header(Matchers.any(), Matchers.any())).thenReturn(mockedBuilder);
        Mockito.doReturn(mockedBuilder).when(mockedBuilder).headers(commonHeaders);
        Mockito.doReturn(mockedBuilder).when(mockedBuilder).accept(APPLICATION_JSON);
        Mockito.doReturn(mockedBuilder).when(mockedWebTarget).request();
        Mockito.doReturn(mockedWebTarget).when(mockedClient).target(sampleTargetUrl + "test");

        serverUtil.prepareDeleteCall("/test", responseContent, Action.ok());

        schedulerInterface.Delete(resultHolder, sampleSourceId, "test", sampleRestObj);

        assertResponseData(sampleRestObj, responseContent, 200);
    }


    private void assertResponseData(RestObject<String> sampleRestObj, String expectedResponse, int expectedStatusCode) {

        assertThat(sampleRestObj.getStatusCode()).isEqualTo(expectedStatusCode);
        assertThat(sampleRestObj.get()).isInstanceOf(String.class).isEqualTo(expectedResponse);
        assertThat(sampleRestObj.getUUID()).isNull();

    }

}
