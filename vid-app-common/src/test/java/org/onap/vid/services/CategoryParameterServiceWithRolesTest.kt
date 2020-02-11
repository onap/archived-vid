package org.onap.vid.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert.assertThat
import org.intellij.lang.annotations.Language
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.invocation.InvocationOnMock
import org.onap.vid.category.CategoryParameterOptionRep
import org.onap.vid.category.CategoryParametersResponse
import org.onap.vid.roles.RoleProvider
import org.onap.vid.roles.RoleValidator
import org.onap.vid.services.CategoryParameterServiceWithRoles.OwningEntityOptionRep
import org.onap.vid.testUtils.TestUtils
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.togglz.core.manager.FeatureManager
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito.`when` as _when

class CategoryParameterServiceWithRolesTest {

    @Mock lateinit var categoryParameterService: CategoryParameterService
    @Mock lateinit var featureManager: FeatureManager
    @Mock lateinit var roleProvider: RoleProvider
    @Mock lateinit var request: HttpServletRequest

    private lateinit var alwaysTrueRoles: RoleValidator
    private lateinit var alwaysFalseRoles: RoleValidator

    @InjectMocks
    lateinit var categoryParameterServiceWithRoles: CategoryParameterServiceWithRoles;

    @BeforeMethod
    fun initMocks() {
        TestUtils.initMockitoMocks(this)

        alwaysTrueRoles = mock(RoleValidator::class.java, Mockito.withSettings().defaultAnswer { o: InvocationOnMock? -> true })
        alwaysFalseRoles = mock(RoleValidator::class.java)
    }

    @Test
    fun `treatPermissions -- given no permissions -- owningEntity lists is empty, the rest left intact`() {
        _when(roleProvider.getUserRolesValidator(any())).thenReturn(alwaysFalseRoles)

        assertThat(
                categoryParameterServiceWithRoles.treatPermissions(categoryParametersResponse),
                allOf(
                    jsonPartEquals("categoryParameters.owningEntity", emptyList<Any>()),
                    jsonEquals<CategoryParametersResponse>(categoryParametersResponse)
                            .whenIgnoringPaths("categoryParameters.owningEntity")
                )
        )
    }

    @Test
    fun `treatPermissions -- given all permissions -- response left intact`() {
        _when(roleProvider.getUserRolesValidator(any())).thenReturn(alwaysTrueRoles)

        assertThat(
                categoryParameterServiceWithRoles.treatPermissions(categoryParametersResponse),
                jsonEquals(categoryParametersResponse)
        )
    }

    @Test
    fun `treatPermissions -- given permission to WayneHolland -- only one owningEntity WayneHolland is left`() {
        val wayneHolland = CategoryParameterOptionRep("d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland")

        val roleValidatorForWayneHolland = mock(RoleValidator::class.java)
        _when(roleValidatorForWayneHolland.isServicePermitted(OwningEntityOptionRep(wayneHolland))).thenReturn(true)

        _when(roleProvider.getUserRolesValidator(any())).thenReturn(roleValidatorForWayneHolland)

        assertThat(
                categoryParameterServiceWithRoles.treatPermissions(categoryParametersResponse),
                jsonPartEquals("categoryParameters.owningEntity", listOf(wayneHolland))
        )
    }



    @Language("JSON") val categoryParametersResponse: CategoryParametersResponse =
        jacksonObjectMapper().readValue("""
            {
              "categoryParameters": {
                "lineOfBusiness": [
                  { "id": "ONAP", "name": "ONAP" },
                  { "id": "zzz1", "name": "zzz1" }
                ],
                "owningEntity": [
                  { "id": "b1a9a80e-71b8-4176-9ac6-d265bf30e9d9", "name": "Melissa" },
                  { "id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "name": "WayneHolland" }
                ],
                "project": [
                  { "id": "WATKINS", "name": "WATKINS" },
                  { "id": "yyy1", "name": "yyy1" }
                ],
                "platform": [
                  { "id": "platform", "name": "platform" },
                  { "id": "xxx1", "name": "xxx1" }
                ]
              }
            }""")


}