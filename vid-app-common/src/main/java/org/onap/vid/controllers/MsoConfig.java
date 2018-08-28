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
import org.onap.portalsdk.core.util.SystemProperties;

import org.onap.vid.client.SyncRestClient;
import org.onap.vid.factories.MsoRequestFactory;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.properties.BaseUrlProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

import java.util.function.Supplier;


@Configuration
public class MsoConfig {

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
    public MsoRequestFactory createRequestDetailsFactory() {
        return new MsoRequestFactory();
    }

    @Bean
    public MsoInterface getMsoClient(
            @Qualifier("securedMsoPropertiesProvider") Supplier<String> getMsoSecuredPropertiesProvider,
            @Qualifier("unsecuredMsoPropertiesProvider") Supplier<String> getMsoUnsecuredPropertiesProvider, FeatureManager featureManager) {

        BaseUrlProvider baseUrlProvider = new BaseUrlProvider(featureManager, getMsoSecuredPropertiesProvider, getMsoUnsecuredPropertiesProvider);
        return new MsoRestClientNew(new SyncRestClient(MsoInterface.objectMapper()), baseUrlProvider);
    }

    @Bean
    public MsoBusinessLogic getMsoBusinessLogic(MsoInterface msoClient, FeatureManager featureManager) {
        return new MsoBusinessLogicImpl(msoClient, featureManager);
    }


    @Bean(name = "securedMsoPropertiesProvider")
    public Supplier<String> getSecuredMsoPropertiesProvider() {
        return () -> SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL);
    }

    @Bean(name = "unsecuredMsoPropertiesProvider")
    public Supplier<String> getUnsecuredMsoPropertiesProvider() {
        return () -> SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL_UNSECURED);
    }

}
