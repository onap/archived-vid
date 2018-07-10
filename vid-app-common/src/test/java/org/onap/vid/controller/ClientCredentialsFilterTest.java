package org.onap.vid.controller;


import org.junit.Assert;
import org.mockito.Mockito;
import org.onap.vid.controller.filter.ClientCredentialsFilter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Matchers.any;


/**
 * Created by amichai on 16/05/2018.
 */
@Test
public class ClientCredentialsFilterTest {

    @DataProvider
    public static Object[][] authorizedData() {
        return new Object[][] {
                {"Basic 123==", null},
                {null, null},
                {null, ""},
                {"Basic 123==", ""},
                {"Basic 123==", "Basic 123=="}
        };
    }

    @DataProvider
    public static Object[][] notAuthorizedData() {
        return new Object[][] {
                {null, "Basic 123=="},
                {"", "Basic 123=="},
                {"not null but not as expected", "Basic 123=="},
                {"basic 123==", "Basic 123=="}
        };
    }

    @DataProvider
    public static Object[][] clientVerified() {
        return new Object[][] {
                {true},
                {false}
        };
    }

    @Test(dataProvider = "authorizedData")
    public void givenAuthorizationHeader_Authorized(String actualAuth, String expectedAuth){
        ClientCredentialsFilter filter = new ClientCredentialsFilter();
        Assert.assertTrue(filter.verifyClientCredentials(actualAuth, expectedAuth));
    }

    @Test(dataProvider = "notAuthorizedData")
    public void givenAuthorizationHeader_NotAuthorized(String actualAuth, String expectedAuth){
        ClientCredentialsFilter filter = new ClientCredentialsFilter();
        Assert.assertFalse(filter.verifyClientCredentials(actualAuth, expectedAuth));
    }

    //@Test(dataProvider = "clientVerified")
    public void notAuthorized_return401(Boolean clientVerified) throws IOException, ServletException {
        ClientCredentialsFilter filter = Mockito.mock(ClientCredentialsFilter.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        FilterChain chain = Mockito.mock(FilterChain.class);


        Mockito.when(filter.verifyClientCredentials(any(String.class),any(String.class))).thenReturn(clientVerified);
        Mockito.doNothing().when(response).sendError(401);

        Mockito.doCallRealMethod().when(filter).doFilter(request,response,chain);
        filter.doFilter(request,response,chain);

        if (clientVerified)
        {
            Mockito.verify(chain).doFilter(request,response);

        }
        else {
            Mockito.verify(response).sendError(401);
        }

    }


}
