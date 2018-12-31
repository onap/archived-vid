package org.onap.vid.model;

public class ServiceProxy extends Node {

    private String sourceModelUuid;

    private String sourceModelInvariant;

    private String sourceModelName;

    public String getSourceModelUuid() {
        return sourceModelUuid;
    }

    public void setSourceModelUuid(String sourceModelUuid) {
        this.sourceModelUuid = sourceModelUuid;
    }

    public String getSourceModelInvariant() {
        return sourceModelInvariant;
    }

    public void setSourceModelInvariant(String sourceModelInvariant) {
        this.sourceModelInvariant = sourceModelInvariant;
    }

    public String getSourceModelName() {
        return sourceModelName;
    }

    public void setSourceModelName(String sourceModelName) {
        this.sourceModelName = sourceModelName;
    }

}
