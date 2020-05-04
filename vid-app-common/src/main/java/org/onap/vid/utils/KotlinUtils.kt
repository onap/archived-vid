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

package org.onap.vid.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.lang3.StringUtils.isEmpty
import org.togglz.core.Feature
import org.togglz.core.manager.FeatureManager
import java.util.*

inline fun <reified E: Enum<E>> getEnumFromMapOfStrings(map: Map<String, Any>, key:String, defaultValue:E): E {
    return java.lang.Enum.valueOf(E::class.java, (map.getOrDefault(key, defaultValue.name) as String))
}

fun FeatureManager.isNotActive(feature: Feature) = this.isActive(feature).not()

@JvmField val JACKSON_OBJECT_MAPPER: ObjectMapper = jacksonObjectMapper()

class JoshworksJacksonObjectMapper: io.joshworks.restclient.http.mapper.ObjectMapper {
    override fun writeValue(value: Any?): String? {
        return JACKSON_OBJECT_MAPPER.writeValueAsString(value)
    }

    override fun <T : Any?> readValue(value: String?, valueType: Class<T>?): T? {
        return if (isEmpty(value)) null else JACKSON_OBJECT_MAPPER.readValue(value, valueType)
    }
}

@JvmField val JOSHWORKS_JACKSON_OBJECT_MAPPER:
        io.joshworks.restclient.http.mapper.ObjectMapper = JoshworksJacksonObjectMapper()

fun <T> Iterable<T>.takeUntilIncluding(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (predicate(item)) {
            break
        }
    }
    return list
}