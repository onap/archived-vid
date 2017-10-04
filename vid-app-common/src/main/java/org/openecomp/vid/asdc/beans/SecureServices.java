package org.openecomp.vid.asdc.beans;

import java.util.Collection;
import java.util.List;

/**
 * Created by Oren on 6/27/17.
 */
public class SecureServices {

    private Collection<Service> services;
    //Disable roles until AAF integration finishes
    private boolean isReadOnly = false;

    public void setServices(Collection<Service> services) {
        this.services = services;
    }

    public Collection<Service> getServices() {

        return services;
    }
    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

}
