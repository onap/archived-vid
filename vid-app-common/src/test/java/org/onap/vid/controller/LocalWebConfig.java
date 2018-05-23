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

package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.*;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

import javax.servlet.ServletContext;
import java.io.File;

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
    public VidService vidService(AsdcClient asdcClient, FeatureManager featureManager) {
        return new VidServiceImpl(asdcClient, featureManager);
    }

    @Bean
    public AaiService getAaiService() {
        return new AaiServiceImpl();
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

    @Bean(name = "aaiRestInterface")
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper) {
        return new AAIRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper);
    }

    @Bean
    public AaiClientInterface getAaiClientInterface(@Qualifier("aaiRestInterface")AAIRestInterface aaiRestInterface, PortDetailsTranslator portDetailsTranslator) {
        return new AaiClient(aaiRestInterface, portDetailsTranslator);
    }

    @Bean
    public ToscaParserImpl2 getToscaParser() {
        return new ToscaParserImpl2();
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
