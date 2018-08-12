package org.opencomp.vid.model.probe;

public class ExternalComponentStatus {
    public enum Component {AAI, MSO}
    private Component component;
    private boolean available;
    private HttpRequestMetadata metadata;

    public ExternalComponentStatus(){}

    public ExternalComponentStatus(Component component, boolean isAvailable, HttpRequestMetadata metadata) {
        this.component = component;
        this.available = isAvailable;
        this.metadata = metadata;
    }

    public Component getComponent() {
        return component;
    }

    public boolean isAvailable() {
        return available;
    }

    public HttpRequestMetadata getMetadata() {
        return metadata;
    }
}