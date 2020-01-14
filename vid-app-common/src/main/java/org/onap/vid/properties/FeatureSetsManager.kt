package org.onap.vid.properties

import org.apache.commons.io.filefilter.WildcardFileFilter
import org.springframework.web.context.request.RequestContextHolder.getRequestAttributes
import org.springframework.web.context.request.ServletRequestAttributes
import org.togglz.core.Feature
import org.togglz.core.manager.FeatureManager
import org.togglz.core.manager.FeatureManagerBuilder
import org.togglz.core.repository.file.FileBasedStateRepository
import java.io.File
import java.io.FilenameFilter
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest


private const val SLOW_RELOAD = 60_000
private const val COOKIE_NAME = "features.set"

class FeatureSetsManager(private val defaultManager: FeatureManager, private val servletContext: ServletContext) : FeatureManager by defaultManager {

    override fun isActive(feature: Feature?): Boolean {
        return resolvedFeatureManager().isActive(feature)
    }

    private fun resolvedFeatureManager(): FeatureManager {
        return when (val alternativeFeaturesSetName = alternativeFeaturesSetName()) {
            null -> defaultManager
            else -> allFeatureManagers.getValue(alternativeFeaturesSetName)
        }
    }

    private fun alternativeFeaturesSetName(): String? = valueFromCookie(currentHttpRequest())

    internal val allFeatureManagers: Map<String, FeatureManager> by lazy {
        allFeatureSetFiles().associateBy(
                { it.name },
                { newFeatureManager(it) }
        ).withDefault { allFeaturesOff }
    }

    private val allFeaturesOff =
            FeatureManagerBuilder().featureEnum(Features::class.java).build()

    internal fun newFeatureManager(file: File): FeatureManager {
        return FeatureManagerBuilder()
                .featureEnum(Features::class.java)
                .stateRepository(FileBasedStateRepository(file, SLOW_RELOAD))
                .build()
    }

    private fun allFeatureSetFiles(): Array<File> {
        val dir = File(servletContext.getRealPath("/WEB-INF/conf/"))
        val fileFilter: FilenameFilter = WildcardFileFilter("*.features.properties")

        return dir.listFiles(fileFilter) ?: emptyArray()
    }

    internal fun valueFromCookie(httpServletRequest: HttpServletRequest?): String? {
        return httpServletRequest
                ?.cookies
                ?.firstOrNull { it.name == COOKIE_NAME }
                ?.value
    }

    internal fun currentHttpRequest(): HttpServletRequest? {
        return when (val requestAttributes = getRequestAttributes()) {
            is ServletRequestAttributes -> requestAttributes.request
            else -> null
        }
    }

}
