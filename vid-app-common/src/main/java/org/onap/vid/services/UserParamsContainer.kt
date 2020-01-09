package org.onap.vid.services

import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue
import org.onap.vid.mso.model.UserParamTypes

class UserParamsContainer(instanceParams: Map<String, String>?, supplementaryParams: List<UserParamNameAndValue>?) {

    val params:Map<String, String>

    init {
        params = aggregateAllInstanceParams(instanceParams, supplementaryParams)
    }

    private fun aggregateAllInstanceParams(
            instanceParams: Map<String, String>?,
            supplementaryParams: List<UserParamNameAndValue>?)
            : Map<String, String> {
        val instanceParamsSafe: Map<String, String> = instanceParams ?: emptyMap()
        val supplementaryParamsSafe: Map<String, String> =
                supplementaryParams?.associate{ it.name to it.value } ?: emptyMap()

        return instanceParamsSafe.plus(supplementaryParamsSafe)
    }

    fun toALaCarte(): List<UserParamTypes> = toUserParamNameAndValue()

    fun toMacroPre1806() : List<UserParamTypes> = toUserParamNameAndValue()

    fun toMacroPost1806() : List<Map<String, String>> = toListOfMap()

    private fun toUserParamNameAndValue(): List<UserParamNameAndValue> {
        return params.map{UserParamNameAndValue(it.key, it.value)}.toList()
    }

    private fun toListOfMap() : List<Map<String, String>> {
        return listOf(params)
    }
}


