package org.onap.vid.aai.model;


import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class ProjectResponse {

    @JsonProperty("project")
    private List<Project> project;


    @JsonProperty("project")
    public List<Project> getProject() {
        return project;
    }

    @JsonProperty("project")
    public void setProject(List<Project> project) {
        this.project = project;
    }


}
