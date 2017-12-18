package org.openecomp.vid.model;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ServiceProxy extends Node {

    /** The Constant LOG. */
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(ServiceProxy.class);

    /** The Constant dateFormat. */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

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
