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

"use strict";

appDS2.factory("FIELD", ["PARAMETER", function (PARAMETER) {

    /*
     * ID values are typically used internally.
     */
    var ID = {
        AVAILABLE_VOLUME_GROUP: "availableVolumeGroup",
        INSTANCE_NAME: "instanceName",
        PNF_ID: "pnfId",
        LCP_REGION: "lcpRegion",
        LCP_REGION_TEXT: "lcpRegionText",
        PRODUCT_FAMILY: "productFamily",
        AIC_ZONES: "aic_zone",
        SERVICE_TYPE: "serviceType",
        SERVICE_ROLE: "serviceRole",
        SUBSCRIBER_NAME: "subscriberName",
        SUPPRESS_ROLLBACK: "suppressRollback",
        TENANT: "tenant",
        PROJECT: "project",
        OWNING_ENTITY : "owningEntity",
        LINE_OF_BUSINESS : "lineOfBusiness",
        PLATFORM : "platform",
        VNF_TARGETPROVSTATUS: "target",

        AAI_GET_FULL_SUBSCRIBERS: "aai_get_full_subscribers",
        AAI_REFRESH_FULL_SUBSCRIBERS: "aai_refresh_full_subscribers",
        AAI_GET_SERVICES: "aai_get_services",
        AAI_GET_SUBSCRIBERS: "aai_get_subscribers",
        AAI_GET_TENTANTS: "aai_get_tenants",
        AAI_REFRESH_SUBSCRIBERS: "aai_refresh_subscribers",
        AAI_SUB_DETAILS: "aai_sub_details",
        AAI_SUB_VIEWEDIT: "aai_sub_viewedit",
        ANGULAR_UI_TREE_COLLAPSEALL: "angular-ui-tree:collapse-all",
        ANGULAR_UI_TREE_EXPANDALL: "angular-ui-tree:expand-all",
        CATEGORY: "category",
        COLOR_8F8: "#8F8",
        COLOR_F88: "#F88",
        COLOR_NONE: "none",
        CUSTOMER: "customer",
        CUSTOMIZATION_UUID: "customizationUuid",
        DESCRIPTION: "description",
        GENERIC_VNF: "generic-vnf",
        GLOBAL_CUSTOMER_ID: "global-customer-id",
        GLOBAL_CUST_ID: "globalCustomerId",
        IN_MAINT: "in-maint",
        INVENTORY_RESPONSE_ITEMS: "inventory-response-items",
        INVENTORY_RESPONSE_ITEM: "inventory-response-item",
        L3_NETWORK: "l3-network",
        SUB_NET: "subnet",
        SUBNET_NAME: "subnet-name",
        SUBNET_ID: "subnet-id",
        GATEWAY_ADDRESS: "gateway-address",
        NETWORK_START_ADDRESS: "network-start-address",
        CIDR_MASK: "cidr-mask",
        MODEL_CUSTOMIZATION_ID: "model-customization-id",
        MODEL_CUSTOMIZATION_NAME: "modelCustomizationName",
        MODEL_INVARIANT_ID: "modelInvariantId",
        MODEL_INVAR_ID: "model-invariant-id",
        MODEL_NAME: "modelName",
        MODEL_NAME_VERSION_ID: "modelNameVersionId",
        MODEL_VERSION: "modelVersion",
        MODEL_VERSION_ID: "model-version-id",
        NETWORK_NAME: "network-name",
        NETWORK_ID: "network-id",
        NETWORK_TYPE: "network-type",
        NETWORKS: "networks",
        OPERATIONAL_STATUS: "operational-status",
        ORCHESTRATION_STATUS: "orchestration-status",
        PERCENT_PROGRESS: "percent-progress",
        PERSONA_MODEL_ID: "persona-model-id",
        PERSONA_MODEL_VERSION: "persona-model-version",
        PERSONA_MODEL_CUSTOMIZATION_ID: "persona-model-customization-id",
        PROV_STATUS: "prov-status",
        REQUEST: "request",
        REQUEST_ID: "requestId",
        REQUEST_LIST: "requestList",
        REQUEST_TYPE: "requestType",
        REQUEST_REFERENCES: "requestReferences",
        REQUEST_STATE: "requestState",
        REQUEST_STATUS: "requestStatus",
        RESOURCE_LINK: "resource-link",
        RESULT_DATA: "result-data",
        SERVICE_DESCRIPTION: "service-description",
        SERVICE_ID: "service-id",
        SERVICE_INSTANCE: "service-instance",
        SERVICE_INSTANCES: "service-instances",
        SERVICE_INSTANCE_ID: "service-instance-id",
        SERVICE_INSTANCE_NAME: "service-instance-name",
        SERVICE_SUBSCRIPTION: "service-subscription",
        SERVICE_SUBSCRIPTIONS: "service-subscriptions",
        SERVICETYPE: "service-type",
        STATUS_MESSAGE: "statusMessage",
        SUBNAME: "subscriber-name",
        IS_PERMITTED: "is-permitted",
        TIMESTAMP: "timestamp",
        VF_MODULE: "vf-module",
        VF_MODULES: "vfModules",
        VF_MODULE_ID: "vf-module-id",
        VF_MODULE_NAME: "vf-module-name",
        VF_MODULE_MODEL_CUSTOMIZATION_ID: "vf-module-model-customization-id",
        VF_MODULE_MODEL_VERSION_ID: "vf-module-model-version-id",
        VID: "VID",
        VNF_ID: "vnf-id",
        VNF_NAME: "vnf-name",
        VNF_TYPE: "vnf-type",
        VNFS: "vnfs",
        AVAILABLEVOLUMEGROUPS: "availableVolumeGroups",
        VOLUMEGROUPS: "volumeGroups",
        VOLUME_GROUP: "volume-group",
        VOLUME_GROUP_ID: "volume-group-id",
        VOLUME_GROUP_NAME: "volume-group-name",
        SDN_C_PRELOAD: "sdncPreload",
        UPLOAD_SUPPLEMENTORY_DATA_FILE: "uploadSupplementoryDataFile",
        SUPPLEMENTORY_DATA_FILE: "supplementoryDataFile",
        ZONE_ID: "zone-id",
        ZONE_NAME: "zone-name",
        GENERIC_CONFIGURATION: "configuration",
        CONFIGURATIONS: "configurations",
        CONFIGURATION: "configuration",
        CONFIGURATION_NAME: "configuration-name",
        CONFIGURATION_TYPE: "configuration-type",
        CONFIGURATION_ID: "configuration-id",
        PORT_ID: "interfaceId",
        PORT_NAME: "interfaceName",
        PORT_MIRRORED: "isPortMirrored"
    };

    var KEY = {
        LCP_REGION_TEXT: "AAIAIC25"
    };

    /*
     * NAME values are displayed on GUI pages.
     */
    var NAME = {
    AVAILABLE_VOLUME_GROUP: "Available Volume Group",
    INSTANCE_NAME: "Instance Name",
    PNF_ID: "PNF (Correlation) ID",
    CUSTOMER_ID: "Customer ID",
    LCP_REGION: "LCP Region",
    LCP_REGION_TEXT: "Legacy Region",
    MODEL_INVARIANT_UUID: "Model Invariant UUID",
    MODEL_NAME: "Model Name",
    MODEL_VERSION: "Model Version",
    MODEL_UUID: "Model UUID",
    MODEL_CUSTOMIZATION_UUID: "Model Customization UUID",
    MODEL_VNF_TYPE: "NF Type",
    MODEL_VNF_ROLE: "NF Role",
    MODEL_VNF_FUNCTION: "NF Function",
    MODEL_VNF_CODE: "NF Naming Code",
    MODEL_CUSTOMIZATION_NAME: "Resource Name",
    PRODUCT_FAMILY: "Product Family",
    AIC_ZONES: "AIC Zone",
	RESOURCE_DESCRIPTION : "Resource Description",
	RESOURCE_NAME : "Resource Name",
	SERVICE_CATEGORY : "Service Category",
	SERVICE_DESCRIPTION : "Service Description",
	SERVICE_INSTANCE_ID : "Service Instance ID",
	SERVICE_INSTANCE_Id : "Service Instance Id",
	SERVICE_INSTANCE_NAME : "Service Instance Name",
	SERVICE_INVARIANT_UUID : "Service Invariant UUID",
	SERVICE_NAME : "Service Name",
	SERVICE_TYPE : "Service Type",
    SERVICE_ROLE: "Service Role",
	SERVICE_UUID : "Service UUID",
	SERVICE_VERSION : "Service Version",
	SUBSCRIBER_NAME : "Subscriber Name",
	EMANUEL :  "Emanuel",
	SUPPRESS_ROLLBACK : "Suppress Rollback on Failure",
	SDN_C_PRELOAD : "SDN-C Pre-Load",
	UPLOAD_SUPPLEMENTORY_DATA_FILE : "Upload Supplementary Data file",
	SUPPLEMENTORY_DATA_FILE : "Supplementory Data file (JSON format)",
	TENANT : "Tenant",
    PROJECT : "Project",
    OWNING_ENTITY : "Owning Entity",
    LINE_OF_BUSINESS : "Line Of Business",
    PLATFORM : "Platform",
	USER_SERVICE_INSTANCE_NAME : "User Service Instance Name",
	VF_MODULE_DESCRIPTION : "VF Module Description",
	VF_MODULE_LABEL : "VF Module Label",
	VF_MODULE_TYPE : "VF Module Type",
	VNF_ORCHESTRATION_STATUS : "Orchestration Status",
	VNF_Operational_Status: "Operational Status",
	VNF_Current_Prov_Status: "Current Prov_Status",
	VNF_Target_Prov_Status: "Target Prov Status",
	VNF_VNF_ID : "VNF ID",
	VNF_VNF_Name: "VNF Name",
	VNF_VNF_Type: "VNF Type",
	VNF_Service_ID: "Service ID",
	VNF_In_Maint: "In Maint",
	VFMDULE_CUSTOMIZATIONUUID: "VF Module Model Customization UUID",
	RESOURCE_CUSTOMIZATION_UUID: "Resource Model Customization UUID"
    };

    /*
     * PROMPT values are initial values displayed in select lists.
     */
    var PROMPT = {
        AVAILABLE_VOLUME_GROUP: "Select Volume Group",
        DEFAULT_A: "A default",
        DEFAULT_B: "B default",
        LCP_REGION: "Select LCP Region",
        NO_SERVICE_INSTANCE: "No Service Instance Found",
        NO_SERVICE_SUB: "No Service Subscription Found",
        PRODUCT_FAMILY: "Select Product Family",
        AIC_ZONES: "Select AIC Zone",
        REGION: "Please choose a region",
        SERVICE_TYPE: "Select Service Type",
        SUBSCRIBER_NAME: "Select Subscriber Name",
        TARGETPROVSTATUS: "Select Target Prov Status",
        TENANT: "Select Tenant Name",
        PROJECT: "Select Project Name",
        OWNING_ENTITY: "Select Owning Entity",
        LINE_OF_BUSINESS : "Select Line Of Business",
        PLATFORM : "Select Platform",
        TEXT_INPUT: "Enter data",
        SELECT_SERVICE: "Select a service type",
        SELECT_SUB: "Select a subscriber name",
        FETCHING_SUBS: " Fetching subscriber list from A&AI",
        REFRESH_SUB_LIST: "Refreshing subscriber list from A&AI...",
        VAR_DESCRIPTION_A: "This variable is 'a'",
        VAR_DESCRIPTION_B: "This variable is 'b'",

    };

    var STATUS = {
        // Status
        ALL: "ALL",
        COMPLETE: "Complete",
        DONE: "Done",
        ERROR: "Error",
        FAILED: "Failed",
        FAILED_SERVICE_MODELS_ASDC: "Failed to get service models from SDC.",
        FETCHING_SERVICE_TYPES: "Fetching service types list from A&AI",
        FETCHING_SERVICE_CATALOG: "Fetching service catalog from AAI.  Please wait.",
        FETCHING_SERVICE_CATALOG_ASDC: "Fetching service catalog from SDC.  Please wait.",
        FETCHING_SUB_DETAILS: "Fetching subscriber details from A&AI for ",
        FETCHING_SERVICE_INST_DATA: "Fetching service instance data from A&AI for service-instance-id=",
        FETCHING_SUBSCRIBER_LIST_AAI: "Fetching subscriber list from A&AI...",
        IN_PROGRESS: "In Progress",
        IS_SUCCESSFUL: " isSuccessful: ",
        MSO_FAILURE: "msoFailure",
        NONE: "None",
        NOT_FOUND: "Not Found",
        NO_SERVICE_SUBSCRIPTION_FOUND: "No Service Subscription Found",
        SUBMITTING_REQUEST: "Submitting Request",
        SUCCESS_VNF_PROV_STATUS: "Successfully set the VNF's Prov_Status to ",
        UNLOCKED: "Unlocked",
        AAI_ACTIVE: "Active",
        AAI_INACTIVE: "Inactive",
        AAI_CREATED: "Created",
        AAI_DELETE: "Deleted",
        AAI_ENABLED: "Enabled",
        AAI_DISABLED: "Disabled",
        ASSIGNED: "Assigned"
    };

    var STYLE = {
        TABLE: "width: auto; margin: 0 auto; border-collapse: collapse; border: none;",
        NAME: "width: 220px; text-align: left; vertical-align: middle; font-weight: bold; padding: 3px 5px; border: none;",
        VALUE: "width: 400px; text-align: left; vertical-align: middle; padding: 3px 5px; border: none;",
        CHECKBOX_VALUE: "width: 400px; text-align: center; vertical-align: middle; padding: 3px 5px; border: none;",
        TEXT_INPUT: "height: 25px; padding: 2px 5px;",
        CHECKBOX_INPUT: "height: 18px; width: 18px; padding: 2px 5px;",
        SELECT: "height: 25px; padding: 2px; text-align: center;",
        REQUIRED_LABEL: "width: 25px; padding: 5px 10px 10px 5px;",

        DISABLED: "disabled",
        BTN_INACTIVE: "button--inactive",
        BTN_PRIMARY: "button--primary",
        BTN_TYPE: "btn-type",
        MSO_CTRL_BTN: "div[ng-controller=msoCommitController] button",
        PRIMARY: "primary",
        PROGRESS_BAR_INFO: "progress-bar progress-bar-info",
        PROGRESS_BAR_SUCCESS: "progress-bar progress-bar-success",
    };

    var ERROR = {
        AAI: "A&AI failure - see log below for details",
        AAI_ERROR: "A&AI Error",
        AAI_FETCHING_CUST_DATA: "Failed to fetch customer data from A&AI: Response Code: ",
        FETCHING_SERVICE_TYPES: "Failed to fetch service types from A&AI: Response Code: ",
        FETCHING_SERVICES: "Failed to fetch services from A&AI: Response Code: ",
        FETCHING_SERVICE_INSTANCE_DATA: "Failed to fetch service instance data from A&AI: Response Code: ",
        INVALID_INSTANCE_NAME: "Invalid instance name: ",
        INSTANCE_NAME_VALIDATE: "The instance name must contain only alphanumeric or \"_-.\" characters, and must start with an alphabetic character",
        INVALID_LIST: "Invalid list parameter: ",
        INVALID_MAP: "Invalid map parameter: ",
        LIST_VALIDATE: "A list parameter value must have the following syntax: '[<value1>,\.\.\.,<valueN>]'",
        MAP_VALIDATE: "A map parameter value must have the following syntax: '{ <entry_key_1>: <entry_value_1>, \.\.\., <entry_key_n>: <entry_value_n> }'",
        MAX_POLLS_EXCEEDED: "Maximum number of poll attempts exceeded",
        MISSING_DATA: "Missing data",
        MODEL_VERSION_ID_MISSING: "Error: model-version-id is not populated in A&AI",
        MSO: "MSO failure - see log below for details",
        NO_MATCHING_MODEL: "No matching model found matching the persona Model Id = ",
        NO_MATCHING_MODEL_AAI: "No matching model found matching the A&AI model version ID = ",
        SELECT: "Please select a subscriber or enter a service instance",
        SERVICE_INST_DNE: "That service instance does not exist.  Please try again.",
        SYSTEM_FAILURE: "System failure",
        INVALID_DATA_FORMAT: 'Invalid data format.Please check your file content whether it is not in json or not.',
        MISSING_FILE: 'Please Select JSON File.',

    };

    /*
     * PARAMETER values indicate field configurations that are provided to
     * parameter block directives.
     */

    var PARAMETER = {
        AVAILABLE_VOLUME_GROUP: {
            name: NAME.AVAILABLE_VOLUME_GROUP,
            id: ID.AVAILABLE_VOLUME_GROUP,
            type: PARAMETER.SELECT,
            prompt: PROMPT.AVAILABLE_VOLUME_GROUP,
            isRequired: true
        },
        INSTANCE_NAME: {
            name: NAME.INSTANCE_NAME,
            id: ID.INSTANCE_NAME,
            isRequired: true
        },
        PNF_ID: {
            name: NAME.PNF_ID,
            id: ID.PNF_ID,
            isRequired: false
        },
        LCP_REGION: {
            name: NAME.LCP_REGION,
            id: ID.LCP_REGION,
            type: PARAMETER.SELECT,
            prompt: PROMPT.LCP_REGION,
            isRequired: true
        },
        VNF_TARGET_PROVSTATUS: {
            name: NAME.VNF_Target_Prov_Status,
            id: ID.VNF_TARGETPROVSTATUS,
            type: PARAMETER.SELECT,
            prompt: PROMPT.TARGETPROVSTATUS,
            isRequired: true
        },
        LCP_REGION_TEXT_HIDDEN: {
            id: ID.LCP_REGION_TEXT,
            isVisible: false
        },
        LCP_REGION_TEXT_VISIBLE: {
            name: NAME.LCP_REGION_TEXT,
            id: ID.LCP_REGION_TEXT,
            isRequired: true,
            isVisible: true
        },
        PRODUCT_FAMILY: {
            name: NAME.PRODUCT_FAMILY,
            id: ID.PRODUCT_FAMILY,
            type: PARAMETER.SELECT,
            prompt: PROMPT.PRODUCT_FAMILY,
            isRequired: true
        },
        AIC_ZONES: {
            name: NAME.AIC_ZONES,
            id: ID.AIC_ZONES,
            type: PARAMETER.SELECT,
            prompt: PROMPT.AIC_ZONES,
            isRequired: false
        },
        SERVICE_TYPE: {
            name: NAME.SERVICE_TYPE,
            id: ID.SERVICE_TYPE,
            type: PARAMETER.SELECT,
            prompt: PROMPT.SERVICE_TYPE,
            isRequired: true
        },
        SERVICE_TYPE_DISABLED: {
            name: NAME.SERVICE_TYPE,
            id: ID.SERVICE_TYPE,
            type: PARAMETER.SELECT,
            isEnabled: false,
            isRequired: true
        },
        SUPPRESS_ROLLBACK: {
            name: NAME.SUPPRESS_ROLLBACK,
            id: ID.SUPPRESS_ROLLBACK,
            type: PARAMETER.BOOLEAN,
            value: false
        },
        SDN_C_PRELOAD_CHECKED: {
            name: NAME.SDN_C_PRELOAD,
            id: ID.SDN_C_PRELOAD,
            type: PARAMETER.CHECKBOX,
            value: true,
            isVisible: true,
            isRequired: false
        },
        SDN_C_PRELOAD_UNCHECKED: {
            name: NAME.SDN_C_PRELOAD,
            id: ID.SDN_C_PRELOAD,
            type: PARAMETER.CHECKBOX,
            value: false,
            isVisible: true,
            isRequired: false
        },
        UPLOAD_SUPPLEMENTORY_DATA_FILE_CHECKED: {
            name: NAME.UPLOAD_SUPPLEMENTORY_DATA_FILE,
            id: ID.UPLOAD_SUPPLEMENTORY_DATA_FILE,
            type: PARAMETER.CHECKBOX,
            value: true,
            isVisible: true,
            isRequired: false
        },
        UPLOAD_SUPPLEMENTORY_DATA_FILE_UNCHECKED: {
            name: NAME.UPLOAD_SUPPLEMENTORY_DATA_FILE,
            id: ID.UPLOAD_SUPPLEMENTORY_DATA_FILE,
            type: PARAMETER.CHECKBOX,
            value: false,
            isVisible: true,
            isRequired: false
        },
        SUPPLEMENTORY_DATA_FILE: {
            name: NAME.SUPPLEMENTORY_DATA_FILE,
            id: ID.SUPPLEMENTORY_DATA_FILE,
            type: PARAMETER.FILE,
            isRequired: false,
            isVisible: true,
            fileData: ''
        },

        SUBSCRIBER_NAME: {
            name: NAME.SUBSCRIBER_NAME,
            id: ID.SUBSCRIBER_NAME,
            type: PARAMETER.SELECT,
            prompt: PROMPT.SUBSCRIBER_NAME,
            isRequired: true,
			isSingleOptionAutoSelected : false
        },
        TENANT_DISABLED: {
            name: NAME.TENANT,
            id: ID.TENANT,
            type: PARAMETER.SELECT,
            isEnabled: false,
            isRequired: true
        },
        TENANT_ENABLED: {
            name: NAME.TENANT,
            id: ID.TENANT,
            type: PARAMETER.SELECT,
            isEnabled: true,
            prompt: PROMPT.TENANT,
            isRequired: true
        },
        PROJECT: {
            name: NAME.PROJECT,
            id: ID.PROJECT,
            type: PARAMETER.SELECT,
            isEnabled: true,
            prompt: PROMPT.PROJECT,
            isRequired: false
        },
        OWNING_ENTITY: {
            name: NAME.OWNING_ENTITY,
            id: ID.OWNING_ENTITY,
            type: PARAMETER.SELECT,
            isEnabled: true,
            prompt: PROMPT.OWNING_ENTITY,
            isRequired: true
        },
        LINE_OF_BUSINESS: {
            name: NAME.LINE_OF_BUSINESS,
            id: ID.LINE_OF_BUSINESS,
            type: PARAMETER.SELECT,
            isEnabled: true,
            prompt: PROMPT.LINE_OF_BUSINESS,
            isRequired: false
        },
        PLATFORM: {
            name: NAME.PLATFORM,
            id: ID.PLATFORM,
            type: PARAMETER.SELECT,
            isEnabled: true,
            prompt: PROMPT.PLATFORM,
            isRequired: true
        }
    };

    return {
        ID: ID,
        KEY: KEY,
        NAME: NAME,
        PARAMETER: PARAMETER,
        PROMPT: PROMPT,
        STATUS: STATUS,
        STYLE: STYLE,
        ERROR: ERROR
    };
}]);
