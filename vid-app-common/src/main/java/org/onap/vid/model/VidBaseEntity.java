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
