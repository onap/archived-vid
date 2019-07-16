package org.onap.vid.services

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.StringUtils.substringAfterLast
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.portalsdk.core.util.SystemProperties
import org.onap.vid.model.VersionAndFeatures
import org.onap.vid.utils.JACKSON_OBJECT_MAPPER
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import javax.inject.Inject
import javax.servlet.ServletContext

@Service
class VersionService
@Inject
constructor(internal var servletContext: ServletContext) {

    private val Logger = EELFLoggerDelegate.getLogger(VersionService::class.java)

    private val versionAndFeatures: VersionAndFeatures by lazy {
        loadVersionAndFeatures()
    }

    private fun readBuildNumber(): String {
        val resource = servletContext.getResource("/app/vid/scripts/constants/version.json")
        return JACKSON_OBJECT_MAPPER.readValue<Map<String, String>>(resource)["Version"]!!
    }

    //protected so can be easily tested in UT
    protected fun readFeatureSet(): String {
        return SystemProperties.getProperty("features.set.filename")
    }

    //protected so can be easily tested in UT
    protected fun buildDisplayVersion(features: String, build: String): String {
        val matcher = Pattern.compile("([^/]+?)(\\.features|$)").matcher(features)
        val majorByFeatures = if (matcher.find()) matcher.group(1) else features

        val buildByVersion = StringUtils.defaultIfBlank(substringAfterLast(build, "."), build)

        return StringUtils.join(majorByFeatures, ".", buildByVersion)
    }

    fun retrieveVersionAndFeatures(): VersionAndFeatures {
        return try {
            versionAndFeatures
        } catch (exception: Exception) {
            Logger.error("Failed to read build number or feature properties settings from version file", exception)
            VersionAndFeatures.unknown
        }
    }

    private fun loadVersionAndFeatures(): VersionAndFeatures {
        val featureSet = readFeatureSet();
        val build = readBuildNumber();
        val displayVersion = buildDisplayVersion(featureSet, build)
        return VersionAndFeatures(featureSet, build, displayVersion)
    }

    //might throw an exception
    fun retrieveBuildNumber(): String {
        return versionAndFeatures.build
    }
}
