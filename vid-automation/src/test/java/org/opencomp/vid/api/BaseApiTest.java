package org.opencomp.vid.api;

import com.att.automation.common.report_portal_integration.listeners.ReportPortalListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.primitives.Ints;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import vid.automation.test.infra.FeaturesTogglingConfiguration;
import vid.automation.test.services.UsersService;
import vid.automation.test.utils.CookieAndJsonHttpHeadersInterceptor;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.util.Collections.singletonList;
import static org.apache.commons.text.StringEscapeUtils.unescapeJson;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@Listeners(ReportPortalListener.class)
public class BaseApiTest {
    protected static final Logger LOGGER = LogManager.getLogger(BaseApiTest.class);

    @SuppressWarnings("WeakerAccess")
    protected URI uri;
    @SuppressWarnings("WeakerAccess")
    protected ObjectMapper objectMapper = new ObjectMapper();
    @SuppressWarnings("WeakerAccess")
    protected Client client;
    protected Random random;
    protected final RestTemplate restTemplate = new RestTemplate();

    protected final UsersService usersService = new UsersService();
    protected final RestTemplate restTemplateErrorAgnostic = new RestTemplate();

    @BeforeClass
    public void init() {
        uri = getUri();
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        random = new Random(System.currentTimeMillis());
        FeaturesTogglingConfiguration.initializeFeatureManager();
    }

    private URI getUri() {
        String host = System.getProperty("VID_HOST", "127.0.0.1");
        Integer port = Integer.valueOf(System.getProperty("VID_PORT", "8080"));
        return new JerseyUriBuilder().host(host).port(port).scheme("http").path("vid").build();
    }

    public void login() {
        UserCredentials userCredentials = getUserCredentials();
        final List<ClientHttpRequestInterceptor> interceptors = singletonList(new CookieAndJsonHttpHeadersInterceptor(getUri(), userCredentials));
        restTemplate.setInterceptors(interceptors);

        restTemplateErrorAgnostic.setInterceptors(interceptors);
        restTemplateErrorAgnostic.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
    }


    static class DB_CONFIG {
        static String url = String.format("jdbc:mariadb://%s:%d/vid_portal",
                System.getProperty("DB_HOST", System.getProperty("VID_HOST", "127.0.0.1")),
                Integer.valueOf(System.getProperty("DB_PORT", "3306"))
        );
        static String username = "euser";
        static String password = "euser";

        static final int userId = 2222;
        static final String loginId = "ab2222";
        static final int roleId = 2222221;
        static final int logRoleId = 2222222;
    }


    @BeforeClass
    protected void createNewTestUser() {

        deleteNewTestUser();

        LOGGER.debug("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            LOGGER.debug("Database connected!");
            //create new user with specific role
            Statement stmt = connection.createStatement();
            stmt.addBatch("INSERT INTO `fn_user` (`USER_ID`, `ORG_USER_ID`, `LOGIN_ID`, `LOGIN_PWD`) VALUES (" + DB_CONFIG.userId + ", 'Porfirio Gerhardt', '" + DB_CONFIG.loginId + "', '" + DB_CONFIG.loginId + "')");
            stmt.addBatch("INSERT INTO `fn_role` (`ROLE_ID`, `ROLE_NAME`, `ACTIVE_YN`, `PRIORITY`) VALUES (" + DB_CONFIG.roleId + ", 'PACKET CORE___vFlowLogic', 'Y', 5)");
            stmt.addBatch("INSERT INTO `fn_role` (`ROLE_ID`, `ROLE_NAME`, `ACTIVE_YN`, `PRIORITY`) VALUES (" + DB_CONFIG.logRoleId + ", 'READ___LOGS___PERMITTED', 'Y', 5)");
            stmt.addBatch("INSERT INTO `fn_user_role` (`USER_ID`, `ROLE_ID`, `PRIORITY`, `APP_ID`) VALUES (" + DB_CONFIG.userId + ", " + DB_CONFIG.roleId + ", NULL, 1)");
            stmt.addBatch("INSERT INTO `fn_user_role` (`USER_ID`, `ROLE_ID`, `PRIORITY`, `APP_ID`) VALUES (" + DB_CONFIG.userId + ", " + DB_CONFIG.logRoleId + ", NULL, 1)");


            int[] executeBatch = stmt.executeBatch();
            assertThat(Ints.asList(executeBatch), everyItem(greaterThan(0)));

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    @AfterClass
    protected void deleteNewTestUser() {
        LOGGER.debug("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            LOGGER.debug("Database connected!");
            Statement stmt = connection.createStatement();
            stmt.addBatch("DELETE FROM `fn_user_role` WHERE `USER_ID` = " + DB_CONFIG.userId);
            stmt.addBatch("DELETE FROM `fn_user` WHERE `USER_ID` = " + DB_CONFIG.userId);
            stmt.addBatch("DELETE FROM `fn_role` WHERE `ROLE_ID` = " + DB_CONFIG.roleId);
            stmt.addBatch("DELETE FROM `fn_role` WHERE `ROLE_ID` = " + DB_CONFIG.logRoleId);


            int[] executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    protected UserCredentials getUserCredentials() {
        final Properties configProp = new Properties();
        try {
            InputStream input = ClassLoader.getSystemResourceAsStream("test_config.properties");
            configProp.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders loginRequestHeaders = new HttpHeaders();
        loginRequestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String loginId = configProp.getProperty("test.loginId", "i'm illegal");
        String loginPassword = configProp.getProperty("test.loginPassword", "i'm illegal");
        return new UserCredentials(loginId, loginPassword, null, null, null);
    }




    protected String getCleanJsonString(String jsonString) {
        // remove leading/trailing double-quotes and unescape
        String res = unescapeJson(jsonString.replaceAll("^\"|\"$", ""));
        LOGGER.debug("getCleanJsonString: " + jsonString + " ==> " + res);
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

    public static String getResourceAsString(String resourcePath) {
        // load expected result
        final URL resource = BaseApiTest.class.getClassLoader().getResource(resourcePath);
        if (resource == null) throw new RuntimeException("resource file not found: " + resourcePath);
        try {
            return IOUtils.toString(resource, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
