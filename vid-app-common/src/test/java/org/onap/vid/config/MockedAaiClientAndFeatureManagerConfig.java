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

package org.onap.vid.config;

import org.mockito.Mockito;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.services.CloudOwnerService;
import org.onap.vid.services.CloudOwnerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

@Configuration
public class MockedAaiClientAndFeatureManagerConfig {

    @Bean
    public FeatureManager featureManager() {
        return Mockito.mock(FeatureManager.class);
    }

    @Bean
    public CloudOwnerService cloudOwnerService(AaiClientInterface aaiClient, FeatureManager featureManager) {
        return new CloudOwnerServiceImpl(aaiClient, featureManager);
    }

    @Bean
    public AaiClientInterface aaiClient() {
        return Mockito.mock(AaiClientInterface.class);
    }
}
