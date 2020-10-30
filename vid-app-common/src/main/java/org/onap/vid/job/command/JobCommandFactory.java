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

package org.onap.vid.job.command;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.impl.JobWorker;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.Function;

@Component
public class JobCommandFactory {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(JobCommandFactory.class);

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
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "job: " + job);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "job type: " + job.getType());
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "job command class: " + job.getType().getCommandClass());
        command.init(job.getSharedData(), job.getData());

        return command;
    }


}
