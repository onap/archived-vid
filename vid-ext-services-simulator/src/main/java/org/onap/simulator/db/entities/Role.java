package org.onap.simulator.db.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "fn_role")
public class Role {
    @Id
    @Column(name = "role_id")
    private Integer id;
    @Column(name = "role_name")
    private String name;
    @Column(name = "active_yn", columnDefinition = "varchar")
    @Type(type="yes_no")
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, targetEntity=RoleFunction.class, mappedBy="id")
    private Set<RoleFunction> roleFunctions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Set<RoleFunction> getRoleFunctions() {
        return roleFunctions;
    }

    public void setRoleFunctions(Set<RoleFunction> roleFunctions) {
        this.roleFunctions = roleFunctions;
    }
}
