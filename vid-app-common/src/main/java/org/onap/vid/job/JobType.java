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

package org.onap.vid.job;

import org.onap.vid.job.command.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.onap.vid.utils.Streams.not;

public enum JobType {

    HttpCall(HttpCallCommand.class),
    AggregateState(AggregateStateCommand.class),
    ServiceInstantiation(MacroServiceInstantiationCommand.class),
    MacroServiceInstantiation(MacroServiceInstantiationCommand.class),
    ALaCarteServiceInstantiation(ALaCarteServiceInstantiationCommand.class),
    ALaCarteService(ALaCarteServiceCommand.class),
    VnfInstantiation(VnfInstantiationCommand.class),
    VfmoduleInstantiation(VfmoduleInstantiationCommand.class),
    VolumeGroupInstantiation(VolumeGroupInstantiationCommand.class),
    VolumeGroupInProgressStatus(VolumeGroupInProgressStatusCommand.class),
    NetworkInstantiation(NetworkInstantiationCommand.class),
    InstanceGroupInstantiation(InstanceGroupInstantiationCommand.class),
    InstanceGroup(InstanceGroupCommand.class),
    InProgressStatus(ServiceInProgressStatusCommand.class),
    ResourceInProgressStatus(ResourceInProgressStatusCommand.class),
    VnfInProgressStatus(VnfInProgressStatusCommand.class),
    Watching(WatchingCommand.class),
    WatchingBaseModule(WatchingCommandBaseModule.class),
    NoOp(NoOpCommand.class);

    private static final Map<Class, JobType> REVERSE_MAP = Stream.of(values())
            .filter(not(jobType -> jobType.equals(ServiceInstantiation)))
            .collect(Collectors.toMap(t -> t.getCommandClass(), t -> t));

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
