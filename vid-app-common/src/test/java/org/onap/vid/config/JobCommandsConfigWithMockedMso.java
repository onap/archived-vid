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
import org.mockito.Mockito;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.ALaCarteServiceCommand;
import org.onap.vid.job.command.CommandUtils;
import org.onap.vid.job.command.InProgressStatusService;
import org.onap.vid.job.command.InstanceGroupCommand;
import org.onap.vid.job.command.InstanceGroupMemberCommand;
import org.onap.vid.job.command.JobCommandFactory;
import org.onap.vid.job.command.MacroServiceCommand;
import org.onap.vid.job.command.MsoRequestBuilder;
import org.onap.vid.job.command.MsoResultHandlerService;
import org.onap.vid.job.command.NetworkCommand;
import org.onap.vid.job.command.VfmoduleCommand;
import org.onap.vid.job.command.VnfCommand;
import org.onap.vid.job.command.VolumeGroupCommand;
import org.onap.vid.job.command.WatchChildrenJobsBL;
import org.onap.vid.job.impl.JobAdapterImpl;
import org.onap.vid.job.impl.JobWorker;
import org.onap.vid.job.impl.JobsBrokerServiceInDatabaseImpl;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AsyncInstantiationBusinessLogicImpl;
import org.onap.vid.services.AuditService;
import org.onap.vid.services.AuditServiceImpl;
import org.onap.vid.services.CloudOwnerService;
import org.onap.vid.services.InstantiationTemplatesService;
import org.onap.vid.services.VersionService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.togglz.core.manager.FeatureManager;

@Configuration
public class JobCommandsConfigWithMockedMso {

    @Bean
    public RestMsoImplementation restMso() {
        return Mockito.mock(RestMsoImplementation.class);
    }

    @Bean
    public VersionService versionService() {
        return Mockito.mock(VersionService.class);
    }

    @Bean
    public JobsBrokerService jobsBrokerService(DataAccessService dataAccessService, SessionFactory sessionFactory, VersionService versionService) {
        return new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, 200, 0,versionService, featureManager());
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
    public HttpsAuthClient httpsAuthClientFactory(SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider, FeatureManager featureManager){
        return new HttpsAuthClient("some random path", systemPropertyHelper, sslContextProvider, featureManager);
    }


