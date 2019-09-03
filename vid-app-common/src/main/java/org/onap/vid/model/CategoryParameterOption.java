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

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.onap.portalsdk.core.domain.support.DomainVo;

@Entity
@Table(name = "vid_category_parameter_option")
public class CategoryParameterOption extends DomainVo {

    private String appId;
    private String name;

    private CategoryParameter categoryParameter;

    public CategoryParameterOption() {
    }

    public CategoryParameterOption(String appId, String name, CategoryParameter categoryParameter) {
        setAppId(appId);
        setName(name);
        setCategoryParameter(categoryParameter);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_OPT_DB_ID")
    @Override
    public Long getId() {
        return id;
    }

    @Column(name = "CATEGORY_OPT_APP_ID")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID", nullable=false)
    public CategoryParameter getCategoryParameter() {
        return categoryParameter;
    }

    public void setCategoryParameter(CategoryParameter categoryParameter) {
        this.categoryParameter = categoryParameter;
    }

    @Override
    @Column(name = "CREATED_DATE")
    public Date getCreated() {
        return super.getCreated();
    }

    @Override
    @Column(name = "MODIFIED_DATE")
    public Date getModified() {
        return super.getModified();
    }

    @Override
    @Transient
    public Long getCreatedId() {
        return super.getCreatedId();
    }

    @Override
    @Transient
    public Long getModifiedId() {
        return super.getModifiedId();
    }

    @Override
    @Transient
    public Serializable getAuditUserId() {
        return super.getAuditUserId();
    }

    @Override
    @Transient
    public Long getRowNum() {
        return super.getRowNum();
    }

    @Override
    @Transient
    public Set getAuditTrail() {
        return super.getAuditTrail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryParameterOption that = (CategoryParameterOption) o;

        if (getAppId() != null ? !getAppId().equals(that.getAppId()) : that.getAppId() != null) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        return getCategoryParameter() != null ? getCategoryParameter().equals(that.getCategoryParameter())
            : that.getCategoryParameter() == null;
    }

    @Override
    public int hashCode() {
        int result = getAppId() != null ? getAppId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + hashCodeOfParentCategoryParameter();
        return result;
    }

    private int hashCodeOfParentCategoryParameter() {
        // Don't use getCategoryParameter's hashCode, as it might loop back to self's hasCode
        return (getCategoryParameter() == null || getCategoryParameter().getId() == null)
                ? 0 : getCategoryParameter().getId().hashCode();
    }

    @Override
    public String toString() {
        return "CategoryParameterOption{" +
                "id=" + id +
                ", key='" + appId + '\'' +
                ", value='" + name + '\'' +
                ", categoryParameterId=" + categoryParameter.getId() +
                '}';
    }
}
