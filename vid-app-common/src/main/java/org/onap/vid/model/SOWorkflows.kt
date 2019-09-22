/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.model

import com.google.common.collect.Lists

enum class WorkflowSource(val source: String) {
    SDC("sdc"), NATIVE("native")
}

data class SOWorkflow constructor(
        val id: String,
        val name: String,
        val source: WorkflowSource,
        val workflowInputParameters: List<WorkflowInputParameter>
) {
    fun clone(): SOWorkflow {
        return copy()
    }
}

data class SOWorkflows @JvmOverloads constructor(
        val workflows: List<SOWorkflow> = emptyList()) {
    fun clone(): SOWorkflows {
        return copy(workflows.toMutableList())
    }
}

enum class SOWorkflowType(
        val type: String) {
    text("text")
}

enum class LocalWorkflowType(
        val type: String) {
    text("text"),
    FILE("FILE")
}

data class SOWorkflowParameterDefinition constructor(
        val id: Long,
        val name: String,
        val pattern: String,
        val type: SOWorkflowType,
        val required: Boolean)

data class SOWorkflowParameterDefinitions constructor(
        val parameterDefinitions: List<SOWorkflowParameterDefinition> = emptyList()) {
    fun clone(): SOWorkflowParameterDefinitions {
        return copy(parameterDefinitions.toMutableList())
    }
}

data class LocalWorkflowParameterDefinition @JvmOverloads constructor(
        val id: Long,
        val name: String,
        val displayName: String,
        val required: Boolean,
        val type: LocalWorkflowType,
        val pattern: String? = null,
        val msgOnPatternError: String? = null,
        val msgOnContentError: String? = null,
        val acceptableFileType: String? = null
)

data class LocalWorkflowParameterDefinitions constructor(
        val parameterDefinitions: List<LocalWorkflowParameterDefinition> = emptyList()
) {
    fun clone(): LocalWorkflowParameterDefinitions {
        return copy(parameterDefinitions.toMutableList())
    }
}


data class ArtifactInfo constructor(
        val artifactType: String,
        val artifactUuid: String,
        val artifactName: String,
        val artifactVersion: String,
        val artifactDescription: String? = null,
        val workflowName: String,
        val operationName: String? = null,
        val workflowSource: String,
        val workflowResourceTarget: String
)

data class ActivitySequenceItem constructor(
        val name: String,
        val description: String
)

data class WorkflowInputParameter constructor(
        val label: String,
        val inputType: String,
        val required: Boolean,
        val validation: List<InputParameterValidation>? = Lists.newArrayList(),
        val soFieldName: String,
        val soPayloadLocation: String?,
        val description: String?

)

data class InputParameterValidation constructor(
        val maxLength: String?,
        val allowableChars: String?
)

data class WorkflowSpecification constructor(
        val artifactInfo: ArtifactInfo,
        val activitySequence: List<ActivitySequenceItem>? = Lists.newArrayList(),
        val workflowInputParameters: List<WorkflowInputParameter>
)

data class WorkflowSpecificationWrapper constructor(
        val workflowSpecification: WorkflowSpecification
)

data class SOWorkflowList constructor(
        val workflowSpecificationList: List<WorkflowSpecificationWrapper>? = Lists.newArrayList()
)
