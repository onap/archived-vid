package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    @JsonProperty("service-instance")
    private ServiceInstance serviceInstance;
    @JsonProperty("collection")
    private Collection collection;
    @JsonProperty("instance-group")
    private InstanceGroup instanceGroup;
    @JsonProperty("networks")
    private List<Network> networks;

    public Result(){
        this.networks = new ArrayList<>();
    }


    @JsonProperty("service-instance")
    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    @JsonProperty("service-instance")
    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    @JsonProperty("collection")
    public Collection getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @JsonProperty("instance-group")
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }

    @JsonProperty("instance-group")
    public void setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
    }

    @JsonProperty("networks")
    public List<Network> getNetworks() { return networks; }

    @JsonProperty("networks")
    public void setNetworks(List<Network> networks) { this.networks = networks; }


}
