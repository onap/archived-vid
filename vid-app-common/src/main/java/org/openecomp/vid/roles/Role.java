package org.openecomp.vid.roles;

/**
 * Created by Oren on 7/1/17.
 */

public class Role {

    private EcompRole ecompRole;

    private String subscribeName;

    private String serviceType;

    private String tenant;

    public Role(EcompRole ecompRole, String subscribeName, String serviceType, String tenant) {
        this.ecompRole = ecompRole;
        this.subscribeName = subscribeName;
        this.serviceType = serviceType;
        this.tenant = tenant;
    }

    public EcompRole getEcompRole() {
        return ecompRole;
    }


    public String getSubscribeName() {
        return subscribeName;
    }

    public void setSubscribeName(String subscribeName) {
        this.subscribeName = subscribeName;
    }

    public String getServiceType() {
        return serviceType;
    }


    public String getTenant() {
        return tenant;
    }



}
