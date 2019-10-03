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

package org.onap.vid.mso.rest;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.controller.filter.PromiseRequestIdFilter;
import org.onap.vid.logging.Headers;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class OutgoingRequestHeadersTest {

    private static final PromiseRequestIdFilter promiseRequestIdFilter = new PromiseRequestIdFilter();

    @InjectMocks
    private RestMsoImplementation restMsoImplementation;

    @Mock
    private SystemPropertyHelper systemPropertyHelper;

    @Mock
    private SystemPropertiesWrapper  systemPropertiesWrapper;

    @Mock
    private HttpsAuthClient httpsAuthClient;

    @Mock
    private ServletRequestHelper servletRequestHelper;

    @Mock
    private Logging loggingService;

    @InjectMocks
    private AAIRestInterface aaiRestInterface;

    @Captor
    private ArgumentCaptor<MultivaluedMap<String, Object>> multivaluedMapArgumentCaptor;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(servletRequestHelper.extractOrGenerateRequestId()).thenAnswer(invocation -> UUID.randomUUID().toString());
        when(systemPropertiesWrapper.getProperty(MsoProperties.MSO_PASSWORD)).thenReturn("OBF:1vub1ua51uh81ugi1u9d1vuz");
    }

    @BeforeMethod
    private void setup() {
        putRequestInSpringContext();
    }

    public static void putRequestInSpringContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
            (HttpServletRequest) promiseRequestIdFilter.wrapIfNeeded(new MockHttpServletRequest())));
    }

    @DataProvider
    public Object[][] msoMethods() {
        return Stream.<ThrowingConsumer<RestMsoImplementation>>of(
                client -> client.GetForObject("/any path", Object.class),
                client -> client.restCall(HttpMethod.DELETE, Object.class, "some payload", "/any path", Optional.of("userId")),
                client -> client.PostForObject("some payload", "/any path", Object.class)
        ).map(l -> ImmutableList.of(l).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "msoMethods")
    public void mso(Consumer<RestMsoImplementation> f) throws Exception {
        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(restMsoImplementation);

        f.accept(restMsoImplementation);

        Invocation.Builder fakeBuilder = mocks.getFakeBuilder();
        Object requestIdValue = verifyXEcompRequestIdHeaderWasAdded(fakeBuilder);
        assertEquals(requestIdValue, captureHeaderKeyAndReturnItsValue(fakeBuilder, "X-ONAP-RequestID"));
        Object invocationId1 = assertRequestHeaderIsUUID(fakeBuilder, "X-InvocationID");
        assertThat((String) captureHeaderKeyAndReturnItsValue(fakeBuilder, "Authorization"), startsWith("Basic "));
        verifyXOnapPartnerNameHeaderWasAdded(fakeBuilder);

        //validate requestId is same in next call but invocationId is different

        //given
        final TestUtils.JavaxRsClientMocks mocks2 = setAndGetMocksInsideRestImpl(restMsoImplementation);

        //when
        f.accept(restMsoImplementation);
        Invocation.Builder fakeBuilder2 = mocks2.getFakeBuilder();

        //then
        Object requestIdValue2 = verifyXEcompRequestIdHeaderWasAdded(fakeBuilder2);
        assertEquals(requestIdValue, requestIdValue2);

        Object invocationId2 = assertRequestHeaderIsUUID(fakeBuilder2, "X-InvocationID");
        assertNotEquals(invocationId1, invocationId2);
    }

    @Test
    public void whenProvideMsoRestCallUserId_builderHasXRequestorIDHeader() throws Exception {

        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(restMsoImplementation);
        String randomUserName = randomAlphabetic(10);

        restMsoImplementation.restCall(HttpMethod.DELETE, String.class, null, "abc", Optional.of(randomUserName));
        assertEquals(randomUserName, captureHeaderKeyAndReturnItsValue(mocks.getFakeBuilder(), "X-RequestorID"));
    }

    @DataProvider
    public Object[][] aaiMethods() {
        return Stream.<ThrowingConsumer<AAIRestInterface>>of(

                client -> client.RestGet("from app id", "some transId", Unchecked.toURI("/any path"), false),
                client -> client.Delete("whatever source id", "some transId", "/any path"),
                client -> client.RestPost("from app id", "/any path", "some payload", false),
                client -> client.RestPut("from app id", "/any path", "some payload", false, false)

        ).map(l -> ImmutableList.of(l).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "aaiMethods")
    public void aai(Consumer<AAIRestInterface> f) throws Exception {
        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(aaiRestInterface);

        f.accept(aaiRestInterface);

        verifyXEcompRequestIdHeaderWasAdded(mocks.getFakeBuilder());
        verifyXOnapPartnerNameHeaderWasAdded(mocks.getFakeBuilder());
    }

//    @Test(dataProvider = "schedulerMethods")
//    public void scheduler(Consumer<AAIRestInterface> f) throws Exception {
//
//        This test os not feasible in the wat acheduler is implemented today,
//        as Scheduler's client is rewritten in every call.
//
//        :-(
//
//    }

    private Object verifyXEcompRequestIdHeaderWasAdded(Invocation.Builder fakeBuilder) {
        final String requestIdHeader = "x-ecomp-requestid";
        return assertRequestHeaderIsUUID(fakeBuilder, requestIdHeader);
    }

    private Object assertRequestHeaderIsUUID(Invocation.Builder fakeBuilder, String headerName) {
        Object headerValue = captureHeaderKeyAndReturnItsValue(fakeBuilder, headerName);
        final String uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        assertThat("header '" + headerName + "' should be a uuid", headerValue,
                allOf(instanceOf(String.class), hasToString(matchesPattern(uuidRegex))));
        return headerValue;
    }

    private void verifyXOnapPartnerNameHeaderWasAdded(Invocation.Builder fakeBuilder) {
        assertThat(
            captureHeaderKeyAndReturnItsValue(fakeBuilder, Headers.PARTNER_NAME.getHeaderName()),
            is("VID.VID")
        );
    }

    private Object captureHeaderKeyAndReturnItsValue(Invocation.Builder fakeBuilder, String headerName) {
        // Checks that the builder was called with either one of header("x-ecomp-requestid", uuid)
        // or the plural brother: headers(Map.of("x-ecomp-requestid", Set.of(uuid))

        Object requestId;
        // The 'verify()' will capture the request id. If no match -- AssertionError will
        // catch for a second chance -- another 'verify()'.
        try {
            try {
                ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
                Mockito.verify(fakeBuilder)
                    .header(
                        Matchers.argThat(s -> equalsIgnoreCase(s, headerName)),
                        argumentCaptor.capture()
                    );
                requestId = argumentCaptor.getValue();

            } catch (AssertionError e) {
                Mockito.verify(fakeBuilder).headers(multivaluedMapArgumentCaptor.capture());

                final MultivaluedMap<String, Object> headersMap = multivaluedMapArgumentCaptor.getValue();
                final String thisRequestIdHeader = getFromSetCaseInsensitive(headersMap.keySet(), headerName);

                assertThat(headersMap.keySet(), hasItem(thisRequestIdHeader));
                requestId = headersMap.getFirst(thisRequestIdHeader);
            }
        } catch (AssertionError e) {
            throw new AssertionError("header not captured: " + headerName, e);
        }
        return requestId;
    }

    private String getFromSetCaseInsensitive(Set<String> set, String key) {
        return set.stream()
                .filter(anotherString -> anotherString.equalsIgnoreCase(key))
                .findFirst()
                .orElse(key);
    }

    private TestUtils.JavaxRsClientMocks setAndGetMocksInsideRestImpl(Class<?> clazz) throws IllegalAccessException {
        TestUtils.JavaxRsClientMocks mocks = new TestUtils.JavaxRsClientMocks();
        Client fakeClient = mocks.getFakeClient();

        FieldUtils.writeStaticField(clazz, "client", fakeClient, true);

        return mocks;
    }

    private TestUtils.JavaxRsClientMocks setAndGetMocksInsideRestImpl(Object instance) throws IllegalAccessException {
        TestUtils.JavaxRsClientMocks mocks = new TestUtils.JavaxRsClientMocks();
        Client fakeClient = mocks.getFakeClient();

        FieldUtils.writeField(instance, "client", fakeClient, true);

        return mocks;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> extends Consumer<T> {
        @Override
        default void accept(T t) {
            try {
                acceptThrows(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        void acceptThrows(T t) throws Exception;
    }

}
