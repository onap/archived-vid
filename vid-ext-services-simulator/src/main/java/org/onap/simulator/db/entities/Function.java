package org.onap.simulator.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "fn_function")
public class Function {

    @Id
    @Column(name = "function_cd")
    private String code;
    @Column(name = "function_name")
    private String name;
    private String type;
    private String action;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RoleFunction [code=" + code + ", name=" + name + ", type=" + type + ", action=" + action + "]";
    }


}
