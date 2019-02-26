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

import java.util.List;

import org.junit.Test;

public class TaskListTest {

    private TaskList createTestSubject() {
        return new TaskList();
    }

    @Test
    public void testGetTaskList() throws Exception {
        TaskList testSubject;
        List<Task> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTaskList();
    }

    @Test
    public void testSetTaskList() throws Exception {
        TaskList testSubject;
        List<Task> taskList = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setTaskList(taskList);
    }
}
