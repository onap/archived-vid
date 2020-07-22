package vid.automation.test.infra;

import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    FLAG_ADD_MSO_TESTAPI_FIELD,
    FLAG_SERVICE_MODEL_CACHE,
    FLAG_NETWORK_TO_ASYNC_INSTANTIATION,
    FLAG_SHOW_ASSIGNMENTS,
    FLAG_UNASSIGN_SERVICE,
    FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS,
    FLAG_SHOW_VERIFY_SERVICE,
    FLAG_RESTRICTED_SELECT,
    FLAG_5G_IN_NEW_INSTANTIATION_UI,
    FLAG_ASYNC_ALACARTE_VNF,
    FLAG_A_LA_CARTE_AUDIT_INFO,
    FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS,
    FLAG_ASYNC_ALACARTE_VFMODULE,
    FLAG_SUPPLEMENTARY_FILE,
    FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI,
    FLAG_ENABLE_WEBPACK_MODERN_UI,
    FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY,
    FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST,
    FLAG_1810_AAI_LOCAL_CACHE,
    FLAG_1810_IDENTIFY_SERVICE_FOR_NEW_UI,
    FLAG_1902_NEW_VIEW_EDIT,
    FLAG_EXP_USE_DEFAULT_HOST_NAME_VERIFIER,
    FLAG_1902_VNF_GROUPING,
    FLAG_1902_RETRY_JOB,
    FLAG_VF_MODULE_RESUME_STATUS_CREATE,
    FLAG_EXP_CREATE_RESOURCES_IN_PARALLEL,
    FLAG_1906_COMPONENT_INFO,
    FLAG_1906_INSTANTIATION_API_USER_VALIDATION,
    FLAG_1906_AAI_SUB_DETAILS_REDUCE_DEPTH,
    FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI,
    FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI,
    FLAG_1908_INFRASTRUCTURE_VPN,
    FLAG_1908_RESUME_MACRO_SERVICE,
    FLAG_1908_RELEASE_TENANT_ISOLATION,
    FLAG_1908_A_LA_CARTE_VNF_NEW_INSTANTIATION_UI,
    FLAG_FLASH_REPLACE_VF_MODULE,
    FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT,
    FLAG_PNP_INSTANTIATION,
    FLAG_HANDLE_SO_WORKFLOWS,
    FLAG_CREATE_ERROR_REPORTS,
    FLAG_SHOW_ORCHESTRATION_TYPE,
    FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT,
    FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG,
    FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH,
    FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE,
    FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI,
    FLAG_2002_VNF_PLATFORM_MULTI_SELECT,
    FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS,
    FLAG_2002_IDENTIFY_INVARIANT_MACRO_UUID_BY_BACKEND,
    FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE,
    FLAG_2002_UNLIMITED_MAX,
    FLAG_2004_INSTANTIATION_TEMPLATES_POPUP,
    FLAG_2006_VFM_SDNC_PRELOAD_FILES,
    FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY,
    FLAG_2006_LIMIT_OWNING_ENTITY_SELECTION_BY_ROLES,
    FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF,
    FLAG_2006_NETWORK_PLATFORM_MULTI_SELECT,
    FLAG_EXP_USE_FORMAT_PARAMETER_FOR_CM_DASHBOARD,
    FLAG_2006_NETWORK_LOB_MULTI_SELECT,
    FLAG_2006_VNF_LOB_MULTI_SELECT,
    FLAG_2006_PORT_MIRRORING_LET_SELECTING_SOURCE_SUBSCRIBER,
    FLAG_2006_PAUSE_VFMODULE_INSTANTIATION_CREATION,
    FLAG_2008_DISABLE_DRAG_FOR_BASE_MODULE,
    FLAG_2008_CREATE_VFMODULE_INSTANTIATION_ORDER_NUMBER,
    FLAG_2008_PAUSE_INSTANTIATION_ON_VFMODULE_POPUP,
    FLAG_2008_REMOVE_PAUSE_INSTANTIATION,
    ;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
