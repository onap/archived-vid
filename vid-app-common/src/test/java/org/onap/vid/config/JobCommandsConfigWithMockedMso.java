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

package org.onap.vid.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiOverTLSClient;
import org.onap.vid.aai.AaiOverTLSClientInterface;
import org.onap.vid.aai.AaiOverTLSPropertySupplier;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.InProgressStatusCommand;
import org.onap.vid.job.command.JobCommandFactory;
import org.onap.vid.job.command.ServiceInstantiationCommand;
import org.onap.vid.job.impl.JobAdapterImpl;
import org.onap.vid.job.impl.JobWorker;
import org.onap.vid.job.impl.JobsBrokerServiceInDatabaseImpl;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AsyncInstantiationBusinessLogicImpl;
import org.onap.vid.services.AuditService;
import org.onap.vid.services.AuditServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class JobCommandsConfigWithMockedMso {

    @Bean
    public RestMsoImplementation restMso() {
        return Mockito.mock(RestMsoImplementation.class);
    }

    @Bean
    public JobsBrokerService jobsBrokerService(DataAccessService dataAccessService, SessionFactory sessionFactory) {
        return new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, 200, 0);
    }

    @Bean
    public HttpsAuthClient httpsAuthClientFactory(){
        return new HttpsAuthClient("some random path", new SystemPropertyHelper(), new SSLContextProvider());
    }

    @Bean
    public JobAdapter jobAdapter() {
        return new JobAdapterImpl();
    }

    @Bean
    public JobCommandFactory jobCommandFactory(ApplicationContext applicationContext) {
        return new JobCommandFactory(applicationContext);
    }

    @Bean
    public JobWorker jobWorker(JobsBrokerService jobsBrokerService, JobCommandFactory jobCommandFactory) {
        JobWorker jobWorker = new JobWorker();
        jobWorker.setJobsBrokerService(jobsBrokerService);
        jobWorker.setJobCommandFactory(jobCommandFactory);
        return jobWorker;
    }

    @Bean
    public AaiOverTLSClientInterface AaiOverTLSClient(){
        io.joshworks.restclient.http.mapper.ObjectMapper objectMapper = new io.joshworks.restclient.http.mapper.ObjectMapper() {

            ObjectMapper om = new ObjectMapper();

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                try {
                    return om.readValue(s, aClass)
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

    @Bean
    public AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic(DataAccessService dataAccessService,
                                                                           JobAdapter jobAdapter,
                                                                           JobsBrokerService jobsBrokerService,
                                                                           SessionFactory sessionFactory,
                                                                           AaiClientInterface aaiClient,
                                                                           AaiOverTLSClientInterface aaiOverTLSClientInterface) {
        return new AsyncInstantiationBusinessLogicImpl(dataAccessService, jobAdapter, jobsBrokerService, sessionFactory, aaiClient, aaiOverTLSClientInterface);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ServiceInstantiationCommand serviceInstantiationCommand() {
        return new ServiceInstantiationCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InProgressStatusCommand inProgressStatusCommand() {
        return new InProgressStatusCommand();
    }

    @Bean
    public AuditService auditService() {
        return new AuditServiceImpl();
    }

}
