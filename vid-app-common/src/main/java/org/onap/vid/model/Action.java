package org.onap.vid.model;

public enum Action {
    Create(ServiceInfo.ServiceAction.INSTANTIATE),
    Delete(ServiceInfo.ServiceAction.DELETE),
    None(ServiceInfo.ServiceAction.UPDATE);

    private final ServiceInfo.ServiceAction serviceAction;
    Action(ServiceInfo.ServiceAction serviceAction){
        this.serviceAction = serviceAction;
    }
    public ServiceInfo.ServiceAction getServiceAction() {
        return serviceAction;
    }
}
