package org.onap.vid.aai.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import javax.ws.rs.core.Response;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.springframework.http.HttpMethod;

public class TestWithAaiClient {

    protected ResponseWithRequestInfo mockedResponseWithRequestInfo(Response.Status status, String entity) {
        return mockedResponseWithRequestInfo(status, entity, "/my/mocked/url", HttpMethod.GET);
    }

    protected ResponseWithRequestInfo mockedResponseWithRequestInfo(Response.Status status, String entity, String requestUrl, HttpMethod method) {
        final Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(status.getStatusCode());
        when(mockResponse.getStatusInfo()).thenReturn(status);
        when(mockResponse.readEntity(String.class)).thenReturn(entity);
        return new ResponseWithRequestInfo(mockResponse, requestUrl, method);
    }

    protected void mockForGetRequest(AAIRestInterface aaiRestInterface, ResponseWithRequestInfo responseWithRequestInfo) {
        when(aaiRestInterface.doRest(anyString(), anyString(), any(URI.class), isNull(), eq(HttpMethod.GET), anyBoolean(), anyBoolean()))
                .thenReturn(responseWithRequestInfo);
    }

    protected void mockForPutRequest(AAIRestInterface aaiRestInterface, ResponseWithRequestInfo responseWithRequestInfo) {
        when(aaiRestInterface.doRest(anyString(), anyString(), any(URI.class), anyString(), eq(HttpMethod.PUT), anyBoolean(), anyBoolean()))
                .thenReturn(responseWithRequestInfo);
    }
}
