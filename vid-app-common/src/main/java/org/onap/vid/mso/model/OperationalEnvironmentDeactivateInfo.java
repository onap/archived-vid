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

package org.onap.vid.mso.model;

import com.google.common.base.MoreObjects;

public class OperationalEnvironmentDeactivateInfo {
    private final String userId;
    private final String operationalEnvironmentId;

    public OperationalEnvironmentDeactivateInfo(String userId, String operationalEnvironmentId) {
        this.userId = userId;
        this.operationalEnvironmentId = operationalEnvironmentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOperationalEnvironmentId() {
        return operationalEnvironmentId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("operationalEnvironmentId", operationalEnvironmentId)
                .add("userId", userId)
                .toString();
    }
}
