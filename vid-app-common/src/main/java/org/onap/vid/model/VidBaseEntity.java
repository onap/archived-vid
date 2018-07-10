package org.onap.vid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onap.portalsdk.core.domain.support.DomainVo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@MappedSuperclass
public class VidBaseEntity extends DomainVo {

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
}
