/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 IBM.
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

import org.onap.vid.mso.model.RequestInfo;

/**
 * Created by Oren on 9/5/17.
 */
public class UpdateRequestInfo {
	
	public String source;

    public  Boolean suppressRollback;

    public String requestorId;

    public UpdateRequestInfo() {
    }


    public UpdateRequestInfo(RequestInfo requestInfo) {
        this.requestorId = requestInfo.getRequestorId();
        this.suppressRollback = requestInfo.getSuppressRollback();
        this.source = requestInfo.getSource();
    }
    

}
