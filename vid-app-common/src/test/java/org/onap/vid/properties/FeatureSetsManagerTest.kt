package org.onap.vid.properties

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.onap.vid.testUtils.TestUtils
import org.springframework.web.context.request.RequestContextHolder
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.togglz.core.manager.FeatureManager
import javax.servlet.ServletContext
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import org.hamcrest.CoreMatchers.`is` as _is
import org.mockito.Mockito.`when` as _when

class FeatureSetsManagerTest {
    @Mock
    lateinit var defaultFeatureManager: FeatureManager
    @Mock
    lateinit var servletContext: ServletContext
    @Mock
    lateinit var alternativeFeatureSetNameProvider: AlternativeFeatureSetNameProvider
    @InjectMocks
    lateinit var featureSetsManager: FeatureSetsManager

    private val alternativeFeatureSetNameFromCookie = AlternativeFeatureSetNameFromCookie()

    @BeforeMethod
    fun setUp() {
        TestUtils.initMockitoMocks(this)
    }

    @Test
    fun `isActive - without alternative features set name - delegates to default and no file loaded`() {
        _when(defaultFeatureManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE)).thenReturn(true)
        _when(alternativeFeatureSetNameProvider.alternativeFeatureSetName).thenReturn(null)

        assertThat(featureSetsManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE), _is(true))

        verifyZeroInteractions(servletContext) // implies no other file loaded
        verify(defaultFeatureManager, times(1)).isActive(Features.FLAG_1810_AAI_LOCAL_CACHE)
    }

    @Test
    fun `isActive - with alternative features set - brings flags from alternative`() {
        _when(servletContext.getRealPath(anyString())).thenReturn(this.javaClass.getResource("/").path)
        _when(alternativeFeatureSetNameProvider.alternativeFeatureSetName).thenReturn("example.features.properties")

        assertThat(featureSetsManager.isActive(Features.FLAG_1810_AAI_LOCAL_CACHE), _is(true))
        assertThat(featureSetsManager.isActive(Features.FLAG_1902_NEW_VIEW_EDIT), _is(false))
        verifyZeroInteractions(defaultFeatureManager)
    }

    @Test
    fun `isActive - with non-existing alternative features set - fallback is to all flags off`() {
        _when(servletContext.getRealPath(anyString())).thenReturn(this.javaClass.getResource("/").path)
        _when(alternativeFeatureSetNameProvider.alternativeFeatureSetName).thenReturn("non-existing")

        assertThat(featureSetsManager, not(nullValue()))
        assertThat(
                featureSetsManager.features.map { featureSetsManager.isActive(it) },
                not(hasItem(true))
        )
    }

    @Test
    fun `valueFromCookie - given no request - return null`() {
        assertThat(alternativeFeatureSetNameFromCookie.valueFromCookie(null), _is(nullValue()))
    }

    @Test
    fun `valueFromCookie - given request - return the correct cookie value`() {
        val servletRequestMock = mock(HttpServletRequest::class.java)
        _when(servletRequestMock.cookies).thenReturn(arrayOf(Cookie("features.set", "value")))

        assertThat(alternativeFeatureSetNameFromCookie.valueFromCookie(servletRequestMock), _is("value"))
    }

    @Test
    fun `valueFromCookie - given request without cookies - return null`() {
        val servletRequestMock = mock(HttpServletRequest::class.java)
        _when(servletRequestMock.cookies).thenReturn(emptyArray())

        assertThat(alternativeFeatureSetNameFromCookie.valueFromCookie(servletRequestMock), _is(nullValue()))
    }

    @Test
    fun `currentHttpRequest - when no current request - return null`() {
        assertPrecondition()
        assertThat(alternativeFeatureSetNameFromCookie.currentHttpRequest(), _is(nullValue()))
    }

    private fun assertPrecondition() {
        assertThat("precondition for test not met: static RequestContextHolder.getRequestAttributes should be null",
                RequestContextHolder.getRequestAttributes(), _is(nullValue()))
    }
}
