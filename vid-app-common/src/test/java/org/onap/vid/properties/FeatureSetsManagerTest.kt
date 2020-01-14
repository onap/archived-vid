package org.onap.vid.properties

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyZeroInteractions
import org.onap.vid.testUtils.TestUtils
import org.springframework.web.context.request.RequestContextHolder
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.togglz.core.manager.FeatureManager
import java.nio.file.Files
import javax.servlet.ServletContext
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import org.hamcrest.CoreMatchers.`is` as _is
import org.mockito.Mockito.`when` as _when

class FeatureSetsManagerTest {
    @Mock
    val featureManager: FeatureManager? = null
    @Mock
    val servletContext: ServletContext? = null
    @InjectMocks
    val featureSetsManager: FeatureSetsManager? = null

    @BeforeMethod
    fun setUp() {
        TestUtils.initMockitoMocks(this)
    }

    @Test
    fun `isActive - without http request - delegates to default and no file loaded`() {
        assertPrecondition()
        _when(featureSetsManager!!.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE)).thenReturn(true)

        assertThat(featureSetsManager!!.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE), _is(true))
        verifyZeroInteractions(servletContext) // i.e. no file loaded
    }

    @Test
    fun `valueFromCookie - given no request - return null`() {
        assertThat(featureSetsManager!!.valueFromCookie(null), _is(nullValue()))
    }

    @Test
    fun `valueFromCookie - given request - return the correct cookie value`() {
        val servletRequestMock = mock(HttpServletRequest::class.java)
        _when(servletRequestMock.cookies).thenReturn(arrayOf(Cookie("features.set", "value")))

        assertThat(featureSetsManager!!.valueFromCookie(servletRequestMock), _is("value"))
    }

    @Test
    fun `valueFromCookie - given request without cookies - return null`() {
        val servletRequestMock = mock(HttpServletRequest::class.java)
        _when(servletRequestMock.cookies).thenReturn(emptyArray())

        assertThat(featureSetsManager!!.valueFromCookie(servletRequestMock), _is(nullValue()))
    }

    @Test
    fun `currentHttpRequest - when no current request - return null`() {
        assertPrecondition()
        assertThat(featureSetsManager!!.currentHttpRequest(), _is(nullValue()))
    }

    @Test
    fun `newFeatureManager - given file - feature set is coherent`() {
        val oneFeature = Features.FLAG_1810_AAI_LOCAL_CACHE
        val anotherFeature = Features.FLAG_1902_NEW_VIEW_EDIT

        val file = Files.createTempFile("test","features")
        Files.write(file, "${oneFeature.name}=true".toByteArray())

        val newFeatureManager = featureSetsManager!!.newFeatureManager(file.toFile())

        assertThat(newFeatureManager.isActive(oneFeature), _is(true))
        assertThat(newFeatureManager.isActive(anotherFeature), _is(false))
    }

    @Test
    fun `allFeatureSets - non-existing set name - default is all flags off`() {
        _when(servletContext!!.getRealPath(anyString())).thenReturn(this.javaClass.getResource(".").path)

        val allFeatureSets = featureSetsManager!!.allFeatureManagers
        val fallbackFeatureManager = allFeatureSets.getValue("non-existing")

        assertThat(fallbackFeatureManager, not(nullValue()))
        assertThat(
                fallbackFeatureManager.features.map { fallbackFeatureManager.isActive(it) },
                not(hasItem(true))
        )
    }

    private fun assertPrecondition() {
        assertThat("precondition for test not met: static RequestContextHolder.getRequestAttributes should be null",
                RequestContextHolder.getRequestAttributes(), _is(nullValue()))
    }
}