package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class Project extends AaiRelationResponse {

    private String projectName;

    public String getProjectName() { return projectName; }

    @JsonProperty("project-name")
    public void setJsonProjectName(String projectName) { this.projectName = projectName; }


}
