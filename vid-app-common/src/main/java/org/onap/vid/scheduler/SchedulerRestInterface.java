package org.onap.vid.scheduler;

import com.att.eelf.configuration.EELFLogger;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.joshworks.restclient.http.HttpResponse;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.properties.BaseUrlProvider;
import org.onap.vid.utils.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

@Service
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {

    final private static EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("scheduler");
    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
    private SyncRestClientInterface syncRestClient;
    private Function<String, String> propertyGetter;
    private Map<String, String> commonHeaders;
    private BaseUrlProvider baseUrlProvider;

    @Autowired
    public SchedulerRestInterface(@Qualifier("schedulerUrlProvider") BaseUrlProvider baseUrlProvider) {
        this.propertyGetter = SystemProperties::getProperty;
        this.baseUrlProvider = baseUrlProvider;
    }

    public SchedulerRestInterface(Function<String, String> propertyGetter, BaseUrlProvider baseUrlProvider) {
        this.propertyGetter = propertyGetter;
        this.baseUrlProvider = baseUrlProvider;
    }

    public void initRestClient() {
        logger.info("Starting to initialize rest client ");
        String authStringEnc = calcEncodedAuthString();

        commonHeaders = Maps.newHashMap();
        commonHeaders.put("Authorization", "Basic " + authStringEnc);

        syncRestClient = new SyncRestClient();

        logger.info("\t<== Client Initialized \n");
    }

    public <T> void Get(T t, String sourceId, String path, org.onap.vid.scheduler.RestObject<T> restObject) {
        initRestClient();
        String methodName = "Get";
        String url = baseUrlProvider.getBaseUrl()+ path;
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        Map<String, String> requestHeaders = ImmutableMap.<String, String>builder()
                .putAll(commonHeaders)
                .put(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId()).build();
        final HttpResponse<T> response = ((HttpResponse<T>) syncRestClient.get(url, requestHeaders,
                Collections.emptyMap(), t.getClass()));
        Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, response);
        int status = response.getStatus();
        restObject.setStatusCode(status);

        if (status == 200) {
            t = response.getBody();
            restObject.set(t);

        } else {
            throw new GenericUncheckedException(String.format("%s with status=%d, url=%s", methodName, status, url));
        }
    }

    public <T> void Delete(T t, String sourceID, String path, org.onap.vid.scheduler.RestObject<T> restObject) {
        initRestClient();
        String url = baseUrlProvider.getBaseUrl()+ path;
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url);
        Map<String, String> requestHeaders = ImmutableMap.<String, String>builder()
                .putAll(commonHeaders)
                .put(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId()).build();
        final HttpResponse<T> response = (HttpResponse<T>) syncRestClient.delete(url, requestHeaders, t.getClass());

        Logging.logResponse(outgoingRequestsLogger, HttpMethod.DELETE, url, response);

        int status = response.getStatus();
        restObject.setStatusCode(status);

        t = response.getBody();
        restObject.set(t);
    }

    public <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    private String calcEncodedAuthString() {
        String retrievedUsername = propertyGetter.apply(SchedulerProperties.SCHEDULER_USER_NAME_VAL);
        final String username = retrievedUsername.isEmpty() ? "" : retrievedUsername;

        String retrievedPassword = propertyGetter.apply(SchedulerProperties.SCHEDULER_PASSWORD_VAL);
        final String password = retrievedPassword.isEmpty() ? "" : getDeobfuscatedPassword(retrievedPassword);

        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    private static String getDeobfuscatedPassword(String password) {
        return password.contains("OBF:") ? Password.deobfuscate(password) : password;
    }
}
