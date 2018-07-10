package org.onap.vid.job.command;

import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AggregateStateCommand implements JobCommand {

    @Override
    public NextCommand call() {
        return null;
    }

    @Override
    public Map<String, Object> getData() {
        return Collections.emptyMap();
    }

}
