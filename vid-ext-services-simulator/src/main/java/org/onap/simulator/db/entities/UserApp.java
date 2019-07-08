package org.onap.simulator.db.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "fn_user_role")
public class UserApp implements Serializable {

    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;
    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
