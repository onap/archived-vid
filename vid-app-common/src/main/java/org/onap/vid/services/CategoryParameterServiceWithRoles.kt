package org.onap.vid.services

import com.fasterxml.jackson.annotation.JsonIgnore
import org.onap.vid.category.CategoryParameterOptionRep
import org.onap.vid.category.CategoryParametersResponse
import org.onap.vid.model.CategoryParameter
import org.onap.vid.properties.Features
import org.onap.vid.roles.RoleProvider
import org.onap.vid.roles.WithPermissionPropertiesOwningEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.togglz.core.manager.FeatureManager
import javax.servlet.http.HttpServletRequest

@Service
@Qualifier("WithRoles")
class CategoryParameterServiceWithRoles(
        private val categoryParameterService: CategoryParameterService,
        private val featureManager: FeatureManager,
        private val roleProvider: RoleProvider,
        private val request: HttpServletRequest
) : CategoryParameterService by categoryParameterService {

    private val owningEntityKey = "owningEntity"

    private fun shouldTreatPermissions() = featureManager.isActive(Features.FLAG_2006_LIMIT_OWNING_ENTITY_SELECTION_BY_ROLES)

    override fun getCategoryParameters(familyName: CategoryParameter.Family?): CategoryParametersResponse {
        val categoryParameters =
                categoryParameterService.getCategoryParameters(familyName)

        return if (shouldTreatPermissions()) {
            treatPermissions(categoryParameters)
        } else {
            categoryParameters
        }
    }

    internal fun treatPermissions(categoryParametersResponse: CategoryParametersResponse): CategoryParametersResponse {
        val extractedCategoryParameters = categoryParametersResponse.categoryParameters
        val owningEntities = extractedCategoryParameters[owningEntityKey]

        return CategoryParametersResponse(
                extractedCategoryParameters + (owningEntityKey to removeNonPermitted(owningEntities)))
    }

    private fun removeNonPermitted(owningEntities: MutableList<CategoryParameterOptionRep>?): List<CategoryParameterOptionRep>? {
        val userRolesValidator = roleProvider.getUserRolesValidator(request)
        return owningEntities
                ?.map { OwningEntityOptionRep(it) }
                ?.filter { userRolesValidator.isServicePermitted(it) }
    }

    /**
     * Encapsulates a CategoryParameterOptionRep where id field  contains an owningEntityId
     */
    class OwningEntityOptionRep(option: CategoryParameterOptionRep) :
            CategoryParameterOptionRep(option.id, option.name), WithPermissionPropertiesOwningEntity {
        override val owningEntityId: String?
            @JsonIgnore get() = id
    }
}
