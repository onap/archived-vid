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

package org.onap.vid.aai;

import org.onap.vid.model.ProxyResponse;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

/**
 * Created by Oren on 7/5/17.
 */

public class SubscriberFilteredResults extends ProxyResponse {

    private SubscriberListWithFilterData subscriberList;

    public SubscriberFilteredResults(RoleValidator roleValidator,SubscriberList subscribers, String errorMessage, int aaiHttpCode) {
        this.subscriberList = new SubscriberListWithFilterData(subscribers,roleValidator);
        this.errorMessage = errorMessage;
        this.httpCode = aaiHttpCode;
    }


    public SubscriberListWithFilterData getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(SubscriberListWithFilterData subscriberList) {
        this.subscriberList = subscriberList;
    }
}
