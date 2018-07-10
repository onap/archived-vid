package org.onap.vid.aai.model;

public class InstanceGroupInfo {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private String name;

    public InstanceGroupInfo(String name){
        this.name = name;
        this.type = "instance-group";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
