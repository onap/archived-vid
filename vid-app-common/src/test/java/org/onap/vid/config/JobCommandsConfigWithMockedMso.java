package org.onap.vid.config;

import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.*;
import org.onap.vid.job.impl.JobAdapterImpl;
import org.onap.vid.job.impl.JobWorker;
import org.onap.vid.job.impl.JobsBrokerServiceInDatabaseImpl;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.services.*;
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
    public JobsBrokerService jobsBrokerService(DataAccessService dataAccessService, SessionFactory sessionFactory) {
        return new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, 200, 0);
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
    public FeatureManager featureManager() {
        return Mockito.mock(FeatureManager.class);
    }

    @Bean
    public AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic(DataAccessService dataAccessService,
                                                                           JobAdapter jobAdapter,
                                                                           JobsBrokerService jobsBrokerService,
                                                                           SessionFactory sessionFactory,
                                                                           AaiClientInterface aaiClient,
                                                                           FeatureManager featureManager,
                                                                           CloudOwnerService cloudOwnerService) {
        return new AsyncInstantiationBusinessLogicImpl(dataAccessService, jobAdapter, jobsBrokerService, sessionFactory, aaiClient, featureManager, cloudOwnerService);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MacroServiceInstantiationCommand serviceInstantiationCommand() {
        return new MacroServiceInstantiationCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ServiceInProgressStatusCommand inProgressStatusCommand() {
        return new ServiceInProgressStatusCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ALaCarteServiceInstantiationCommand aLaCarteServiceInstantiationCommand() {
        return new ALaCarteServiceInstantiationCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ALaCarteServiceCommand aLaCarteServiceCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            JobsBrokerService jobsBrokerService,
            MsoResultHandlerService msoResultHandlerService,
            JobAdapter jobAdapter,
            InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            RestMsoImplementation restMso) {
        return new ALaCarteServiceCommand(inProgressStatusService, watchChildrenJobsBL, asyncInstantiationBusinessLogic, jobsBrokerService, msoResultHandlerService, jobAdapter, restMso);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InstanceGroupCommand instanceGroupCommand(
            AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic,
            MsoResultHandlerService msoResultHandlerService, InProgressStatusService inProgressStatusService,
            WatchChildrenJobsBL watchChildrenJobsBL,
            RestMsoImplementation restMso) {
        return new InstanceGroupCommand(asyncInstantiationBusinessLogic, restMso, msoResultHandlerService, inProgressStatusService, watchChildrenJobsBL);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VnfInstantiationCommand vnfInstantiationCommand() {
        return new VnfInstantiationCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VolumeGroupInstantiationCommand volumeGroupInstantiationCommand() {
        return new VolumeGroupInstantiationCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WatchingCommandBaseModule watchingCommandBaseModule() {
        return new WatchingCommandBaseModule();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VolumeGroupInProgressStatusCommand volumeGroupInProgressStatusCommand() {
        return new VolumeGroupInProgressStatusCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VfmoduleInstantiationCommand vfmoduleInstantiationCommand() {
        return new VfmoduleInstantiationCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WatchingCommand watchingCommandCommand() {
        return new WatchingCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ResourceInProgressStatusCommand resourceInProgressStatusCommand() {
        return new ResourceInProgressStatusCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VnfInProgressStatusCommand vnfInProgressStatusCommand() {
        return new VnfInProgressStatusCommand();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InstanceGroupInstantiationCommand instanceGroupInstantiationCommand() {
        return new InstanceGroupInstantiationCommand();
    }

    @Bean
    public AuditService auditService(AsyncInstantiationBusinessLogic asyncInstantiationBL, RestMsoImplementation msoClient) {
        return new AuditServiceImpl(asyncInstantiationBL, msoClient);
    }

    @Bean
    public InProgressStatusService inProgressStatusService(AsyncInstantiationBusinessLogic asyncInstantiationBL, RestMsoImplementation restMso, AuditService auditService) {
        return new InProgressStatusService(asyncInstantiationBL, restMso, auditService);
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
        return new WatchChildrenJobsBL(dataAccessService);
    }

}
