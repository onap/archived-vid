/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * 
 * Modifications Copyright (C) 2019 IBM.
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

import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oren on 7/5/17.
 */
public class SubscriberListWithFilterData {
	
	public List<SubscriberWithFilter> customer;

    public SubscriberListWithFilterData(SubscriberList subscriberList, RoleValidator roleValidator){
        List<Subscriber> subscribers = subscriberList != null ? subscriberList.customer : new ArrayList<>();
        customer = new ArrayList<>();
        for (Subscriber subscriber :subscribers){
            SubscriberWithFilter subscriberWithFilter = new SubscriberWithFilter();
            subscriberWithFilter.setIsPermitted(roleValidator.isSubscriberPermitted(subscriber.globalCustomerId));
            subscriberWithFilter.subscriberType = subscriber.subscriberType;
            subscriberWithFilter.resourceVersion = subscriber.resourceVersion;
            subscriberWithFilter.subscriberName = subscriber.subscriberName;
            subscriberWithFilter.globalCustomerId = subscriber.globalCustomerId;
            customer.add(subscriberWithFilter);
        }
    }

}
