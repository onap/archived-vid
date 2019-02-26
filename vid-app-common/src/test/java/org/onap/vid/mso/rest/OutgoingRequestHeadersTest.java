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

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mockito.*;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.controller.filter.PromiseEcompRequestIdFilter;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Unchecked;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;


public class OutgoingRequestHeadersTest {


//    @InjectMocks
//    private RestMsoImplementation restMsoImplementation;

    @Mock
    private SystemPropertyHelper systemPropertyHelper;

    @Mock
    private ServletRequestHelper servletRequestHelper;

    @InjectMocks
    private AAIRestInterface aaiRestInterface;

    @Captor
    private ArgumentCaptor<MultivaluedMap<String, Object>> multivaluedMapArgumentCaptor;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(servletRequestHelper.extractOrGenerateRequestId()).thenAnswer(invocation -> UUID.randomUUID().toString());
    }

    @BeforeMethod
    private void putRequestInSpringContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes((HttpServletRequest) PromiseEcompRequestIdFilter.wrapIfNeeded(new MockHttpServletRequest())));
    }

//    @DataProvider
//    public Object[][] msoMethods() {
//        return Stream.<ThrowingConsumer<RestMsoImplementation>>of(
//
//                client -> client.Get(new Object(), "/any path", new RestObject<>(), false),
//                client -> client.GetForObject("/any path", Object.class),
//                client -> client.Post("", "some payload", "/any path", new RestObject<>()),
//                client -> client.PostForObject("some payload", "/any path", Object.class),
//                client -> client.Put(Object.class, new RequestDetailsWrapper(), "/any path", new RestObject<>())
//
//        ).map(l -> ImmutableList.of(l).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
//    }
//
//    @Test(dataProvider = "msoMethods")
//    public void mso(Consumer<RestMsoImplementation> f) throws Exception {
//        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(restMsoImplementation);
//
//        f.accept(restMsoImplementation);
//
//        Invocation.Builder fakeBuilder = mocks.getFakeBuilder();
//        Object requestIdValue = verifyXEcompRequestIdHeaderWasAdded(fakeBuilder);
//        assertEquals(requestIdValue, captureHeaderKeyAndReturnItsValue(fakeBuilder, "X-ONAP-RequestID"));
//
//        assertThat((String) captureHeaderKeyAndReturnItsValue(fakeBuilder, "Authorization"), startsWith("Basic "));
//        assertThat(captureHeaderKeyAndReturnItsValue(fakeBuilder, "X-ONAP-PartnerName"), equalTo("VID"));
//    }
//
//    @Test
//    public void whenProvideMsoRestCallUserId_builderHasXRequestorIDHeader() throws Exception {
//
//        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(restMsoImplementation);
//        String randomUserName = randomAlphabetic(10);
//
//        restMsoImplementation.restCall(HttpMethod.DELETE, String.class, null, "abc", Optional.of(randomUserName));
//        assertEquals(randomUserName, captureHeaderKeyAndReturnItsValue(mocks.getFakeBuilder(), "X-RequestorID"));
//    }

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
        final String uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        Object requestId = captureHeaderKeyAndReturnItsValue(fakeBuilder, requestIdHeader);

        assertThat("header '" + requestIdHeader + "' should be a uuid", requestId,
                allOf(instanceOf(String.class), hasToString(matchesPattern(uuidRegex))));
        return requestId;
    }

    private Object captureHeaderKeyAndReturnItsValue(Invocation.Builder fakeBuilder, String headerName) {
        // Checks that the builder was called with either one of header("x-ecomp-requestid", uuid)
        // or the plural brother: headers(Map.of("x-ecomp-requestid", Set.of(uuid))

        Object requestId;
        // The 'verify()' will capture the request id. If no match -- AssertionError will
        // catch for a second chance -- another 'verify()'.
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
