package org.onap.vid.model;

//import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//import javax.persistence.*;

@Entity
@Table(name = "vid_category_parameter", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CategoryParameter extends VidBaseEntity {

    public enum Family {
        PARAMETER_STANDARDIZATION,
        TENANT_ISOLATION
    }

    private String name;
    private boolean idSupported;

    @Column(name = "FAMILY")
    @Enumerated(EnumType.STRING)
    private String family;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    private Set<CategoryParameterOption> options = new HashSet<>(0);

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "NAME", unique = true, nullable = false, length=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
