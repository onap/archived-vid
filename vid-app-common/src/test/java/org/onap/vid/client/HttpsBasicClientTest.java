package org.onap.vid.client;

import org.junit.Test;

public class HttpsBasicClientTest {

    private HttpsBasicClient createTestSubject() {
        return new HttpsBasicClient();
    }

    @Test
    public void testGetClient() throws Exception {

        // default test
        HttpsBasicClient.getClient();
    }
}