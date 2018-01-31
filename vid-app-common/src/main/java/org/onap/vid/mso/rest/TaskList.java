package org.onap.vid.mso.rest;

import java.util.List;

public class TaskList {

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    private List<Task> taskList;
}
