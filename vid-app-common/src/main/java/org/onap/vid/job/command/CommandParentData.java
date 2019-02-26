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

package org.onap.vid.job.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.mso.model.ModelInfo;

import java.util.HashMap;
import java.util.Map;

public class CommandParentData {


    public enum CommandDataKey{
        SERVICE_MODEL_INFO,
        SERVICE_INSTANCE_ID,
        VNF_INSTANCE_ID,
        VNF_MODEL_INFO,
        VG_INSTANCE_ID,
        ;
    }

    private static final String RESOURCE_INSTANCE_IDS = "resourceInstancesIds";
    private static final String RESOURCE_MODEL_INFOS = "resourceModelInfos";

    private final TypeReference<Map<CommandDataKey, String>> mapCommandKeyToString =
            new TypeReference<Map<CommandDataKey, String>> () {};

    private  final TypeReference<Map<CommandDataKey, ModelInfo>> mapCommandKeyToModelInfo =
            new TypeReference<Map<CommandDataKey, ModelInfo>> () {};


    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<CommandDataKey,ModelInfo> getModelInfosByCommandData(Map<String, Object> commandData) {
        Object object = commandData.get(RESOURCE_MODEL_INFOS);
        if (object != null) {
            return objectMapper.convertValue(object, mapCommandKeyToModelInfo);
        }
        return null;
    }

    private Map<CommandDataKey,String> getInstanceIdsByCommandData(Map<String, Object> commandData) {
        Object object = commandData.get(RESOURCE_INSTANCE_IDS);
        if (object != null) {
            return objectMapper.convertValue(object, mapCommandKeyToString);
        }
        return null;
    }

    public Map<String, Object> getParentData() {
        Map<String, Object> data = new HashMap<>();
        data.put(RESOURCE_INSTANCE_IDS, resourceInstancesIds);
        data.put(RESOURCE_MODEL_INFOS, resourceModelInfos);
        return data;
    }
    private Map<CommandDataKey, String> resourceInstancesIds = new HashMap<>();
    private Map<CommandDataKey, ModelInfo> resourceModelInfos = new HashMap<>();

    public void addModelInfo(CommandDataKey modelInfoKey, ModelInfo modelInfo) {
        resourceModelInfos.put(modelInfoKey, modelInfo);
    }

    public void addInstanceId(CommandDataKey instanceIdKey, String instanceId) {
         resourceInstancesIds.put(instanceIdKey, instanceId);
    }
    public ModelInfo getModelInfo(CommandDataKey modelInfoKey) {
        return resourceModelInfos.get(modelInfoKey);
    }

    public String getInstanceId(CommandDataKey instanceIdKey) {
        return resourceInstancesIds.get(instanceIdKey);
    }

    public CommandParentData initParentData(Map<String, Object> commandData) {
        resourceModelInfos = getModelInfosByCommandData(commandData);
        resourceInstancesIds = getInstanceIdsByCommandData(commandData);
        return this;
    }

}
