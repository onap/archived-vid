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