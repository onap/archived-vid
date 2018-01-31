package org.onap.vid.aai.model;


import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by moriya1 on 08/10/2017.
 */
public class Project extends AaiRelationResponse {

    @JsonProperty("project-name")
    private String projectName;

    @JsonProperty("project-name")
    public String getProjectName() { return projectName; }

    @JsonProperty("project-name")
    public void setProjectName(String projectName) { this.projectName = projectName; }


}
