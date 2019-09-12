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

package org.onap.vid.properties;

import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    /*******************************
     * Use /docs/feature-flags.md for details
     */

    CREATE_INSTANCE_TEST,
    EMPTY_DRAWING_BOARD_TEST,
    FLAG_ADD_MSO_TESTAPI_FIELD,
    FLAG_SERVICE_MODEL_CACHE,
    FLAG_NETWORK_TO_ASYNC_INSTANTIATION,
    FLAG_SHOW_ASSIGNMENTS,
    FLAG_UNASSIGN_SERVICE,
    FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS,
    FLAG_SHOW_VERIFY_SERVICE, // AKA POMBA
    FLAG_DEFAULT_VNF,
    FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD,
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
    FLAG_1810_CR_SOFT_DELETE_ALACARTE_VF_MODULE,
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
    FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT,
    FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH,
    ;


    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}
