package org.onap.vid.job.command;

import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.Function;

@Component
public class JobCommandFactory {

    final Function<Class<? extends JobCommand>, JobCommand> jobFactory;

    @Inject
    public JobCommandFactory(ApplicationContext applicationContext) {
        this.jobFactory = (jobType -> {
            final Object commandBean = applicationContext.getBean(jobType);

            if (!(commandBean instanceof JobCommand)) {
                throw new GenericUncheckedException(commandBean.getClass() + " is not a JobCommand");
            }

            return (JobCommand) commandBean;
        });
    }

    public JobCommandFactory(Function<Class<? extends JobCommand>, JobCommand> jobFactory) {
        this.jobFactory = jobFactory;
    }

    public JobCommand toCommand(Job job) {

        final JobCommand command = jobFactory.apply(job.getType().getCommandClass());
        command.init(job.getUuid(), job.getData());

        return command;
    }


}
