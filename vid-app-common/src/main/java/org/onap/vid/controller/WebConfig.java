/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

package org.onap.vid.controller;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import java.util.concurrent.Executors;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.*;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.*;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.asdc.parser.VidNotionsBuilder;
import org.onap.vid.asdc.rest.SdcRestClient;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.scheduler.SchedulerService;
import org.onap.vid.scheduler.SchedulerServiceImpl;
import org.onap.vid.services.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

@EnableSwagger2
@Configuration
public class WebConfig {

    /**
     * Gets the object mapper.
     *
     * @return the object mapper
     */
    @Bean
    public com.fasterxml.jackson.databind.ObjectMapper getObjectMapper() {
        return new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new KotlinModule());
    }


    @Bean
    public SchedulerService schedulerService(ChangeManagementService changeManagementService) {
        return new SchedulerServiceImpl(changeManagementService);
    }

    @Bean
    public AaiService getAaiService(AaiClientInterface aaiClient, AaiOverTLSClientInterface aaiOverTLSClient,
        AaiResponseTranslator aaiResponseTranslator, AAITreeNodeBuilder aaiTreeNode, AAIServiceTree aaiServiceTree, ExecutorService executorService) {
        return new AaiServiceImpl(aaiClient, aaiOverTLSClient, aaiResponseTranslator, aaiTreeNode, aaiServiceTree, executorService);
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
    public AaiClientInterface getAaiRestInterface(@Qualifier("aaiRestInterface") AAIRestInterface restController, PortDetailsTranslator portsDetailsTranslator, CacheProvider cacheProvider) {
        return new AaiClient(restController, portsDetailsTranslator, cacheProvider);
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
    public HttpsAuthClient httpsAuthClientFactory(ServletContext servletContext, SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider ,FeatureManager featureManager) {
        final String certFilePath = new File(servletContext.getRealPath("/WEB-INF/cert/")).getAbsolutePath();
        return new HttpsAuthClient(certFilePath, systemPropertyHelper, sslContextProvider, featureManager);
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
    public VidNotionsBuilder vidNotionsBuilder(FeatureManager featureManager) {
        return new VidNotionsBuilder(featureManager);
    }

    @Bean
    public ToscaParserImpl2 getToscaParser(VidNotionsBuilder vidNotionsBuilder) {
        return new ToscaParserImpl2(vidNotionsBuilder);
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
    public ServiceInstanceStandardQuery serviceInstanceStandardQuery(AaiClientInterface aaiClient) {
        return new ServiceInstanceStandardQuery(aaiClient);
    }

    @Bean
    public AaiOverTLSClientInterface aaiOverTLSClient(ObjectMapper unirestObjectMapper, SystemProperties systemProperties){
        return new AaiOverTLSClient(new SyncRestClient(unirestObjectMapper), new AaiOverTLSPropertySupplier());
    }

    @Bean
    public ObjectMapper unirestFasterxmlObjectMapper(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        return new ObjectMapper() {

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                try {
                    return objectMapper.readValue(s, aClass);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object o) {
                try {
                    return objectMapper.writeValueAsString(o);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };

    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.onap.vid.controller.open"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public ExecutorService executorService() {
        int threadsCount = defaultIfNull(Integer.parseInt(SystemProperties.getProperty(VidProperties.VID_THREAD_COUNT)), 1);
        return Executors.newFixedThreadPool(threadsCount);
    }
}
