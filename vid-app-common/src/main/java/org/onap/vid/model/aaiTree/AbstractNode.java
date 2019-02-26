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

package org.onap.vid.model.aaiTree;

import org.onap.vid.model.Action;
import org.onap.vid.mso.model.ModelInfo;

public abstract class AbstractNode {

    protected final Action action;
    protected String instanceName;
    protected String instanceId;
    protected String orchStatus;
    protected String productFamilyId;
    protected String lcpCloudRegionId;
    protected String tenantId;
    protected ModelInfo modelInfo;

    public AbstractNode() {
        this.action = Action.None;
    }

    public final Action getAction() {
        return action;
    }

    public final String getInstanceName() {
        return instanceName;
    }

    public final String getInstanceId() {
        return instanceId;
    }

    public final String getOrchStatus() {
        return orchStatus;
    }

    public final String getProductFamilyId() {
        return productFamilyId;
    }

    public final String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    public final String getTenantId() {
        return tenantId;
    }

    public final ModelInfo getModelInfo() {
        return modelInfo;
    }

}
