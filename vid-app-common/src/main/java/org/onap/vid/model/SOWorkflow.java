package org.onap.vid.model;

public class SOWorkflow {
    private String name;
    private Long id;

    public SOWorkflow(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SOWorkflow() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public SOWorkflow clone() {
        return new SOWorkflow(this.id,this.name);
    }
}
