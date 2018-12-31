package org.onap.vid.aai.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.common.collect.ImmutableMap
import org.springframework.stereotype.Component

//I use a regular kotlin class because I want that when jackson read
//a json with null values (or missing fields) they would get default values.
//for other cases it's better to use data class for POJO class
//for more information you can read here :
//https://github.com/FasterXML/jackson-module-kotlin/issues/130
class CacheConfig constructor(
        isActive: Boolean?,
        expireAfterWriteHours: Long?,
        refreshAfterWriteSeconds: Long?) {
    val isActive: Boolean = isActive ?: true
    val expireAfterWriteHours: Long = expireAfterWriteHours ?: 24L
    val refreshAfterWriteSeconds: Long = refreshAfterWriteSeconds ?: 10L

    companion object {
        val defaultCacheConfig = CacheConfig(null, null, null)
    }

}


interface CacheConfigProvider {
    fun getCacheConfig(cacheName:String): CacheConfig
}

@Component
class CacheConfigProviderImpl() : CacheConfigProvider {
    private val mapper = ObjectMapper().apply { registerModule(KotlinModule()) }

    private fun readMapOfCacheConfig(): Map<String, CacheConfig> {
        val configInputStream = CacheConfigProviderImpl::class.java.classLoader.getResourceAsStream("cacheConfig.json")

        return if (configInputStream == null) {
            ImmutableMap.of()
        } else {
            mapper.readValue(configInputStream, object : TypeReference<Map<String, CacheConfig>>() {})
        }
    }

    override fun getCacheConfig(cacheName: String): CacheConfig {
        return readMapOfCacheConfig()[cacheName] ?: CacheConfig.defaultCacheConfig
    }
}


