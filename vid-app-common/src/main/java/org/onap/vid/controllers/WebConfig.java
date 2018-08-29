/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiOverTLSClient;
import org.onap.vid.aai.AaiOverTLSClientInterface;
import org.onap.vid.aai.AaiOverTLSPropertySupplier;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.PombaClientImpl;
import org.onap.vid.aai.PombaClientInterface;
import org.onap.vid.aai.PombaRestInterface;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.asdc.rest.SdcRestClient;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.PombaService;
import org.onap.vid.services.PombaServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.onap.vid.scheduler.SchedulerRestInterface;
import org.onap.vid.scheduler.SchedulerRestInterfaceIfc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

import javax.servlet.ServletContext;
import java.io.File;

@Configuration
public class WebConfig {

    /**
     * Gets the object mapper.
     *
     * @return the object mapper
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public VidService vidService(AsdcClient asdcClient, FeatureManager featureManager) {
        return new VidServiceImpl(asdcClient, featureManager);
    }

    @Bean
    public AaiService getAaiService() {
        return new AaiServiceImpl();
    }

    @Bean
    public AaiResponseTranslator aaiResponseTranslator() {
        return new AaiResponseTranslator();
    }

    @Bean
    public PortDetailsTranslator portDetailsTranslator() {
        return new PortDetailsTranslator();
    }

    @Bean
    public AaiClientInterface getAaiRestInterface(@Qualifier("aaiRestInterface") AAIRestInterface restController, PortDetailsTranslator portsDetailsTranslator) {
        return new AaiClient(restController, portsDetailsTranslator);
    }

    @Bean(name = "aaiRestInterface")
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper) {
        return new AAIRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper);
    }

    @Bean
    public PombaRestInterface getPombaRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper) {
        return new PombaRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper);
    }

    @Bean
    public SSLContextProvider sslContextProvider() {
        return new SSLContextProvider();
    }

    @Bean
    public SystemPropertyHelper systemPropertyHelper() {
        return new SystemPropertyHelper();
    }

    @Bean
    public ServletRequestHelper servletRequestHelper() {
        return new ServletRequestHelper();
    }

    @Bean
    public HttpsAuthClient httpsAuthClientFactory(ServletContext servletContext, SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider) {
        final String certFilePath = new File(servletContext.getRealPath("/WEB-INF/cert/")).getAbsolutePath();
        return new HttpsAuthClient(certFilePath, systemPropertyHelper, sslContextProvider);
    }

    @Bean
    public AsdcClient sdcClient(AsdcClientConfiguration asdcClientConfiguration, SyncRestClientInterface syncRestClient) {
        String auth = asdcClientConfiguration.getAsdcClientAuth();
        String host = asdcClientConfiguration.getAsdcClientHost();
        String protocol = asdcClientConfiguration.getAsdcClientProtocol();
        int port = asdcClientConfiguration.getAsdcClientPort();

        return new SdcRestClient(protocol + "://" + host + ":" + port + "/", auth, syncRestClient);
    }

    @Bean
    public SyncRestClientInterface syncRestClient() {
        return new SyncRestClient();
    }

    @Bean
    public ToscaParserImpl2 getToscaParser() {
        return new ToscaParserImpl2();
    }

    @Bean
    public PombaService getVerifyServiceInstanceService() {
        return new PombaServiceImpl();
    }

    @Bean
    public PombaClientInterface getVerifyServiceInstanceClientInterface() {
        return new PombaClientImpl();
    }

    @Bean
    public SchedulerRestInterfaceIfc getSchedulerRestInterface(){
        return new SchedulerRestInterface();
    }


    @Bean
    public AaiOverTLSClientInterface getAaiOverTLSClientInterface() {

        io.joshworks.restclient.http.mapper.ObjectMapper objectMapper = new io.joshworks.restclient.http.mapper.ObjectMapper() {

            ObjectMapper om = new ObjectMapper();

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                try {
                    return om.readValue(s, aClass);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object o) {
                try {
                    return om.writeValueAsString(o);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        return new AaiOverTLSClient(new SyncRestClient(objectMapper), new AaiOverTLSPropertySupplier());
    }
}
