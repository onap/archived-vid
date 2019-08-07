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

package org.onap.vid.asdc.parser

import org.onap.vid.model.*
import org.springframework.stereotype.Component

@Component
class ServiceModelInflator {

    data class Names (val modelCustomizationName: String?, val modelKey: String?)

    fun toNamesByVersionId(model: ServiceModel): Map<String, Names> {
        return emptyMap<String, Names>()
                .plus(inflate(model.networks))
                .plus(inflate(model.vnfs))
                .plus(inflate(model.vnfGroups))
                .plus(inflate(model.vrfs))
                .plus(inflate(model.collectionResources))
    }

    private fun inflate(instances: Map<String, *>): Map<String, Names> {
        return instances.entries.map { inflate(it.key, it.value) }.fold(emptyMap()) { acc, it -> acc.plus(it) }
    }

    private fun inflate(modelKey: String, vnf: VNF): Map<String, Names> {
        return mapOf(vnf.uuid to Names(vnf.modelCustomizationName, modelKey))
                .plus(inflate(vnf.vfModules))
                .plus(inflate(vnf.volumeGroups))
    }

    private fun inflate(modelKey: String, cr: CR): Map<String, Names> {
        return mapOf(cr.uuid to Names(null, modelKey))
                .plus(inflate(cr.networksCollection))
    }

    private fun inflate(modelKey: String, instance: Any?): Map<String, Names> {
        return when (instance) {
            is Network -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is VfModule -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is VolumeGroup -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is ResourceGroup -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is VNF -> inflate(modelKey, instance)
            is CR -> inflate(modelKey, instance)
            is NetworkCollection -> mapOf(instance.uuid to Names(null, modelKey))
            is Node -> mapOf(instance.uuid to Names(null, modelKey))

            else -> {
                // sink
                emptyMap()
            }
        }
    }

}
