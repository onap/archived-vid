package org.onap.vid.mso.rest;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.controller.filter.PromiseEcompRequestIdFilter;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.testUtils.TestUtils;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.matchesPattern;


public class OutgoingRequestIdTest {


    @InjectMocks
    private RestMsoImplementation restMsoImplementation;

    @InjectMocks
    private AAIRestInterface aaiRestInterface;


    @Captor
    private ArgumentCaptor<MultivaluedMap<String, Object>> multivaluedMapArgumentCaptor;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    private void putRequestInSpringContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes((HttpServletRequest) PromiseEcompRequestIdFilter.wrapIfNeeded(new MockHttpServletRequest())));
    }

    @DataProvider
    public Object[][] msoMethods() {
        return Stream.<ThrowingConsumer<RestMsoImplementation>>of(

                client -> client.Get(new Object(), "whatever source id", "/any path", new RestObject<>()),
                client -> client.GetForObject("whatever source id", "/any path", Object.class),
                client -> client.Post(new Object(), "some payload", "whatever source id", "/any path", new RestObject<>()),
                client -> client.PostForObject("some payload", "whatever source id", "/any path", Object.class),
                client -> client.Put(Object.class, new RequestDetailsWrapper(), "whatever source id", "/any path", new RestObject<>())

        ).map(l -> ImmutableList.of(l).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "msoMethods")
    public void mso(Consumer<RestMsoImplementation> f) throws Exception {
        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(restMsoImplementation);

        f.accept(restMsoImplementation);

        verifyRequestIdHeaderWasAdded(mocks.getFakeBuilder());
    }

    @DataProvider
    public Object[][] aaiMethods() {
        return Stream.<ThrowingConsumer<AAIRestInterface>>of(

                client -> client.RestGet("from app id", "some transId", "/any path", false),
                client -> client.Delete("whatever source id", "some transId", "/any path"),
                client -> client.RestPost("from app id", "/any path", "some payload", false),
                client -> client.RestPut("from app id", "/any path", "some payload", false)

        ).map(l -> ImmutableList.of(l).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    //@Test(dataProvider = "aaiMethods")
    public void aai(Consumer<AAIRestInterface> f) throws Exception {
        final TestUtils.JavaxRsClientMocks mocks = setAndGetMocksInsideRestImpl(aaiRestInterface);

        f.accept(aaiRestInterface);

        verifyRequestIdHeaderWasAdded(mocks.getFakeBuilder());
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

    private void verifyRequestIdHeaderWasAdded(Invocation.Builder fakeBuilder) {
        final String requestIdHeader = "x-ecomp-requestid";
        final String uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

        // Checks that the builder was called with either one of header("x-ecomp-requestid", uuid)
        // or the plural brother: headers(Map.of("x-ecomp-requestid", Set.of(uuid))

        Object requestId;
        // The 'verify()' will capture the request id. If no match -- AssertionError will
        // catch for a second chance -- another 'verify()'.
        try {
            ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
            Mockito.verify(fakeBuilder)
                    .header(
                            Matchers.argThat(equalToIgnoringCase(requestIdHeader)),
                            argumentCaptor.capture()
                    );
            requestId = argumentCaptor.getValue();

        } catch (AssertionError e) {
            Mockito.verify(fakeBuilder).headers(multivaluedMapArgumentCaptor.capture());

            final MultivaluedMap<String, Object> headersMap = multivaluedMapArgumentCaptor.getValue();
            final String thisRequestIdHeader = getFromSetCaseInsensitive(headersMap.keySet(), requestIdHeader);

            assertThat(headersMap.keySet(), hasItem(thisRequestIdHeader));
            requestId = headersMap.getFirst(thisRequestIdHeader);
        }

        assertThat("header '" + requestIdHeader + "' should be a uuid", requestId,
                allOf(instanceOf(String.class), hasToString(matchesPattern(uuidRegex))));
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
