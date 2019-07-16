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

import org.onap.vid.job.impl.JobSharedData;

import java.util.Map;


/**
 * A callable instance, with serializable characteristics.
 * Represents a step in a chain of steps, which eventualy
 * resides into a packing Job.
 */
public interface JobCommand {

    /**
     * Initialize the command state
     * @param sharedData shared data cross all job commands
     * @param commandData An input to be set into the command. Each implementation may expect different keys in the map.
     * @return Returns itself
     */
    default JobCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        return this;
    }

    /**
     * @return Returns the inner state of the command. This state, once passed into init(), should
     *         bring the command back to it's state.
     */
    Map<String, Object> getData();

    /**
     * Execute the command represented by this instance. Assumes the instance is already init().
     * @return A NextCommand containing the next command in chain of commands, or null if chain
     *         should be terminated. Might return itself (packed in a NextCommand).
     */
    NextCommand call();

    default JobType getType() {
        return JobType.jobTypeOf(this.getClass());
    }

}
