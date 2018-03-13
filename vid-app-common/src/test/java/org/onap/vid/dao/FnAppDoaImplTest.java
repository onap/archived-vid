package org.onap.vid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Assert;
import org.junit.Test;

public class FnAppDoaImplTest {

    private FnAppDoaImpl createTestSubject() {
        return new FnAppDoaImpl();
    }

    @Test
    public void testGetConnection() throws Exception {
        String driver2 = "";
        String url = "";
        String username = "";
        String password = "";
        Connection result;

        // test 1
        url = null;
        username = null;
        password = null;
        result = FnAppDoaImpl.getConnection(driver2, url, username, password);
        Assert.assertEquals(null, result);

        // test 2
        url = "";
        username = null;
        password = null;
        result = FnAppDoaImpl.getConnection(driver2, url, username, password);
        Assert.assertEquals(null, result);

        // test 3
        username = null;
        url = null;
        password = null;
        result = FnAppDoaImpl.getConnection(driver2, url, username, password);
        Assert.assertEquals(null, result);

        // test 4
        username = "";
        url = null;
        password = null;
        result = FnAppDoaImpl.getConnection(driver2, url, username, password);
        Assert.assertEquals(null, result);

        // test 5
        password = null;
        url = null;
        username = null;
        result = FnAppDoaImpl.getConnection(driver2, url, username, password);
        Assert.assertEquals(null, result);

        // test 6
        password = "";
        url = null;
        username = null;
        result = FnAppDoaImpl.getConnection(driver2, url, username, password);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testCleanup() throws Exception {
        ResultSet rs = null;
        PreparedStatement st = null;

        // test 1
        rs = null;
        FnAppDoaImpl.cleanup(rs, st, null);

        // test 2
        st = null;
        FnAppDoaImpl.cleanup(rs, st, null);

        // test 3
        FnAppDoaImpl.cleanup(rs, st, null);
    }

    @Test
    public void testGetProfileCount() throws Exception {
        FnAppDoaImpl testSubject;
        String driver = "";
        String URL = "";
        String username = "";
        String password = "";
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getProfileCount(driver, URL, username, password);
    }
}