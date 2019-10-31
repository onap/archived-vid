/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.scheduler;

import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

import com.att.eelf.configuration.EELFLogger;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.joshworks.restclient.http.HttpResponse;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {

    private static final  EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("scheduler");
    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
    private static final String SUCCESSFUL_API_MESSAGE=" REST api GET was successful!";
    private SyncRestClientInterface syncRestClient;
    private Function<String, String> propertyGetter;
    private Map<String, String> commonHeaders;

    private Logging loggingService;

    public SchedulerRestInterface(Function<String, String> propertyGetter, Logging loggingService) {
        this.loggingService = loggingService;
        this.propertyGetter = propertyGetter;
    }

    @Autowired
    public SchedulerRestInterface(Logging loggingService) {
        this.loggingService = loggingService;
        this.propertyGetter = SystemProperties::getProperty;
    }

    public void initRestClient() {
        logger.info("Starting to initialize rest client ");
        String authStringEnc = calcEncodedAuthString();

        commonHeaders = Maps.newHashMap();
        commonHeaders.put("Authorization", "Basic " + authStringEnc);

        syncRestClient = new SyncRestClient(loggingService, true);

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
            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
            Map<String, String> requestHeaders = ImmutableMap.<String, String>builder()
                    .putAll(commonHeaders)
                    .build();
            final HttpResponse<String> response = syncRestClient.get(url, requestHeaders,
                    Collections.emptyMap(), String.class);
            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.GET, url, response);
            status = response.getStatus();
            restObject.setStatusCode(status);
            rawData = response.getBody();
            restObject.setRaw(rawData);
            if (status == 200) {
                if (t instanceof String) {
                    restObject.set((T)rawData);
                }
                else {
                    restObject.set(JACKSON_OBJECT_MAPPER.readValue(rawData, (Class<T>)t.getClass()));
                }
                logger.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + SUCCESSFUL_API_MESSAGE);
                logger.info(EELFLoggerDelegate.errorLogger, "<== " + methodName + SUCCESSFUL_API_MESSAGE);
            } else {
                throw new GenericUncheckedException(new HttpException(String.format("%s with status=%d, url=%s", methodName, status, url)));
            }
            return new RestObjectWithRequestInfo<>(HttpMethod.GET, url, restObject, status, rawData);
        }
        catch (Exception e) {
            throw new ExceptionWithRequestInfo(HttpMethod.GET, url, rawData, status, e);
        }
    }

    public <T> void Delete(T t, String sourceID, String path, RestObject<T> restObject) {
        initRestClient();
        String url = String.format("%s%s", propertyGetter.apply(SchedulerProperties.SCHEDULER_SERVER_URL_VAL), path);
        loggingService.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url);
        Map<String, String> requestHeaders = ImmutableMap.<String, String>builder()
                .putAll(commonHeaders)
                .build();
        final HttpResponse<T> response = (HttpResponse<T>) syncRestClient.delete(url, requestHeaders, t.getClass());

        loggingService.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url, response);

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
