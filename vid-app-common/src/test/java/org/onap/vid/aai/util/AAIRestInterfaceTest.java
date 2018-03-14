package org.onap.vid.aai.util;

import org.junit.Test;

public class AAIRestInterfaceTest {

    private AAIRestInterface createTestSubject() {
        return new AAIRestInterface("");
    }

    @Test
    public void testEncodeURL() throws Exception {
        AAIRestInterface testSubject;
        String nodeKey = "";
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.encodeURL(nodeKey);
    }

    @Test
    public void testSetRestSrvrBaseURL() throws Exception {
        AAIRestInterface testSubject;
        String baseURL = "";

        // test 1
        testSubject = createTestSubject();
        baseURL = null;
        testSubject.SetRestSrvrBaseURL(baseURL);

        // test 2
        testSubject = createTestSubject();
        baseURL = "";
        testSubject.SetRestSrvrBaseURL(baseURL);
    }

    @Test
    public void testGetRestSrvrBaseURL() throws Exception {
        AAIRestInterface testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRestSrvrBaseURL();
    }


    @Test
    public void testRestPut() throws Exception {
        AAIRestInterface testSubject;
        String fromAppId = "";
        String transId = "";
        String path = "";
        String payload = "";
        boolean xml = false;

        // default test
        testSubject = createTestSubject();
        testSubject.RestPut(fromAppId, transId, path, payload, xml);
    }

    @Test
    public void testRestPost() throws Exception {
        AAIRestInterface testSubject;
        String fromAppId = "";
        String transId = "";
        String path = "";
        String payload = "";
        boolean xml = false;

        // default test
        testSubject = createTestSubject();
        testSubject.RestPost(fromAppId, transId, path, payload, xml);
    }
}