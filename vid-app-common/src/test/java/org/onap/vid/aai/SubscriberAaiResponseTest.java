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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubscriberAaiResponseTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final String SUBSCRIBER_JSON_EXAMPLE = "{\n" +
			"  \"global-customer-id\": \"id\",\n" +
			"  \"subscriber-name\": \"name\",\n" +
			"  \"subscriber-type\": \"type\",\n" +
			"  \"resource-version\": \"version\"\n" +
			"}";

	@Test
	public void shouldGetSubscriberList() throws IOException {
		Subscriber sampleSubscriber =
				OBJECT_MAPPER.readValue(SUBSCRIBER_JSON_EXAMPLE, Subscriber.class);
		List<Subscriber> expectedListOfSubscribers = Arrays.asList(sampleSubscriber);
		SubscriberList expectedSubscriberList = new SubscriberList(expectedListOfSubscribers);
		SubscriberAaiResponse subscriberAaiResponse = new SubscriberAaiResponse(expectedSubscriberList, "msg", 200);

		assertEquals(subscriberAaiResponse.getSubscriberList(), expectedSubscriberList);
		assertEquals(subscriberAaiResponse.getSubscriberList().customer.size(), 1);
		assertEquals(subscriberAaiResponse.getSubscriberList().customer.get(0), sampleSubscriber);

	}
}
