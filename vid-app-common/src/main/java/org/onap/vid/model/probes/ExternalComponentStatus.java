package org.onap.vid.model.probes;

public class ExternalComponentStatus {
    public enum Component {AAI, MSO}
    private final Component component;
    private final boolean available;
    private final StatusMetadata metadata;

    public ExternalComponentStatus(Component component, boolean isAvailable, StatusMetadata metadata) {
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

    public StatusMetadata getMetadata() {
        return metadata;
    }
}
