package org.onap.vid.aai.util;

import org.junit.Test;

public class HttpsAuthClientTest {

    private HttpsAuthClient createTestSubject() {
        return new HttpsAuthClient();
    }

    @Test
    public void testGetClient() throws Exception {
        String certFilePath = "";

        // default test
        HttpsAuthClient.getClient(certFilePath);
    }
}