package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class Project extends AaiRelationResponse {

    private String projectName;

    public String getProjectName() { return projectName; }

    @JsonAlias("project-name")
    public void setProjectName(String projectName) { this.projectName = projectName; }


}
