package org.onap.vid.scheduler;

import com.att.eelf.configuration.EELFLogger;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.http.HttpException;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

@Service
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {

    private static final  EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("scheduler");
    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
    private static final String SUCCESSFUL_API_MESSAGE=" REST api GET was successful!";
    private SyncRestClientInterface syncRestClient;
    private Function<String, String> propertyGetter;
    private Map<String, String> commonHeaders;

    public SchedulerRestInterface() {
        this.propertyGetter = SystemProperties::getProperty;
    }

    public SchedulerRestInterface(Function<String, String> propertyGetter) {
        this.propertyGetter = propertyGetter;
    }

    public void initRestClient() {
        logger.info("Starting to initialize rest client ");
        String authStringEnc = calcEncodedAuthString();

        commonHeaders = Maps.newHashMap();
        commonHeaders.put("Authorization", "Basic " + authStringEnc);

        syncRestClient = new SyncRestClient();

        logger.info("\t<== Client Initialized \n");
    }

    public <T> RestObjectWithRequestInfo<T> Get(T t, String path, RestObject<T> restObject) {

        String url = null;
        String rawData = null;
        Integer status = null;

        try {
            String methodName = "Get";
            url = String.format("%s%s", propertyGetter.apply(SchedulerProperties.SCHEDULER_SERVER_URL_VAL), path);
            initRestClient();
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
            Map<String, String> requestHeaders = ImmutableMap.<String, String>builder()
                    .putAll(commonHeaders)
                    .put(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId())
                    .build();
            final HttpResponse<T> response = ((HttpResponse<T>) syncRestClient.get(url, requestHeaders,
                    Collections.emptyMap(), t.getClass()));
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, response);
            status = response.getStatus();
            restObject.setStatusCode(status);

            if (status == 200) {
                t = response.getBody();
                restObject.set(t);
                logger.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + SUCCESSFUL_API_MESSAGE);
                logger.info(EELFLoggerDelegate.errorLogger, "<== " + methodName + SUCCESSFUL_API_MESSAGE);
            } else {
                throw new GenericUncheckedException(new HttpException(String.format("%s with status=%d, url=%s", methodName, status, url)));
            }
            return new RestObjectWithRequestInfo<>(HttpMethod.GET, url, restObject, status, rawData);
        }
        catch (RuntimeException e) {
            throw new ExceptionWithRequestInfo(HttpMethod.GET, url, rawData, status, e);
        }
    }

    public <T> void Delete(T t, String sourceID, String path, RestObject<T> restObject) {
        initRestClient();
        String url = String.format("%s%s", propertyGetter.apply(SchedulerProperties.SCHEDULER_SERVER_URL_VAL), path);
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
