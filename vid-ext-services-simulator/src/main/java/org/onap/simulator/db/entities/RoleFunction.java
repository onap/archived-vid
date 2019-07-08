package org.onap.simulator.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "fn_role_function")
public class RoleFunction implements Serializable {

    @Id
    @Column(name = "role_id")
    private Integer id;
    @Id
    @Column(name = "function_cd")
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
