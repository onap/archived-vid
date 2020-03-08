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

import io.joshworks.restclient.http.mapper.ObjectMapper;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.factories.MsoRequestFactory;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.services.CloudOwnerService;
import org.onap.vid.services.CloudOwnerServiceImpl;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;


@Configuration
public class MsoConfig {

    @Bean
    public MsoRequestFactory createRequestDetailsFactory(){
        return new MsoRequestFactory();
    }

    @Bean
    public MsoRestClientNew msoRestClientNew(ObjectMapper unirestObjectMapper,
        SystemPropertiesWrapper systemPropertiesWrapper,
        Logging loggingService){
        // Satisfy both interfaces -- MsoInterface and RestMsoImplementation
        return new MsoRestClientNew(
            new SyncRestClient(unirestObjectMapper, loggingService),
            SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL),
            systemPropertiesWrapper
        );
    }

    @Bean
    public RestMsoImplementation restMsoImplementation(HttpsAuthClient httpsAuthClient,
        SystemPropertiesWrapper systemPropertiesWrapper,
        Logging loggingService){
        // Satisfy both interfaces -- MsoInterface and RestMsoImplementation
        return new RestMsoImplementation(
            httpsAuthClient,
            systemPropertiesWrapper,
            loggingService
        );
    }


    @Bean
    public MsoBusinessLogic getMsoBusinessLogic(MsoInterface msoClient, FeatureManager featureManager){
        return new MsoBusinessLogicImpl(msoClient, featureManager );
    }

    @Bean
    public CloudOwnerService cloudOwnerService(AaiClientInterface aaiClient, FeatureManager featureManager) {
        return new CloudOwnerServiceImpl(aaiClient, featureManager);
    }


}
