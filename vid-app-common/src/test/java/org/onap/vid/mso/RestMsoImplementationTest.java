package org.onap.vid.mso;

import static org.junit.Assert.*;
import org.junit.Test;
import org.onap.vid.changeManagement.RequestDetailsWrapper;

import java.util.*;

import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.Assert;

public class RestMsoImplementationTest {

    private RestMsoImplementation createTestSubject() {
        return new RestMsoImplementation();
    }

    @Test
    public void testInitMsoClient() throws Exception {
        RestMsoImplementation testSubject;
        MultivaluedHashMap<String, Object> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.initMsoClient();
        } catch (Exception e) {
        }
    }


    @Test
    public void testGetForObject() throws Exception {
        RestMsoImplementation testSubject;
        String sourceID = "";
        String path = "";

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.GetForObject(sourceID, path, null);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDelete() throws Exception {
        RestMsoImplementation testSubject;
        String sourceID = "";
        String path = "";

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.Delete(null, null, sourceID, path, null);
        } catch (Exception e) {
        }
    }

    @Test
    public void testPostForObject() throws Exception {
        RestMsoImplementation testSubject;
        Object requestDetails = null;
        String sourceID = "";
        String path = "";

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.PostForObject(requestDetails, sourceID, path, null);
        } catch (

        Exception e) {
        }
    }



    @Test
    public void testPrepareClient() throws Exception {
        RestMsoImplementation testSubject;
        String path = "";
        String methodName = "";

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.prepareClient(path, methodName);
        } catch (

        Exception e) {
        }
    }




    // @Test
    // public void testInitMsoClient() throws Exception {
    // RestMsoImplementation testSubject;
    //
    // // default test
    // testSubject = createTestSubject();
    // testSubject.initMsoClient();
    // }

}