package org.opencomp.vid.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.primitives.Ints;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsNot.not;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.util.Collections.singletonList;
import static org.apache.commons.text.StringEscapeUtils.unescapeJson;

public class BaseApiTest {
    @SuppressWarnings("WeakerAccess")
    protected URI uri;
    @SuppressWarnings("WeakerAccess")
    protected ObjectMapper objectMapper = new ObjectMapper();
    @SuppressWarnings("WeakerAccess")
    protected Client client;
    protected Random random;
    protected final RestTemplate restTemplate = new RestTemplate();



    @BeforeClass
    public void init() {
        String host = System.getProperty("VID_HOST", "127.0.0.1");
        Integer port = Integer.valueOf(System.getProperty("VID_PORT", "8080"));
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vid").build();
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        random = new Random(System.currentTimeMillis());
    }


    static class DB_CONFIG {
        static String url = String.format("jdbc:mariadb://%s:%d/vid_portal",
                System.getProperty("DB_HOST", System.getProperty("VID_HOST", "127.0.0.1" )),
                Integer.valueOf(System.getProperty("DB_PORT", "3306"))
        );
        static String username = "euser";
        static String password = "euser";

        static final int userId = 2222;
        static final int roleId = 2222222;
    }



    @BeforeClass
    protected void createNewTestUser() {

        deleteNewTestUser();

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            System.out.println("Database connected!");
            //create new user with specific role
            Statement stmt = connection.createStatement();
            stmt.addBatch("INSERT INTO `fn_user` (`USER_ID`, `ORG_USER_ID`, `LOGIN_ID`, `LOGIN_PWD`) VALUES (" + DB_CONFIG.userId + ", 'Porfirio Gerhardt', '"+ DB_CONFIG.userId +"', '"+ DB_CONFIG.userId +"')");
            stmt.addBatch("INSERT INTO `fn_role` (`ROLE_ID`, `ROLE_NAME`, `ACTIVE_YN`, `PRIORITY`) VALUES (" + DB_CONFIG.roleId + ", 'PACKET CORE___vFlowLogic', 'Y', 5)");
            stmt.addBatch("INSERT INTO `fn_user_role` (`USER_ID`, `ROLE_ID`, `PRIORITY`, `APP_ID`) VALUES (" + DB_CONFIG.userId + ", " + DB_CONFIG.roleId + ", NULL, 1)");


            int[] executeBatch = stmt.executeBatch();
            assertThat(Ints.asList(executeBatch), everyItem(greaterThan(0)));

        } catch (SQLException e) {  
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    @AfterClass
    protected void deleteNewTestUser() {
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            System.out.println("Database connected!");
            Statement stmt = connection.createStatement();
            stmt.addBatch("DELETE FROM `fn_user_role` WHERE `USER_ID` = " + DB_CONFIG.userId);
            stmt.addBatch("DELETE FROM `fn_user` WHERE `USER_ID` = " + DB_CONFIG.userId);
            stmt.addBatch("DELETE FROM `fn_role` WHERE `ROLE_ID` = " + DB_CONFIG.roleId);

            int[] executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    protected HttpHeaders getCookieAndJsonHttpHeaders() {
        final Properties configProp = new Properties();
        try {
            InputStream input = ClassLoader.getSystemResourceAsStream("objectconfig.properties");
            configProp.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders loginRequestHeaders = new HttpHeaders();
        loginRequestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String loginId = configProp.getProperty("test.loginId", "1");
        String loginPassword = configProp.getProperty("test.loginPassword", "1");
        ResponseEntity<String> loginRes = restTemplate.postForEntity(uri.toASCIIString() + "/login_external.htm", new HttpEntity<>("loginId=" + loginId + "&password=" + loginPassword, loginRequestHeaders), String.class);
        Assert.assertEquals("Login failed - wrong http status", HttpStatus.FOUND, loginRes.getStatusCode());
        Assert.assertNull("Failed to login with user:"+loginId+" password:"+loginPassword, loginRes.getBody());
        HttpHeaders loginResponseHeaders = loginRes.getHeaders();
        List<String> cookie = loginResponseHeaders.get(HttpHeaders.SET_COOKIE);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.put(HttpHeaders.COOKIE, cookie);
        return headers;
    }

    class CookieAndJsonHttpHeadersInterceptor implements ClientHttpRequestInterceptor {
        private final HttpHeaders cookieAndJsonHttpHeaders;

        public CookieAndJsonHttpHeadersInterceptor() {
            this.cookieAndJsonHttpHeaders = getCookieAndJsonHttpHeaders();
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {

            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            requestWrapper.getHeaders().putAll(cookieAndJsonHttpHeaders);

            return execution.execute(requestWrapper, body);
        }
    }


    protected String getCleanJsonString(String jsonString) {
        // remove leading/trailing double-quotes and unescape
        String res = unescapeJson(jsonString.replaceAll("^\"|\"$", ""));
        System.out.println("getCleanJsonString: " + jsonString  + " ==> " + res);
        return res;
    }

    protected String getCleanJsonString(Object object) throws JsonProcessingException {
        if (object instanceof String) {
            return getCleanJsonString((String) object);
        } else {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(object);
        }
    }

    protected String buildUri(String path) {
        return uri + "/" + path;
    }

}
