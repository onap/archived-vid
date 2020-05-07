package org.onap.vid.api;

import static java.util.Collections.singletonList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.apache.commons.text.StringEscapeUtils.unescapeJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import javax.ws.rs.client.Client;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import vid.automation.reportportal.ReportPortalListenerDelegator;
import vid.automation.test.infra.FeaturesTogglingConfiguration;
import vid.automation.test.services.UsersService;
import vid.automation.test.utils.CookieAndJsonHttpHeadersInterceptor;
import vid.automation.test.utils.InsecureHttpsClient;

@Listeners(ReportPortalListenerDelegator.class)
public class BaseApiTest {
    protected static final Logger LOGGER = LogManager.getLogger(BaseApiTest.class);

    @SuppressWarnings("WeakerAccess")
    protected URI uri;
    @SuppressWarnings("WeakerAccess")
    protected ObjectMapper objectMapper = new ObjectMapper();
    @SuppressWarnings("WeakerAccess")
    protected Client client;
    protected Random random;
    protected final RestTemplate restTemplate = InsecureHttpsClient.newRestTemplate();

    protected static final UsersService usersService = new UsersService();
    protected final RestTemplate restTemplateErrorAgnostic = InsecureHttpsClient.newRestTemplate();

    @BeforeClass
    public void init() {
        uri = getUri();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        client = InsecureHttpsClient.newJaxrsClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        random = new Random(System.currentTimeMillis());
        FeaturesTogglingConfiguration.initializeFeatureManager();
    }

    private URI getUri() {
        String host = System.getProperty("VID_HOST", "127.0.0.1");
        int port = Integer.valueOf(System.getProperty("VID_PORT", "8080"));
        String protocol = System.getProperty("VID_PROTOCOL", "http");
        return new JerseyUriBuilder().host(host).port(port).scheme(protocol).path("vid").build();
    }

    public void login() {
        login(getUserCredentials());
    }

    public void login(UserCredentials userCredentials) {
        final List<ClientHttpRequestInterceptor> interceptors = loginWithChosenRESTClient(userCredentials, restTemplate);
        restTemplateErrorAgnostic.setInterceptors(interceptors);
        restTemplateErrorAgnostic.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
    }

    public List<ClientHttpRequestInterceptor> loginWithChosenRESTClient(UserCredentials userCredentials,RestTemplate givenRestTemplate) {
        final List<ClientHttpRequestInterceptor> interceptors = singletonList(new CookieAndJsonHttpHeadersInterceptor(getUri(), userCredentials));
        givenRestTemplate.setInterceptors(interceptors);
        givenRestTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                try {
                    super.handleError(response);
                } catch (HttpStatusCodeException e) {
                    LOGGER.error("HTTP {}: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
                    throw e;
                }
            }
        });
        return interceptors;
    }


    //set time zone to UTC so clock will go closely with VID app
    @BeforeClass
    public void setDefaultTimeZoneToUTC() {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); //since TimeZone cache previous user.timezone
    }

    public UserCredentials getUserCredentials() {
        final Properties configProp = new Properties();
        try {
            InputStream input = ClassLoader.getSystemResourceAsStream("test_config.properties");
            configProp.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    protected void assertJsonEquals(String actual, String expected) {
        LOGGER.info(actual);
        assertThat(actual, not(isEmptyOrNullString()));

        assertThat(actual, jsonEquals(expected)
                .when(IGNORING_ARRAY_ORDER)
        );
    }

}
