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

package org.onap.vid.model;

import org.onap.vid.asdc.beans.tosca.Input;
import org.onap.vid.asdc.parser.ToscaParserImpl2.Constants;

import java.util.Map;

import static org.onap.vid.asdc.parser.ToscaParserImpl2.isModuleTypeIsBaseObjectSafe;

public class Group implements MinimalNode {


    /** The uuid. */
    private String uuid;

    /** The invariant uuid. */
    private String invariantUuid;

    /** The customization uuid. */
    private String customizationUuid;

    /** The description. */
    private String description;

    /** The name. */
    private String name;

    /** The version. */
    private String version;

    /** The model customization name. */
    private String modelCustomizationName;

    /** The group properties. */
    private GroupProperties properties;

    private Map<String, Input> inputs;


    /**
     * Gets the model customization name.
     *
     * @return the model customization name
     */
    public String getModelCustomizationName() {
        return modelCustomizationName;
    }
    /**
     * Gets the uuid.
     *
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Gets the invariant uuid.
     *
     * @return the invariant uuid
     */
    public String getInvariantUuid() {
        return invariantUuid;
    }
    /**
     * Gets the customization uuid.
     *
     * @return the invariant uuid
     */
    public String getCustomizationUuid() {
        return customizationUuid;
    }
    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public GroupProperties getProperties() {
        return properties;
    }
    /**
     * Sets the uuid.
     *
     * @param uuid the new uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Sets the invariant uuid.
     *
     * @param invariantUuid the new invariant uuid
     */
    public void setInvariantUuid(String invariantUuid) {
        this.invariantUuid = invariantUuid;
    }
    /**
     * Sets the customization uuid.
     *
     * @param customizationUuid the new customization uuid
     */
    public void setCustomizationUuid(String customizationUuid) {
        this.customizationUuid = customizationUuid;
    }
    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the version.
     *
     * @param version the new version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Input> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Input> inputs) {
        this.inputs = inputs;
    }

    /**
     * Sets the model customization name.
     *
     * @param modelCustomizationName the new model customization name
     */
    public void setModelCustomizationName(String modelCustomizationName) {
        this.modelCustomizationName = modelCustomizationName;
    }
    /**
     * Sets the group properties.
     *
     * @param properties the new model customization name
     */
    public void setProperties(GroupProperties properties) {
        this.properties = properties;
    }



    protected static GroupProperties extractPropertiesForGroup(org.onap.vid.asdc.beans.tosca.Group group){
        String [] propertyKeys = {
                Constants.MIN_VF_MODULE_INSTANCES,
                Constants.MAX_VF_MODULE_INSTANCES,
                Constants.INITIAL_COUNT,
        };
        GroupProperties groupProperties = new GroupProperties();

        for(String propertyKey : propertyKeys){
            Object val = group.getProperties().get(propertyKey);
            if (val != null && val instanceof Integer) {
                setInGroupProperties(groupProperties, propertyKey, (Integer) val);
            }
        }

        groupProperties.setBaseModule(isModuleTypeIsBaseObjectSafe(group.getProperties().get(Constants.VF_MODULE_TYPE)));

        return groupProperties;
    }

    private static void setInGroupProperties(GroupProperties groupProperties, String propertyKey, Integer propertyValue){
        switch (propertyKey) {
            case Constants.MIN_VF_MODULE_INSTANCES:
                groupProperties.setMinCountInstances(propertyValue);
                break;
            case Constants.MAX_VF_MODULE_INSTANCES:
                groupProperties.setMaxCountInstances(propertyValue);
                break;
            case Constants.INITIAL_COUNT:
                groupProperties.setInitialCount(propertyValue);
                break;
            default:
                // do noting
        }
    }
}
