package org.onap.vid.logging;

import static org.testng.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ApacheClientMetricInterceptorTest {

    private ApacheClientMetricInterceptor interceptor;
    private final String path = "https://gerrit.onap.org/r/projects/vid/branches?n=16&S=0&m=master";
    private HttpGet request;
    private HttpResponse response;

    @BeforeMethod
    public void before() {
        interceptor = new ApacheClientMetricInterceptor() {};
        request = new HttpGet(path);
        response = new BasicHttpResponse(new ProtocolVersion("a",1,2), 200, "ok");
    }

    @Test
    public void testAddHeader() {
        interceptor.addHeader(request, "key", "value");
        assertEquals(request.getFirstHeader("key").getValue(), "value");
    }

    @Test
    public void testGetTargetServiceName() {
        assertEquals(path, interceptor.getTargetServiceName(request));
    }

    @Test
    public void testGetServiceName() {
        assertEquals(path, interceptor.getTargetServiceName(request));
    }

    @Test
    public void testGetHttpStatusCode() {
        assertEquals(200, interceptor.getHttpStatusCode(response));
    }

    @Test
    public void testGetResponseCode() {
        assertEquals("ok", interceptor.getResponseCode(response));
    }

    @Test
    public void testGetTargetEntity() {

    }
}
