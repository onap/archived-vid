package org.onap.vid.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "vid_workflow")
public class VidWorkflow extends VidBaseEntity {

    private String wokflowName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKFLOW_DB_ID")
    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Column(name = "WORKFLOW_APP_NAME")
    public String getWokflowName() {
        return wokflowName;
    }

    public void setWokflowName(String wokflowName) {
        this.wokflowName = wokflowName;
    }
}
