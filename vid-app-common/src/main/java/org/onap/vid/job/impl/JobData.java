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

package org.onap.vid.job.impl;

import org.onap.vid.job.JobType;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class JobData {

    private TreeMap<JobType, Map<String, Object>> commandData;
    private JobSharedData sharedData;

    public JobData() {
        commandData = new TreeMap<>();
        sharedData = new JobSharedData();
    }

    public JobData(TreeMap<JobType, Map<String, Object>> commandData, JobSharedData sharedData) {
        this.commandData = commandData;
        this.sharedData = sharedData;
    }

    public TreeMap<JobType, Map<String, Object>> getCommandData() {
        return commandData;
    }

    public void setCommandData(TreeMap<JobType, Map<String, Object>> commandData) {
        this.commandData = commandData;
    }

    public JobSharedData getSharedData() {
        return sharedData;
    }

    public void setSharedData(JobSharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof JobData))
            return false;
        JobData jobData = (JobData) o;
        return Objects.equals(getCommandData(), jobData.getCommandData()) &&
                Objects.equals(getSharedData(), jobData.getSharedData());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCommandData(), getSharedData());
    }
}
