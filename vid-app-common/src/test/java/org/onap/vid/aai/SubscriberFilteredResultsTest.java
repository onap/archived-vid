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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.EcompRole;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.roles.RoleValidatorFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SubscriberFilteredResultsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private SubscriberFilteredResults subscriberFilteredResults;
    private RoleValidator roleValidator;
    private SubscriberList subscriberList;
    private SubscriberListWithFilterData subscriberListWithFilterData;

    private static final String SUBSCRIBER_JSON_EXAMPLE = "{\n" +
            "  \"global-customer-id\": \"id\",\n" +
            "  \"subscriber-name\": \"name\",\n" +
            "  \"subscriber-type\": \"type\",\n" +
            "  \"resource-version\": \"version\"\n" +
            "}";

    @Before
    public void setUp() throws IOException {
        createTestSubject();
    }

    @Test
    public void testGetSubscriberList(){
        assertEquals(subscriberFilteredResults.getSubscriberList(), subscriberListWithFilterData);
    }

    @Test
    public void testSetSubscriberList() throws Exception {
        subscriberList.customer = new ArrayList<>();
        subscriberList.customer.add(new Subscriber());
        SubscriberListWithFilterData expectedList = createSubscriberList(subscriberList,roleValidator);
        subscriberFilteredResults.setSubscriberList(expectedList);

        assertEquals(subscriberFilteredResults.getSubscriberList(), expectedList);
    }

    private void createTestSubject() throws IOException {
        prepareRoleValidator();
        prepareSubscriberList();
        prepareSubscriberListWithFilterData();
        createSubscriberFilteredResults();
    }

    private void createSubscriberFilteredResults() {
        subscriberFilteredResults =
                new SubscriberFilteredResults(roleValidator, subscriberList, "OK", 200);
        subscriberFilteredResults.setSubscriberList(subscriberListWithFilterData);
    }

    private void prepareSubscriberListWithFilterData() {
        subscriberListWithFilterData = createSubscriberList(subscriberList, roleValidator);
    }

    private void prepareRoleValidator() {
        roleValidator = mock(RoleValidator.class);
    }

    private void prepareSubscriberList() throws IOException {
        Subscriber sampleSubscriber =
                OBJECT_MAPPER.readValue(SUBSCRIBER_JSON_EXAMPLE, Subscriber.class);
        List<Subscriber> expectedListOfSubscribers = Collections.singletonList(sampleSubscriber);
        subscriberList = new SubscriberList(expectedListOfSubscribers);
    }

    private SubscriberListWithFilterData createSubscriberList(SubscriberList subscriberList,
                                                              RoleValidator roleValidator){
        return new SubscriberListWithFilterData(subscriberList,roleValidator);
    }
}
