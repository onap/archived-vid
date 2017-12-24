package org.openecomp.vid.model;

//import org.hibernate.annotations.Table;
import org.openecomp.portalsdk.core.domain.support.DomainVo;

//import javax.persistence.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vid_category_parameter", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CategoryParameter extends DomainVo {

    private String name;
    private boolean idSupported;
    private Set<CategoryParameterOption> options = new HashSet<>(0);

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    public Long getId() {
        return super.getId();
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

    @Column(name = "NAME", unique = true, nullable = false, length=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "categoryParameter")
    public Set<CategoryParameterOption> getOptions() {
        return options;
    }

    public void setOptions(Set<CategoryParameterOption> options) {
        this.options = options;
    }

    public boolean addOption(CategoryParameterOption option) {
        return options.add(option);
    }

    @Column(name = "ID_SUPPORTED")
    public boolean isIdSupported() {
        return idSupported;
    }

    public void setIdSupported(boolean idSupported) {
        this.idSupported = idSupported;
    }
}
