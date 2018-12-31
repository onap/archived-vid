package org.onap.vid.model.aaiTree;

import org.onap.vid.aai.util.AAITreeConverter;

import static org.onap.vid.aai.util.AAITreeConverter.IS_BASE_VF_MODULE;

public class VfModule extends Node {

    private boolean isBase;
    private String volumeGroupName;

    public VfModule(AAITreeNode node) {
        super(node, AAITreeConverter.ModelType.vfModule);
    }

    public boolean getIsBase() {
        return isBase;
    }

    public void setIsBase(boolean isBase) {
        this.isBase = isBase;
    }

    public String getVolumeGroupName() {
        return volumeGroupName;
    }

    public void setVolumeGroupName(String volumeGroupName) {
        this.volumeGroupName = volumeGroupName;
    }

    public static VfModule from(AAITreeNode node) {
        VfModule vfModule = new VfModule(node);

        if (node.getAdditionalProperties().get(IS_BASE_VF_MODULE) != null) {
            vfModule.setIsBase(Boolean.valueOf(node.getAdditionalProperties().get(IS_BASE_VF_MODULE).toString()));
        }

        return vfModule;
    }
}
