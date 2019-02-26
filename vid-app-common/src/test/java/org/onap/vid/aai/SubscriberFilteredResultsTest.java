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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.EcompRole;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleValidator;

public class SubscriberFilteredResultsTest {

    private SubscriberFilteredResults createTestSubject() {
        ArrayList<Role> list = new ArrayList<Role>();
        list.add(new Role(EcompRole.READ, "a", "a", "a"));
        RoleValidator rl=new RoleValidator(list);
        SubscriberList sl = new SubscriberList();
        sl.customer = new ArrayList<org.onap.vid.model.Subscriber>();
        sl.customer.add(new org.onap.vid.model.Subscriber());
        return new SubscriberFilteredResults(rl, sl, "OK", 200);
    }

    @Test
    public void testGetSubscriberList() throws Exception {
        SubscriberFilteredResults testSubject;
        SubscriberListWithFilterData result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSubscriberList();
    }

    @Test
    public void testSetSubscriberList() throws Exception {
        SubscriberFilteredResults testSubject;
        SubscriberListWithFilterData subscriberList = null;

        // default test
        testSubject = createTestSubject();
        //testSubject.setSubscriberList(subscriberList);
        testSubject.getSubscriberList();
    }
}
