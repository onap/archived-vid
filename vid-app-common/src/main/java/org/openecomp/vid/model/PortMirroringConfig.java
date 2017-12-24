package org.openecomp.vid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.sdc.toscaparser.api.NodeTemplate;
import org.openecomp.sdc.toscaparser.api.RequirementAssignments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PortMirroringConfig extends Node {

    /** The Constant LOG. */
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(PortMirroringConfig.class);

    /** The Constant dateFormat. */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    /** The model customization name. */
    private String modelCustomizationName;

    /** The port miroring requirements for source/collector */
    @JsonIgnore
    private RequirementAssignments requirementAssignments;

    private List<String> sourceNodes;

    private List<String> collectorNodes;

    private boolean configurationByPolicy;

    public PortMirroringConfig() {
        super();
        this.configurationByPolicy = false;
    }

    public String getModelCustomizationName() {
        return modelCustomizationName;
    }

    public void setModelCustomizationName(String modelCustomizationName) {
        this.modelCustomizationName = modelCustomizationName;
    }

    public RequirementAssignments getRequirementAssignments() {
        return requirementAssignments;
    }

    public void setRequirementAssignments(RequirementAssignments requirementAssignments) {
        this.requirementAssignments = requirementAssignments;
    }

    public List<String> getSourceNodes() {
        return sourceNodes;
    }

    public void setSourceNodes(List<String> sourceNodes) {
        this.sourceNodes = sourceNodes;
    }

    public List<String> getCollectorNodes() {
        return collectorNodes;
    }

    public void setCollectorNodes(List<String> collectorNodes) {
        this.collectorNodes = collectorNodes;
    }

    public void setConfigurationByPolicy(boolean configurationByPolicy) {
        this.configurationByPolicy = configurationByPolicy;
    }

    public boolean isConfigurationByPolicy() {
        return configurationByPolicy;
    }
}
