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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.PombaClientImpl;
import org.onap.vid.aai.PombaClientInterface;
import org.onap.vid.aai.PombaRestInterface;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.AAIProperties;
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
import org.onap.vid.properties.BaseUrlProvider;
import org.onap.vid.scheduler.SchedulerProperties;
import org.onap.vid.scheduler.SchedulerRestInterface;
import org.onap.vid.scheduler.SchedulerRestInterfaceIfc;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.PombaService;
import org.onap.vid.services.PombaServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.function.Supplier;

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
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper,
                                             @Qualifier("aaiServerBaseUrlProvider") BaseUrlProvider getAaiServerBaseUrlProvider,
                                             @Qualifier("aaiServerUrlProvider") BaseUrlProvider getAaiServerUrlProvider) {
        return new AAIRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper, getAaiServerUrlProvider, getAaiServerBaseUrlProvider);
    }

    @Bean
    public PombaRestInterface getPombaRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper,
                                                    @Qualifier("aaiServerBaseUrlProvider") BaseUrlProvider getAaiServerBaseUrlProvider,
                                                    @Qualifier("aaiServerUrlProvider") BaseUrlProvider getAaiServerUrlProvider) {
        return new PombaRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper,getAaiServerUrlProvider,getAaiServerBaseUrlProvider);
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
    public AsdcClient sdcClient(@Qualifier("sdcSecuredPropertiesProvider") Supplier<String> getSdcSecuredPropertiesProvider,
                                @Qualifier("sdcUnsecuredPropertiesProvider") Supplier<String> getSdcUnsecuredPropertiesProvider,
                                SyncRestClientInterface syncRestClient, FeatureManager featureManager, AsdcClientConfiguration asdcClientConfiguration) {

        BaseUrlProvider baseUrlProvider = new BaseUrlProvider(featureManager, getSdcSecuredPropertiesProvider, getSdcUnsecuredPropertiesProvider);
        return new SdcRestClient(baseUrlProvider, asdcClientConfiguration.asdcClientAuth, syncRestClient);
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




    @Bean(name = "sdcSecuredPropertiesProvider")
    public Supplier<String> getSdcSecuredPropertiesProvider(AsdcClientConfiguration asdcClientConfig) {
        return () -> asdcClientConfig.getAsdcClientSecuredProtocol() + "://" + asdcClientConfig.getAsdcClientHost() + ":" + asdcClientConfig.getAsdcClientSecuredPort() + "/";
    }

    @Bean(name = "sdcUnsecuredPropertiesProvider")
    public Supplier<String> getSdcUnsecuredPropertiesProvider(AsdcClientConfiguration asdcClientConfig) {
        return () -> asdcClientConfig.getAsdcClientProtocol() + "://" + asdcClientConfig.getAsdcClientHost() + ":" + asdcClientConfig.getAsdcClientPort() + "/";
    }

    @Bean(name = "securedSchedulerPropertiesProvider")
    public Supplier<String> getSecuredSchedulerPropertiesProvider() {
        return () -> SchedulerProperties.SCHEDULER_SERVER_URL_VAL;
    }

    @Bean(name = "unsecuredSchedulerPropertiesProvider")
    public Supplier<String> getUnsecuredSchedulerPropertiesProvider() {
        return () -> SchedulerProperties.SCHEDULER_SERVER_URL_UNSECURED_VAL;
    }


    @Bean(name = "securedAaiBasePropertiesProvider")
    public Supplier<String> getSecuredAaiBasePropertiesProvider() {
        return () -> AAIProperties.AAI_SERVER_URL_BASE;
    }

    @Bean(name = "unsecuredAaiBasePropertiesProvider")
    public Supplier<String> getUnsecuredAaiBasePropertiesProvider() {
        return () -> AAIProperties.AAI_SERVER_URL_BASE_UNSECURED;
    }

    @Bean(name = "securedAaiPropertiesProvider")
    public Supplier<String> getSecuredAaiPropertiesProvider() {
        return () -> AAIProperties.AAI_SERVER_URL;
    }

    @Bean(name = "unsecuredAaiPropertiesProvider")
    public Supplier<String> getUnsecuredAaiPropertiesProvider() {
        return () -> AAIProperties.AAI_SERVER_URL_UNSECURED;
    }

    @Bean(name = "aaiServerBaseUrlProvider")
    public BaseUrlProvider getAaiServerBaseUrlProvider(FeatureManager featureManager,
                                                       @Qualifier("securedAaiBasePropertiesProvider") Supplier<String> getSecuredAaiBaseServerPropertiesProvider,
                                                       @Qualifier("unsecuredAaiBasePropertiesProvider") Supplier<String> getUnsecuredAaiBasedServerPropertiesProvider) {
        return new BaseUrlProvider(featureManager, getSecuredAaiBaseServerPropertiesProvider, getUnsecuredAaiBasedServerPropertiesProvider);
    }

    @Bean(name = "aaiServerUrlProvider")
    public BaseUrlProvider getAaiServerUrlProvider(FeatureManager featureManager,
                                                   @Qualifier("securedAaiPropertiesProvider") Supplier<String> getSecuredAaiBaseServerPropertiesProvider,
                                                   @Qualifier("unsecuredAaiPropertiesProvider") Supplier<String> getUnsecuredAaiBasedServerPropertiesProvider) {
        return new BaseUrlProvider(featureManager, getSecuredAaiBaseServerPropertiesProvider, getUnsecuredAaiBasedServerPropertiesProvider);
    }

    @Bean(name = "schedulerUrlProvider")
    public BaseUrlProvider getSchedulerPropertiesProvider(
            @Qualifier("securedSchedulerPropertiesProvider") Supplier<String> getSecuredSchedulerPropertiesProvider,
            @Qualifier("unsecuredSchedulerPropertiesProvider") Supplier<String> getUnsecuredSchedulerPropertiesProvider,
            FeatureManager featureManager) {
        return  new BaseUrlProvider(featureManager, getSecuredSchedulerPropertiesProvider, getUnsecuredSchedulerPropertiesProvider);
    }

}
