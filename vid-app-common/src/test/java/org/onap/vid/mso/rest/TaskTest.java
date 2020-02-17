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

package org.onap.vid.mso.rest;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.Test;

public class TaskTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private String templateTaskJson(String insertion) {
        return ""
            + "{ "
            + "  \"taskId\": \"taskId\", "
            + "  \"type\": \"type\", "
            + "  \"nfRole\": \"nfRole\", "
            + "  \"subscriptionServiceType\": \"subscriptionServiceType\", "
            + "  \"originalRequestId\": \"originalRequestId\", "
            + "  \"originalRequestorId\": \"originalRequestorId\", "
            + "  \"buildingBlockName\": \"buildingBlockName\", "
            + "  \"buildingBlockStep\": \"buildingBlockStep\", "
            + "  \"errorSource\": \"errorSource\", "
            + "  \"errorCode\": \"errorCode\", "
            + "  \"errorMessage\": \"errorMessage\", "
            + insertion
            + "  \"validResponses\": [ "
            + "    \"a\", "
            + "    \"b\", "
            + "    \"c\" "
            + "  ] "
            + "} ";
    }

    private final String TASK_JSON = templateTaskJson(""
        + "  \"description\": \"description\", "
        + "  \"timeout\": \"timeout\", "
    );

    private final String TASK_JSON_WITHOUT_TIMEOUT = templateTaskJson("");

    private Task newTaskWithPopulatedFields() {
        Task task = TestUtils.setStringsInStringFields(new Task());
        task.setValidResponses(ImmutableList.of("a", "b", "c"));
        return task;
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(Task.class, hasValidGettersAndSetters());
    }

    @Test
    public void serializeTask() throws IOException {
        assertThat(
            mapper.writeValueAsString(newTaskWithPopulatedFields()),
            jsonEquals(TASK_JSON)
        );
    }

    @Test
    public void deserializeTask() throws IOException {
        assertThat(
            mapper.readValue(TASK_JSON, Task.class),
            is(newTaskWithPopulatedFields())
        );
    }

    @Test
    public void deserializeTaskWithoutTimeout() throws IOException {
        /*
        SO may return no timeout, and therefore no description as well
         */
        final Task taskWithoutTimeout = newTaskWithPopulatedFields();
        taskWithoutTimeout.setDescription(null);
        taskWithoutTimeout.setTimeout(null);

        assertThat(
            mapper.readValue(TASK_JSON_WITHOUT_TIMEOUT, Task.class),
            is(taskWithoutTimeout)
        );
    }
}
