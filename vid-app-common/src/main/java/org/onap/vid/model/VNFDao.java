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

package org.onap.vid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onap.portalsdk.core.domain.support.DomainVo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vid_vnf")
public class VNFDao extends DomainVo {

    private String vnfUUID;
    private String vnfInvariantUUID;
    private Set<VidWorkflow> workflows = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VNF_DB_ID")
    @JsonIgnore
    @Override
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

    @Column(name = "VNF_APP_UUID")
    public String getVnfUUID() {
        return vnfUUID;
    }
    
    @Column(name = "VNF_APP_INVARIANT_UUID")
    public String getVnfInvariantUUID() {
        return vnfInvariantUUID;
    }
    

    public void setVnfUUID(String vnfUUID) {
        this.vnfUUID = vnfUUID;
    }
    
    public void setVnfInvariantUUID(String vnfInvariantUUID) {
		this.vnfInvariantUUID = vnfInvariantUUID;
	}

    @ManyToMany(cascade = CascadeType.ALL, fetch =FetchType.EAGER )
    @JoinTable(name = "vid_vnf_workflow", joinColumns = { @JoinColumn(name = "VNF_DB_ID") }, inverseJoinColumns = { @JoinColumn(name = "WORKFLOW_DB_ID") })
    public Set<VidWorkflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(Set<VidWorkflow> workflows) {
        this.workflows = workflows;
    }
}
