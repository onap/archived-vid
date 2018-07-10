package org.onap.vid.job;

import org.onap.vid.job.command.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum JobType {

    HttpCall(HttpCallCommand.class),
    AggregateState(AggregateStateCommand.class),
    ServiceInstantiation(ServiceInstantiationCommand.class),
    InProgressStatus(InProgressStatusCommand.class),
    NoOp(NoOpCommand.class);

    private static final Map<Class, JobType> REVERSE_MAP = Stream.of(values()).collect(Collectors.toMap(t -> t.getCommandClass(), t -> t));

    private final Class commandClass;

    <T extends JobCommand> JobType(Class<T> commandClass) {
        this.commandClass = commandClass;
    }

    public Class getCommandClass() {
        return commandClass;
    }
    static JobType jobTypeOf(Class commandClass) {
        return REVERSE_MAP.get(commandClass);
    }
}