    @Bean
    public JobAdapter jobAdapter(FeatureManager featureManager) {
        return new JobAdapterImpl(featureManager);
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
    public FeatureManager featureManager() {
        return Mockito.mock(FeatureManager.class);
    }

    @Bean
    public MsoRequestBuilder msoRequestHandlerService(AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
                                                      CloudOwnerService cloudOwnerService,
                                                      AaiClientInterface aaiClient,
                                                      FeatureManager featureManager) {
        return new MsoRequestBuilder(asyncInstantiationBusinessLogic, cloudOwnerService, aaiClient, featureManager);
    }
    @Bean
    public AsyncInstantiationRepository asyncInstantiationRepository(DataAccessService dataAccessService) {
        return new AsyncInstantiationRepository(dataAccessService);
    }

    @Bean
    public AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic(JobAdapter jobAdapter,
                                                                           JobsBrokerService jobsBrokerService,
                                                                           SessionFactory sessionFactory,
                                                                           AaiClientInterface aaiClient,
                                                                           FeatureManager featureManager,
                                                                           CloudOwnerService cloudOwnerService,
                                                                           AsyncInstantiationRepository asyncInstantiationRepository,
                                                                           AuditService auditService) {
        return new AsyncInstantiationBusinessLogicImpl(jobAdapter, jobsBrokerService, sessionFactory, aaiClient, featureManager, cloudOwnerService, asyncInstantiationRepository, auditService);
    }

    @Bean
    public ModelUtil modelUtil() {return new ModelUtil();}

    @Bean
    public InstantiationTemplatesService instantiationTemplatesService(
        ModelUtil modelUtil,
        AsyncInstantiationRepository asyncInstantiationRepository,
        FeatureManager featureManager
        ) {
        return new InstantiationTemplatesService(modelUtil, asyncInstantiationRepository, featureManager);
    };


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ALaCarteServiceCommand aLaCarteServiceCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            JobsBrokerService jobsBrokerService,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            JobAdapter jobAdapter,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            RestMsoImplementation restMso,
            AuditService auditService,
            FeatureManager featureManager) {
        return new ALaCarteServiceCommand(inProgressStatusService, watchChildrenJobsBL, asyncInstantiationBusinessLogic, jobsBrokerService, msoRequestBuilder, msoResultHandlerService, jobAdapter, restMso, auditService, featureManager);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MacroServiceCommand macroServiceCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            JobsBrokerService jobsBrokerService,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            JobAdapter jobAdapter,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            RestMsoImplementation restMso,
            AuditService auditService,
            FeatureManager featureManager) {
        return new MacroServiceCommand(inProgressStatusService, watchChildrenJobsBL, asyncInstantiationBusinessLogic, jobsBrokerService, msoRequestBuilder, msoResultHandlerService, jobAdapter, restMso, auditService, featureManager);
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NetworkCommand networkCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            RestMsoImplementation restMso,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            JobsBrokerService jobsBrokerService,
            JobAdapter jobAdapter,
            FeatureManager featureManager) {
        return new NetworkCommand(asyncInstantiationBusinessLogic, restMso, msoRequestBuilder, msoResultHandlerService,
                inProgressStatusService, watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InstanceGroupCommand instanceGroupCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            RestMsoImplementation restMso,
            JobsBrokerService jobsBrokerService,
            JobAdapter jobAdapter,
            FeatureManager featureManager) {
        return new InstanceGroupCommand(asyncInstantiationBusinessLogic, restMso, msoRequestBuilder, msoResultHandlerService, inProgressStatusService,
            watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InstanceGroupMemberCommand instanceGroupMemberCommand (
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            RestMsoImplementation restMso,
            JobsBrokerService jobsBrokerService,
            JobAdapter jobAdapter,
            FeatureManager featureManager) {
        return new InstanceGroupMemberCommand(asyncInstantiationBusinessLogic, restMso, msoRequestBuilder, msoResultHandlerService, inProgressStatusService,
                watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager);
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VnfCommand VnfCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            RestMsoImplementation restMso,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            JobsBrokerService jobsBrokerService,
            JobAdapter jobAdapter,
            FeatureManager featureManager) {
        return new VnfCommand(asyncInstantiationBusinessLogic, restMso, msoRequestBuilder, msoResultHandlerService,
                inProgressStatusService, watchChildrenJobsBL, jobsBrokerService ,jobAdapter,
                featureManager);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VolumeGroupCommand volumeGroupCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            RestMsoImplementation restMso,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            JobsBrokerService jobsBrokerService,
            JobAdapter jobAdapter,
            FeatureManager featureManager) {
        return new VolumeGroupCommand(asyncInstantiationBusinessLogic, restMso, msoRequestBuilder, msoResultHandlerService,
                inProgressStatusService, watchChildrenJobsBL, jobsBrokerService ,jobAdapter, featureManager);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VfmoduleCommand VfmoduleCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            RestMsoImplementation restMso,
            MsoRequestBuilder msoRequestBuilder,
            MsoResultHandlerService msoResultHandlerService,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            JobsBrokerService jobsBrokerService,
            JobAdapter jobAdapter,
            FeatureManager featureManager) {
        return new VfmoduleCommand(asyncInstantiationBusinessLogic, restMso, msoRequestBuilder, msoResultHandlerService,
                inProgressStatusService, watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager);
    }
    @Bean
    public AuditService auditService(RestMsoImplementation msoClient, AsyncInstantiationRepository asyncInstantiationRepository) {
        return new AuditServiceImpl(msoClient, asyncInstantiationRepository);
    }

    @Bean
    public InProgressStatusService inProgressStatusService(AsyncInstantiationBusinessLogic asyncInstantiationBL, RestMsoImplementation restMso, AuditService auditService, FeatureManager featureManager) {
        return new InProgressStatusService(asyncInstantiationBL, restMso, auditService, featureManager);
    }

    @Bean
    public MsoResultHandlerService rootCommandService(AsyncInstantiationBusinessLogic asyncInstantiationBL, AuditService auditService) {
        return new MsoResultHandlerService(asyncInstantiationBL, auditService);
    }

    @Bean
    public CommandUtils commandUtils() {
        return Mockito.mock(CommandUtils.class);
    }

    @Bean
    public WatchChildrenJobsBL watchChildrenJobsService(DataAccessService dataAccessService) {
        return new WatchChildrenJobsBL(dataAccessService, featureManager());
    }

}
