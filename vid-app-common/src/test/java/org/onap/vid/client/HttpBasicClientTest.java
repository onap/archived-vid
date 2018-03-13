package org.onap.vid.client;

import org.junit.Test;

public class HttpBasicClientTest {

    private HttpBasicClient createTestSubject() {
        return new HttpBasicClient();
    }

    @Test
    public void testGetClient() throws Exception {
        // default test
        HttpBasicClient.getClient();
    }
}