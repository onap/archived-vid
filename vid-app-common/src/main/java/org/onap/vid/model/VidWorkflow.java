package org.onap.vid.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onap.portalsdk.core.domain.support.DomainVo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "vid_workflow")
public class VidWorkflow extends DomainVo {

    private String wokflowName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKFLOW_DB_ID")
    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "CREATED_DATE")
    @JsonIgnore
    public Date getCreated() {
        return super.getCreated();
    }

    @Override
    @Column(name = "MODIFIED_DATE")
    @JsonIgnore
    public Date getModified() {
        return super.getModified();
    }

    @Override
    @Transient
    @JsonIgnore
    public Long getCreatedId() {
        return super.getCreatedId();
    }

    @Override
    @Transient
    @JsonIgnore
    public Long getModifiedId() {
        return super.getModifiedId();
    }

    @Override
    @Transient
    @JsonIgnore
    public Serializable getAuditUserId() {
        return super.getAuditUserId();
    }

    @Override
    @Transient
    @JsonIgnore
    public Long getRowNum() {
        return super.getRowNum();
    }

    @Override
    @Transient
    @JsonIgnore
    public Set getAuditTrail() {
        return super.getAuditTrail();
    }

    @Column(name = "WORKFLOW_APP_NAME")
    public String getWokflowName() {
        return wokflowName;
    }

    public void setWokflowName(String wokflowName) {
        this.wokflowName = wokflowName;
    }
}
