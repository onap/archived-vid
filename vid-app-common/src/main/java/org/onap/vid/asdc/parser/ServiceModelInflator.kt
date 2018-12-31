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
    }

    private fun inflate(instances: Map<String, *>): Map<String, Names> {
        return instances.entries.map { inflate(it.key, it.value) }.fold(emptyMap()) { acc, it -> acc.plus(it) }
    }

    private fun inflate(modelKey: String, vnf: VNF): Map<String, Names> {
        return mapOf(vnf.uuid to Names(vnf.modelCustomizationName, modelKey))
                .plus(inflate(vnf.vfModules))
                .plus(inflate(vnf.volumeGroups))
    }

    private fun inflate(modelKey: String, instance: Any?): Map<String, Names> {
        return when (instance) {
            is Network -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is VfModule -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is VolumeGroup -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is ResourceGroup -> mapOf(instance.uuid to Names(instance.modelCustomizationName, modelKey))
            is VNF -> inflate(modelKey, instance)

            else -> {
                // sink
                emptyMap()
            }
        }
    }

}
