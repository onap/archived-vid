package org.onap.vid.asdc.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.fail;

public class RestfulAsdcClientTest {

    @DataProvider
    public static Object[][] javaxExceptions() {

        return new Object[][] {
                {NotFoundException.class, (Consumer<Client>) javaxClientMock ->
                        when(javaxClientMock.target(any(URI.class))).thenThrow(
                                new NotFoundException("HTTP 404 Not Found"))},
                {ProcessingException.class, (Consumer<Client>) javaxClientMock ->
                        when(javaxClientMock.target(any(URI.class))).thenThrow(
                                new ProcessingException("java.net.ConnectException: Connection refused: connect"))},
        };
    }


    @Test(dataProvider = "javaxExceptions")
    public void whenJavaxClientThrowException_thenExceptionRethrown(Class<? extends Throwable> expectedType, Consumer<Client> setupMocks) throws Exception {
        /*
        Call chain is like:
            this test -> RestfulAsdcClient ->  javax's Client

        In this test, *RestfulAsdcClient* is under test (actual implementation is used), while javax's Client is
        mocked to return pseudo-responses or - better - throw exceptions.
         */

        // prepare mocks
        TestUtils.JavaxRsClientMocks mocks = new TestUtils.JavaxRsClientMocks();
        Client javaxClientMock = mocks.getFakeClient();

        // prepare real RestfulAsdcClient (Under test)
        RestfulAsdcClient restfulAsdcClient = new RestfulAsdcClient.Builder(javaxClientMock, new URI(""))
                .auth("")
                .build();

        /// TEST:
        setupMocks.accept(javaxClientMock);

        try {
            restfulAsdcClient.getServiceToscaModel(UUID.randomUUID());
        } catch (Exception e) {
            assertThat("root cause incorrect for " + ExceptionUtils.getStackTrace(e), ExceptionUtils.getRootCause(e), instanceOf(expectedType));
            return; //OK
        }

        fail("exception shall rethrown by getServiceToscaModel once javax client throw exception ");
    }

}
