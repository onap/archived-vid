package org.onap.vid.aai;

import static org.junit.Assert.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.model.SubscriberList;

public class AaiClientTest {

    private AaiClient createTestSubject() {
        return new AaiClient();
    }


    @Test
    public void testDoAaiGet() throws Exception {
        AaiClient testSubject;
        String certiPath = "";
        String uri = "";
        boolean xml = false;

        // default test
        testSubject = createTestSubject();
        testSubject.doAaiGet(certiPath, uri, xml);
    }

    @Test
    public void testParseServiceSubscriptionObjectForTenants() throws Exception {
        JSONObject jsonObject = null;
        String result;

        // default test
        result = AaiClient.parseServiceSubscriptionObjectForTenants(jsonObject);
    }

}