package org.onap.simulator.db.entities;

import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Type;

@Entity(name = "fn_user")
public class User {

    @Id @Column(name = "user_id")
    private Integer id;
    @Column(name = "created_id")
    private Integer createdId;
    @Column(name = "modified_id")
    private Integer modifiedId;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "org_manager_userid")
    private String managerId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleInitial;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address_id")
    private BigDecimal addressId;
    @Column(name = "alert_method_cd")
    private String alertMethodCd;
    private String hrid;
    @Column(name = "org_user_id")
    private String orgUserId;
    @Column(name = "ADDRESS_LINE_1")
    private String address1;
    @Column(name = "ADDRESS_LINE_2")
    private String address2;
    @Column(name = "login_id")
    private String loginId;
    @Column(name = "login_pwd")
    private String loginPwd;
    @Column(name = "active_yn", columnDefinition = "varchar")
    @Type(type="yes_no")
    private Boolean active;
    @Column(name = "is_internal_yn", columnDefinition = "varchar")
    @Type(type="yes_no")
    private Boolean internal;
    @Column(name = "timezone")
    private Integer timeZoneId;

    @OneToMany(cascade = CascadeType.ALL, targetEntity=UserApp.class, mappedBy="userId")
    private Set<UserApp> userApps;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    public Integer getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(Integer modifiedId) {
        this.modifiedId = modifiedId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getAddressId() {
        return addressId;
    }

    public void setAddressId(BigDecimal addressId) {
        this.addressId = addressId;
    }

    public String getAlertMethodCd() {
        return alertMethodCd;
    }

    public void setAlertMethodCd(String alertMethodCd) {
        this.alertMethodCd = alertMethodCd;
    }

    public String getHrid() {
        return hrid;
    }

    public void setHrid(String hrid) {
        this.hrid = hrid;
    }

    public String getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public Integer getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(Integer timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public Set<UserApp> getUserApps() {
        return userApps;
    }

    public void setUserApps(Set<UserApp> userApps) {
        this.userApps = userApps;
    }
}
