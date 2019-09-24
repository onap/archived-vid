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

package org.onap.vid.controller.filter;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.onap.vid.logging.RequestIdHeader;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class PromiseRequestIdFilterTest {

    private final String anotherHeader = "ANDREI_RUBLEV";
    private final String anotherValue = "foo value";
    private final String mixedCaseHeader = "x-ecomp-REQUESTID";

    private static final String onapRequestIdHeader = "x-onap-requestid";
    private static final String transactionIdHeader = "x-transactionid";
    private static final String requestIdHeader = "x-requestid";

    private final PromiseRequestIdFilter promiseRequestIdFilter = new PromiseRequestIdFilter();

    @Test
    public void givenRequestIdHeader_headerValueNotChanged() throws IOException, ServletException {

        final String someTxId = "863850e2-8545-4efd-94b8-afba5f52b3d5";

        final ImmutableMap<String, String> incomingRequestHeaders = ImmutableMap.of(
                anotherHeader, anotherValue,
                ECOMP_REQUEST_ID, someTxId
        );

        buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(incomingRequestHeaders, specificTxId(someTxId));
    }


    @Test
    public void givenRequestIdHeaderThatIsNotAUUID_headerValueChanged() throws IOException, ServletException {

        final String someTxId = "863850e28544efd94b8afba5f52b3d5";

        final ImmutableMap<String, String> incomingRequestHeaders = ImmutableMap.of(
                anotherHeader, anotherValue,
                ECOMP_REQUEST_ID, someTxId
        );

        buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(incomingRequestHeaders, UserUtils::getRequestId);
    }


    @Test
    public void givenMixedCaseRequestIdHeader_headerValueNotChanged() throws IOException, ServletException {

        final String someTxId = "729bbd8d-b0c2-4809-a794-dcccd9cda2c0";

        final ImmutableMap<String, String> incomingRequestHeaders = ImmutableMap.of(
                mixedCaseHeader, someTxId,
                anotherHeader, anotherValue
        );

        buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(incomingRequestHeaders, specificTxId(someTxId));
    }

    @Test
    public void givenNoRequestIdHeader_headerValueWasGenerated() throws IOException, ServletException {

        final ImmutableMap<String, String> incomingRequestHeaders = ImmutableMap.of(
                anotherHeader, anotherValue
        );

        buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(incomingRequestHeaders, UserUtils::getRequestId);
    }

    @Test
    public void givenTwoRequestIdHeader_onapHeaderValueIsUsed() throws IOException, ServletException {

        final String onapTxId = "863850e2-8545-4efd-94b8-AFBA5F52B3D5"; // note mixed case
        final String ecompTxId = "6e8ff89e-88a4-4977-b63f-3142892b6e08";

        final ImmutableMap<String, String> incomingRequestHeaders = ImmutableMap.of(
            anotherHeader, anotherValue,
            ECOMP_REQUEST_ID, ecompTxId,
            onapRequestIdHeader, onapTxId
        );

        buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(incomingRequestHeaders, specificTxId(onapTxId));
    }

    @Test
    public void givenTwoRequestIdHeaderAndHigherPriorityIsMalformed_headerValueIsGenerated() throws IOException, ServletException {

        final String malformedTxId = "6e8ff89e-88a4-4977-b63f-3142892b6e08-";
        final String anotherTxId = "863850e2-8545-4efd-94b8-afba5f52b3d5";

        final ImmutableMap<String, String> incomingRequestHeaders = ImmutableMap.of(
            anotherHeader, anotherValue,
            requestIdHeader, malformedTxId, // requestIdHeader as higher priority than transactionIdHeader
            transactionIdHeader, anotherTxId
        );

        HttpServletRequest wrappedRequest =
            buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(incomingRequestHeaders, UserUtils::getRequestId);

        assertThat(UserUtils.getRequestId(wrappedRequest),
            not(anyOf(equalTo(malformedTxId), equalTo(anotherTxId)))
        );
    }


    @Test
    public void toUuidOrElse_givenValid_yieldSame() {
        final String someTxId = "729bbd8d-b0c2-4809-a794-DCCCD9CDA2C0"; // note mixed case
        UUID unexpected = UUID.randomUUID();
        assertThat(promiseRequestIdFilter.toUuidOrElse(someTxId, () -> unexpected), is(UUID.fromString(someTxId)));
    }

    @Test
    public void toUuidOrElse_givenNull_yieldSupplier() {
        UUID expected = UUID.fromString("729bbd8d-b0c2-4809-a794-dcccd9cda2c0");
        assertThat(promiseRequestIdFilter.toUuidOrElse(null, () -> expected), is(expected));
    }

    @Test
    public void toUuidOrElse_givenMalformed_yieldSupplier() {
        UUID expected = UUID.fromString("729bbd8d-b0c2-4809-a794-dcccd9cda2c0");
        assertThat(promiseRequestIdFilter.toUuidOrElse("malformed uuid", () -> expected), is(expected));
    }

    @DataProvider
    public static Object[][] severalRequestIdHeaders() {
        String someTxId = "69fa2575-d7f2-482c-ad1b-53a63ca03617";
        String anotherTxId = "06de373b-7e19-4357-9bd1-ed95682ae3a4";

        return new Object[][]{
            {
                "header is selected when single", RequestIdHeader.TRANSACTION_ID,
                ImmutableMap.of(
                    transactionIdHeader, someTxId
                )
            }, {
                "header is selected when first", RequestIdHeader.ONAP_ID,
                ImmutableMap.of(
                    onapRequestIdHeader, someTxId,
                    "noise-header", anotherTxId,
                    ECOMP_REQUEST_ID, anotherTxId
                )
            }, {
                "header is selected when last", RequestIdHeader.ONAP_ID,
                ImmutableMap.of(
                    ECOMP_REQUEST_ID, anotherTxId,
                    "noise-header", anotherTxId,
                    onapRequestIdHeader, someTxId
                )
            }, {
                "header is selected when value is invalid uuid", RequestIdHeader.ONAP_ID,
                ImmutableMap.of(
                    onapRequestIdHeader, "invalid-uuid"
                )
            }, {
                "header is selected when no ecomp-request-id", RequestIdHeader.ONAP_ID,
                ImmutableMap.of(
                    requestIdHeader, anotherTxId,
                    onapRequestIdHeader, someTxId
                )
            }, {
                "ECOMP_REQUEST_ID is returned when no request-id header", RequestIdHeader.ECOMP_ID,
                ImmutableMap.of(
                    "tsamina-mina", anotherTxId,
                    "waka-waka", anotherTxId
                )
            },
        };
    }

    @Test(dataProvider = "severalRequestIdHeaders")
    public void highestPriorityHeader_givenSeveralRequestIdHeaders_correctHeaderIsUsed(String description, RequestIdHeader expectedHeader, Map<String, String> incomingRequestHeaders) {

        HttpServletRequest mockedHttpServletRequest = createMockedHttpServletRequest(incomingRequestHeaders);

        assertThat(description,
            promiseRequestIdFilter.highestPriorityHeader(mockedHttpServletRequest), is(expectedHeader));
    }


    private HttpServletRequest buildRequestThenRunThroughFilterAndAssertResultRequestHeaders(
            ImmutableMap<String, String> originalRequestHeaders,
            Function<HttpServletRequest, String> txIdExtractor
    ) throws IOException, ServletException {
        HttpServletRequest servletRequest = createMockedHttpServletRequest(originalRequestHeaders);
        HttpServletResponse servletResponse = createMockedHttpServletResponse();

        final FilterChain capturingFilterChain = Mockito.mock(FilterChain.class);

        //////////////////
        //
        // doFilter() is the function under test
        //
        promiseRequestIdFilter.doFilter(servletRequest, servletResponse, capturingFilterChain);
        //
        //////////////////

        final ServletRequest capturedServletRequest = extractCapturedServletRequest(capturingFilterChain);
        final ServletResponse capturedServletResponse = extractCapturedServletResponse(capturingFilterChain);
        final String expectedTxId = txIdExtractor.apply((HttpServletRequest) capturedServletRequest);

        assertRequestObjectHeaders(capturedServletRequest, expectedTxId);
        assertResponseObjectHeaders(capturedServletResponse, expectedTxId);

        return (HttpServletRequest) capturedServletRequest;
    }


    private void assertRequestObjectHeaders(ServletRequest request, String expectedTxId) {
        /*
        Assert that:
        - Two headers are in place
        - Direct value extraction is as expected
        - UserUtils.getRequestId() returns correct and valid value
         */
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        assertThat(Collections.list(httpServletRequest.getHeaderNames()),
                hasItems(equalToIgnoringCase(ECOMP_REQUEST_ID), equalToIgnoringCase(anotherHeader)));

        assertThat(httpServletRequest.getHeader(anotherHeader), is(anotherValue));

        assertThat(httpServletRequest.getHeader(ECOMP_REQUEST_ID), equalToIgnoringCase(expectedTxId));
        assertThat(httpServletRequest.getHeader(mixedCaseHeader), equalToIgnoringCase(expectedTxId));

        assertThat(UserUtils.getRequestId(httpServletRequest), equalToIgnoringCase(expectedTxId));
        assertThat(UserUtils.getRequestId(httpServletRequest), is(not(emptyOrNullString())));
    }

    private void assertResponseObjectHeaders(ServletResponse response, String txId) {
        final String REQUEST_ID_HEADER_NAME_IN_RESPONSE = mixedCaseHeader + "-echo";
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        assertThat("header " + REQUEST_ID_HEADER_NAME_IN_RESPONSE.toLowerCase() + " in response must be provided",
                httpServletResponse.getHeader(REQUEST_ID_HEADER_NAME_IN_RESPONSE), equalToIgnoringCase(txId));
    }



    private HttpServletRequest createMockedHttpServletRequest(Map<String, String> requestHeaders) {
        HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
        requestHeaders.forEach((k, v) -> {
            Mockito.when(servletRequest.getHeader(argThat(s -> equalsIgnoreCase(s, k)))).thenReturn(v);
            Mockito.when(servletRequest.getHeaders(argThat(s -> equalsIgnoreCase(s, k)))).then(returnEnumerationAnswer(v));
        });
        Mockito.when(servletRequest.getHeaderNames()).then(returnEnumerationAnswer(requestHeaders.keySet()));
        return servletRequest;
    }

    private HttpServletResponse createMockedHttpServletResponse() {
        return new MockHttpServletResponse();
    }

    private static Answer<Enumeration<String>> returnEnumerationAnswer(String ... items) {
        return returnEnumerationAnswer(Arrays.asList(items));
    }

    private static Answer<Enumeration<String>> returnEnumerationAnswer(Collection<String> items) {
        return invocation -> Collections.enumeration(items);
    }

    private Function<HttpServletRequest, String> specificTxId(String someTxId) {
        return r -> someTxId;
    }

    private ServletRequest extractCapturedServletRequest(FilterChain capturingFilterChain) throws IOException, ServletException {
        ArgumentCaptor<ServletRequest> captor = ArgumentCaptor.forClass(ServletRequest.class);
        Mockito.verify(capturingFilterChain).doFilter(captor.capture(), any());
        return captor.getValue();
    }

    private ServletResponse extractCapturedServletResponse(FilterChain capturingFilterChain) throws IOException, ServletException {
        ArgumentCaptor<ServletResponse> captor = ArgumentCaptor.forClass(ServletResponse.class);
        Mockito.verify(capturingFilterChain).doFilter(any(), captor.capture());
        return captor.getValue();
    }

}
