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
