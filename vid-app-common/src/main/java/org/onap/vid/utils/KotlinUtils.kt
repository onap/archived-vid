package org.onap.vid.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

inline fun <reified E: Enum<E>> getEnumFromMapOfStrings(map: Map<String, Any>, key:String, defaultValue:E): E {
    return java.lang.Enum.valueOf(E::class.java, (map.getOrDefault(key, defaultValue.name) as String))
}

val JACKSON_OBJECT_MAPPER: ObjectMapper = jacksonObjectMapper()