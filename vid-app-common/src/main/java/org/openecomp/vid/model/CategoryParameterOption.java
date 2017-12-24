package org.openecomp.vid.model;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "vid_category_parameter_option")
public class CategoryParameterOption extends DomainVo {

    private Long id;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryParameterOption that = (CategoryParameterOption) o;

        if (getAppId() != null ? !getAppId().equals(that.getAppId()) : that.getAppId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getCategoryParameter() != null ? getCategoryParameter().equals(that.getCategoryParameter()) : that.getCategoryParameter() == null;
    }

    @Override
    public int hashCode() {
        int result = getAppId() != null ? getAppId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCategoryParameter() != null ? getCategoryParameter().hashCode() : 0);
        return result;
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
