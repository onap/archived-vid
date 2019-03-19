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

package org.onap.vid.mso.rest

data class Task(
        var taskId: String?,
        var type: String?,
        var nfRole: String?,
        var subscriptionServiceType: String?,
        var originalRequestId: String?,
        var originalRequestorId: String?,
        var errorSource: String?,
        var errorCode: String?,
        var errorMessage: String?,
        var buildingBlockName: String?,
        var buildingBlockStep: String?,
        var validResponses: List<String>?
) {
    // i.e. "default constructor", no params
    constructor() : this(
            null, null, null, null,
            null, null, null, null,
            null, null, null, null
    )
}
