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


import org.hibernate.SessionFactory;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobAdapterImpl;
import org.onap.vid.job.impl.JobsBrokerServiceInDatabaseImpl;
import org.onap.vid.properties.VidProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JobAdapterConfig {

    @Bean
    public JobAdapter jobAdapter() {
        return new JobAdapterImpl();
    }

    @Bean
    public JobsBrokerService jobsBrokerService(DataAccessService dataAccessService, SessionFactory sessionFactory) {
        int maxOpenedInstantiationRequestsToMso = Integer.parseInt(SystemProperties.getProperty(VidProperties.MSO_MAX_OPENED_INSTANTIATION_REQUESTS));
        int pollingIntervalSeconds = Integer.parseInt(SystemProperties.getProperty(VidProperties.MSO_ASYNC_POLLING_INTERVAL_SECONDS));

        return new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, maxOpenedInstantiationRequestsToMso, pollingIntervalSeconds);
    }

}
