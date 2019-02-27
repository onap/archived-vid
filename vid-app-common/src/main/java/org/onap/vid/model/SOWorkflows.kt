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

package org.onap.vid.model

data class SOWorkflow constructor(
        val id: Long,
        val name: String) {
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
    STRING("STRING")
}

enum class LocalWorkflowType(
        val type: String) {
    STRING("STRING"),
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
        val required: Boolean,
        val type: LocalWorkflowType,
        val pattern: String? = null,
        val errorPattern: String? = null,
        val errorContent: String? = null,
        val accept: String? = null
)

data class LocalWorkflowParameterDefinitions constructor(
        val parameterDefinitions: List<LocalWorkflowParameterDefinition> = emptyList()
) {
    fun clone(): LocalWorkflowParameterDefinitions {
        return copy(parameterDefinitions.toMutableList())
    }
}

