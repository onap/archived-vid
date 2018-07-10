package org.onap.vid.model.probes;

public abstract class StatusMetadata {
    protected final long duration;
    protected final String description;

    public StatusMetadata(String description, long duration) {
        this.description = description;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }
}
