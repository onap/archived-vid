/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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
package org.onap.vid.changeManagement;

import org.onap.vid.mso.model.CloudConfiguration;

public class WorkflowRequestDetail {


    private CloudConfiguration cloudConfiguration;

    private RequestParameters requestParameters;


    public CloudConfiguration getCloudConfiguration() {
        return cloudConfiguration;
    }

    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }


    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }

}
