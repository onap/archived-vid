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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.concurrent.ExecutorService;
import javax.servlet.ServletContext;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.CacheProvider;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.NonCachingCacheProvider;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.asdc.parser.VidNotionsBuilder;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.services.AAIServiceTree;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.onap.vid.utils.Logging;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

@Configuration
public class LocalWebConfig {

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
    public VidService vidService(AsdcClient asdcClient, ToscaParserImpl2 toscaParserImpl2,FeatureManager featureManager) {
        return new VidServiceImpl(asdcClient,toscaParserImpl2, featureManager);
    }

    @Bean
    public AaiService getAaiService(AaiClientInterface aaiClient, AaiResponseTranslator aaiResponseTranslator,
        AAIServiceTree aaiServiceTree, Logging logging, ExecutorService executorService, AsyncInstantiationRepository asyncInstantiationRepository) {
        return new AaiServiceImpl(aaiClient, aaiResponseTranslator, aaiServiceTree, executorService, logging, asyncInstantiationRepository);
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
    public HttpsAuthClient httpsAuthClientFactory(ServletContext servletContext, SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider, FeatureManager featureManager) {
        final String certFilePath = new File(servletContext.getRealPath("/WEB-INF/cert/")).getAbsolutePath();
        return new HttpsAuthClient(certFilePath, systemPropertyHelper, sslContextProvider, featureManager);
    }

    @Bean
    public CacheProvider cacheProvider() {
        return new NonCachingCacheProvider();
    }

    @Bean(name = "aaiRestInterface")
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory,
        ServletRequestHelper servletRequestHelper,
        SystemPropertyHelper systemPropertyHelper,
        Logging loggingService) {
        return new AAIRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper, loggingService);
    }

    @Bean
    public AaiClientInterface getAaiClientInterface(@Qualifier("aaiRestInterface") AAIRestInterface aaiRestInterface, PortDetailsTranslator portDetailsTranslator, CacheProvider cacheProvider) {
        return new AaiClient(aaiRestInterface, portDetailsTranslator, cacheProvider);
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
    public AaiResponseTranslator aaiResponseTranslator() {
        return new AaiResponseTranslator();
    }

    @Bean
    public PortDetailsTranslator portDetailsTranslator(){
        return new PortDetailsTranslator();
    }

}
