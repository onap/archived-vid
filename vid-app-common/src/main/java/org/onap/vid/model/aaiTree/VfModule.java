/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
