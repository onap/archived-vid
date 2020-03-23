package org.onap.vid.api;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static vid.automation.test.utils.RegExMatcher.matchesRegEx;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.util.concurrent.Uninterruptibles;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.commons.text.RandomStringGenerator;
import org.hamcrest.Matcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;

public class TestUtils {

    protected static ObjectMapper objectMapper = new ObjectMapper();

    public static void assertStatusOK(Object request, WebTarget webTarget, Response response) throws IOException {
        assertHttpStatus(request, webTarget, response, HttpStatus.OK);
    }

    public static void assertHttpStatus(Object request, WebTarget webTarget, Response response, HttpStatus exceptedHttpStatus) throws IOException {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        org.testng.Assert.assertEquals(response.getStatus(), exceptedHttpStatus.value(),
                String.format("Failed post URI: %s with request %s. Got Status:%d and body: %s",
                        webTarget.getUri(),
                        objectMapper.writeValueAsString(request),
                        response.getStatus(),
                        objectMapper.writeValueAsString(response.getEntity())));
    }

    public static String convertRequest(ObjectMapper objectMapper, String msoRequestDetailsFileName) {

        ClassLoader cl = TestUtils.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources;
        try {
            resources = resolver.getResources(msoRequestDetailsFileName);
            //using InputStream and not file. see https://stackoverflow.com/questions/14876836/file-inside-jar-is-not-visible-for-spring/51131841#51131841
            InputStream inputStream = resources[0].getInputStream();
            String content = new Scanner(inputStream).useDelimiter("\\Z").next();
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            return objectMapper.writeValueAsString(objectMapper.readValue(content, Object.class));
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getNestedPropertyInMap(Object item, String path) {
        return getNestedPropertyInMap(item, path, String.class, "/");
    }

    public static <T> T getNestedPropertyInMap(Object item, String path, Class<T> valueType) {
        return getNestedPropertyInMap(item, path, valueType, "/");
    }

    /*
    Use this method to extract item from Map that represent Json hierarchy (Map<String,Map>)
     */
    public static <T> T getNestedPropertyInMap(Object item, String path, Class<T> valueType, String delimeter) {
        String[] pathes  = path.split(delimeter);
        return valueType.cast(getNestedPropertyInMap(item,pathes,0));
    }

    private static Object getNestedPropertyInMap(Object item, String[] pathes, int index) {
        if (index==pathes.length) {
            return item;
        }
        return getNestedPropertyInMap(((Map<String,Object>)item).get(pathes[index]), pathes, ++index);
    }

    static Matcher hasOrLacksOfEntry(String pathRegex, Long expectedCounter) {
        return expectedCounter.equals(0L) ? not(hasKey(matchesRegEx(pathRegex))) : hasEntry(matchesRegEx(pathRegex), is(expectedCounter));
    }

    private static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(LETTERS, DIGITS)
            .build();

    public static String generateRandomAlphaNumeric(int length) {
        return generator.generate(length);
    }

    public static void assertAndRetryIfNeeded(long timeoutInSeconds, Runnable asserter) {
        final Instant expiry = Instant.now().plusSeconds(timeoutInSeconds);
        while (true) {
            try {
                asserter.run();
                break; // we're cool, assertion passed
            } catch (AssertionError fail) {
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                if (Instant.now().isAfter(expiry)) {
                    throw fail;
                } else {
                    System.out.println("retrying after: " + fail);
                }
            }
        }
    }

    @DataProvider
    public static Object[][] trueAndFalse() {
        return new Object[][]{{true}, {false}};
    }

}
