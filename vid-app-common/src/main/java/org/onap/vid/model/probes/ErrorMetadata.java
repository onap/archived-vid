package org.onap.vid.model.probes;

public class ErrorMetadata extends StatusMetadata {
    public ErrorMetadata(String description, long duration) {
        super(description, duration);
    }
}
