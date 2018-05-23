package org.onap.vid.config;

import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.SSLContextProvider;
import org.onap.vid.aai.util.SystemPropertyHelper;
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
    public AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic(DataAccessService dataAccessService,
                                                                           JobAdapter jobAdapter,
                                                                           JobsBrokerService jobsBrokerService,
                                                                           SessionFactory sessionFactory,
                                                                           AaiClientInterface aaiClient) {
        return new AsyncInstantiationBusinessLogicImpl(dataAccessService, jobAdapter, jobsBrokerService, sessionFactory, aaiClient);
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
