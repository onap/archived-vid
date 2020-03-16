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

import com.fasterxml.jackson.module.kotlin.KotlinModule;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.domain.App;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
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
import org.onap.vid.aai.util.CacheProvider;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.ServiceInstanceStandardQuery;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.asdc.parser.VidNotionsBuilder;
import org.onap.vid.asdc.rest.SdcRestClient;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.logging.VidLoggingInterceptor;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.onap.vid.properties.Features;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.scheduler.SchedulerService;
import org.onap.vid.scheduler.SchedulerServiceImpl;
import org.onap.vid.services.AAIServiceTree;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.PombaService;
import org.onap.vid.services.PombaServiceImpl;
import org.onap.vid.utils.JoshworksJacksonObjectMapper;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.togglz.core.manager.FeatureManager;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired DataAccessService dataAccessService;
    @Autowired FeatureManager featureManager;
    String portalAppPassword = System.getenv(VidProperties.PORTAL_APP_PASSWORD_ENVIRONMENT_VARIABLE_NAME);

    @PostConstruct
    public void persistPortalAppPassword() {
        if (featureManager.isActive(Features.FLAG_GUILIN_CONFIG_PORTAL_APP_PASSWORD)) {
            if (StringUtils.isEmpty(portalAppPassword)) {
                return;
            }

            final App defaultApp = (App) dataAccessService.getDomainObject(App.class, 1L, null);

            if (defaultApp == null || StringUtils.equals(defaultApp.getAppPassword(), portalAppPassword)) {
                return;
            }

            defaultApp.setAppPassword(portalAppPassword);
            dataAccessService.saveDomainObject(defaultApp, null);
        }
    }


    @Bean
    public com.fasterxml.jackson.databind.ObjectMapper getObjectMapper() {
        return new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new KotlinModule());
    }


    @Bean
    public SchedulerService schedulerService(ChangeManagementService changeManagementService) {
        return new SchedulerServiceImpl(changeManagementService);
    }

    @Bean
    public AaiService getAaiService(AaiClientInterface aaiClient, AaiResponseTranslator aaiResponseTranslator,
        AAIServiceTree aaiServiceTree, Logging logging, ExecutorService executorService) {
        return new AaiServiceImpl(aaiClient, aaiResponseTranslator, aaiServiceTree, executorService, logging);
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
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory,
        ServletRequestHelper servletRequestHelper,
        SystemPropertyHelper systemPropertyHelper,
        Logging loggingService) {
        return new AAIRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper, loggingService);
    }

    @Bean
    public PombaRestInterface getPombaRestInterface(HttpsAuthClient httpsAuthClientFactory,
        ServletRequestHelper servletRequestHelper,
        SystemPropertyHelper systemPropertyHelper,
        Logging loggingService) {
        return new PombaRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper, loggingService);
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
    public AsdcClient sdcClient(AsdcClientConfiguration asdcClientConfiguration, Logging loggingService) {
        String auth = asdcClientConfiguration.getAsdcClientAuth();
        String host = asdcClientConfiguration.getAsdcClientHost();
        String protocol = asdcClientConfiguration.getAsdcClientProtocol();
        int port = asdcClientConfiguration.getAsdcClientPort();

        return new SdcRestClient(protocol + "://" + host + ":" + port + "/", auth,
            new SyncRestClient( loggingService, true),
            loggingService);
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
    public AaiOverTLSClientInterface aaiOverTLSClient(ObjectMapper unirestObjectMapper, SystemProperties systemProperties, Logging loggingService){
        return new AaiOverTLSClient(
            new SyncRestClient(unirestObjectMapper,  loggingService),
            new AaiOverTLSPropertySupplier());
    }

    @Bean
    public ObjectMapper unirestFasterxmlObjectMapper() {
        return new JoshworksJacksonObjectMapper();
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new VidLoggingInterceptor(new ControllersUtils(new SystemPropertiesWrapper()))
        ).order(Ordered.HIGHEST_PRECEDENCE);
    }
}
