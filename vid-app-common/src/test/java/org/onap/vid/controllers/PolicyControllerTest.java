package org.onap.vid.controllers;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.onap.vid.policy.PolicyResponseWrapper;
import org.springframework.http.ResponseEntity;

public class PolicyControllerTest {

    private PolicyController createTestSubject() {
        return new PolicyController();
    }

    @Test
    public void testGetPolicyInfo() throws Exception {
        PolicyController testSubject;
        HttpServletRequest request = null;
        JSONObject policy_request = null;
        ResponseEntity<String> result;

        // default test
        try {
        testSubject = createTestSubject();
        result = testSubject.getPolicyInfo(request, policy_request);
        }catch(Exception e){}
    }

}