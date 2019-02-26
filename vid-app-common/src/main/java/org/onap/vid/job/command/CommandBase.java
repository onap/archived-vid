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

import org.onap.vid.job.impl.JobSharedData;

import javax.inject.Inject;


public abstract class CommandBase  {

    @Inject
    protected CommandUtils commandUtils;

    private JobSharedData sharedData;

    protected CommandBase init(JobSharedData sharedData) {
        this.setSharedData(sharedData);
        return this;
    }

    public JobSharedData getSharedData() {
        return sharedData;
    }

    private void setSharedData(JobSharedData sharedData) {
        this.sharedData = sharedData;
    }
}
