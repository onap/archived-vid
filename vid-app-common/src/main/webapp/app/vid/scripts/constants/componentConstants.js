/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

appDS2
    .constant("_", window._)
	.constant("COMPONENT", (function() {
    return {
    A_LA_CARTE : "a la carte",
    CLOUD_REGION_ID : "cloudRegionID",
    COMPONENT_STATUS : "ComponentStatus",
    CREATE_COMPONENT : "createComponent",
    DELETE_RESUME_COMPONENT : "deleteResumeComponent",
    DELETE:"Delete",
    RESUME:"Resume",
    ENTITY : "entity",
    GET_COMPONENT_LIST : "getComponentList",
    GET_SUBS : "getSubs",
    GET_SUB_DETAILS : "getSubDetails",
    GLOBAL_CUSTOMER_ID : "globalCustomerId",
    MACRO : "Macro",
    MODEL_NAME_IISBC : "Intercarrier Interconnect Session Border Controller",
	MODEL_NAME_VISBCOAMNETWORK : "vIsbcOamNetwork",
	MODEL_NAME_VISBCRTPEXPANSIONMODULE : "vIsbcRtpExpansionModule",
	MODEL_NAME_VISBC : "vIsbc",
	MODEL_NAME_WANBONDING : "WanBonding",
	MODEL_VERSION_1 : "1",
	MSO_CREATE_REQ : "createInstance",
	MSO_DELETE_REQ : "deleteInstance",
	MSO_CHANGE_CONFIG_STATUS_REQ: "changeConfigurationStatus",
	MSO_CHANGE_PORT_STATUS_REQ: "changePortStatus",
	MSO_CREATE_ENVIRONMENT: "createEnvironmentInstance",
	MSO_DEACTIVATE_ENVIRONMENT: "deactivateEnvironmentInstance",
	MSO_ACTIVATE_ENVIRONMENT: "activateEnvironmentInstance",
	MSO_ACTIVATE_SERVICE_REQ: "activateServiceInstance",
	MSO_DEACTIVATE_SERVICE_REQ: "deactivateServiceInstance",
    NAME : "name",
	NETWORK : "network",
	CONFIGURATION : "configuration",
	PORT: "port",
	NETWORKS : "networks",
	PRODUCT_NAME_TRINITY : "Trinity",
	QUERY_SERVICE_INSTANCE : "queryServiceInstance",
	REFRESH_PROPERTIES : "refreshProperties",
	SDN_L3_BONDING : "SDN-L3-BONDING",
	SDN_ETHERNET_INTERNET : "SDN-ETHERNET-INTERNET",
	SERVICE : "service",
    OLDVERSION : 'oldversion',
    SERVICE_TYPE : "serviceType",
	SHOW_COMPONENT_DETAILS : "showComponentDetails",
	STATUS : "status",
	SUBSCRIBER_NAME : "subscriberName",
	TENANT_ID : "tenantID",
	TENANT_NAME : "tenantName",
	TRUE : "true",
	UCPE_VMS : "uCPE-VMS",
	VF_MODULE : "vfModule",
	VNF : "vnf",
	VNF_CODE : "vnfCode",
	VNF_FUNCTION : "vnfFunction",
	VNF_ROLE : "vnfRole",
	VNF_TYPE : "vnfType",
	VOLUME_GROUP : "volumeGroup",
    IS_PERMITTED: "is-permitted",
    RESUME_STATUS: "PendingActivation",
    PNF : "pnf",
        // IDs
	CIDR_MASK_1 : "255.255.255.000",
	//COMPONENT_LIST_NAMED_QUERY_ID : "ed0a0f5b-cf79-4784-88b2-911cd726cd3d",
	CUSTOMER_ID_1 : "icore9883749",
	DELETE_INSTANCE_ID_1 : "ff305d54-75b4-ff1b-fff1-eb6b9e5460ff",
	GATEWAY_ADDRESS_1 : "10.10.125.1",
	GLOBAL_SUBSCRIBER_ID_1 : "C12345",
	INSTANCE_ID_1 : "ff305d54-75b4-431b-adb2-eb6b9e5ff000",
	INSTANCE_ID_2 : "ff305d54-75b4-ff1b-adb2-eb6b9e5460ff",
	INSTANCE_ID_3 : "ff305d54-75b4-ff1b-bdb2-eb6b9e5460ff",
	MODEL_ID_1 : "sn5256d1-5a33-55df-13ab-12abad84e764",
	MODEL_ID_2 : "ff5256d1-5a33-55df-aaaa-12abad84e7ff",
	MODEL_ID_3 : "ff3514e3-5a33-55df-13ab-12abad84e7ff",
	MODEL_ID_4 : "ff5256d1-5a33-55df-13ab-12abad84e7ff",
	MODEL_ID_5 : "ff5256d1-5a33-55df-13ab-22abad84e7ff",
	MODEL_NAME_VERSION_ID_1 : "ab6478e4-ea33-3346-ac12-ab121484a333",
	MODEL_NAME_VERSION_ID_2 : "fe6478e4-ea33-3346-aaaa-ab121484a3fe",
	MODEL_NAME_VERSION_ID_3 : "fe6985cd-ea33-3346-ac12-ab121484a3fe",
	MODEL_NAME_VERSION_ID_4 : "fe6478e4-ea33-3346-ac12-ab121484a3fe",
	MODEL_NAME_VERSION_ID_5 : "fe6478e4-ea33-3346-bc12-ab121484a3fe",
	SERVICE_INSTANCE_ID_1 : "bc305d54-75b4-431b-adb2-eb6b9e546014",
	SUBSCRIBER_NAME_GED12 : "General Electric Division 12",
	VNF_INSTANCE_ID : "VNF_INSTANCE_ID_12345",
	VPN_ID_1 : "1a2b3c4d5e6f",

	// PATHS
	GET_WORKFLOW: "change-management/get_vnf_workflow_relation",
	GET_MSO_WORKFLOWS: "change-management/mso",
	GET_SCHEDULER_CHANGE_MANAGEMENTS: "change-management/scheduler",
	CANCEL_SCHEDULE_REQUEST: "change-management/scheduler/schedules",
	ASSIGN : "?r=",
	AAI_GET_SERVICE_INSTANCE_PATH : "aai_get_service_instance/",
	AAI_GET_SERVICES : "aai_get_services",
    AAI_GET_AIC_ZONES :"aai_get_aic_zones",
    AAI_GET_AIC_ZONE_FOR_PNF :"aai_get_aic_zone_for_pnf/@globalCustomerId/@serviceType/@serviceInstanceId",
    AAI_GET_SERVICES_BY_TYPE : "aai_get_models_by_service_type",
	AAI_GET_TENANTS : "aai_get_tenants/",
	AAI_SUB_DETAILS_PATH : "aai_sub_details/",
    AAI_GET_VERSION_BY_INVARIANT_ID:"aai_get_version_by_invariant_id/",
    SEARCH_SERVICE_INSTANCES:"search_service_instances",
    AAI_GET_VNF_DATA_PATH: "aai_get_vnf_data/",
    AAI_GET_VNF_BY_CUSTOMERID_AND_SERVICETYPE: "get_vnf_data_by_globalid_and_service_type/",
    AAI_GET_SERVICES_BY_OWNING_ENTITY_ID: 'aai_get_services_by_owning_entity_id',
    AAI_SUB_VIEWEDIT_PATH : "aai_sub_viewedit",
	AAI_GET_VNF_INFO : "aai_get_vnf_information",
	AAI_GET_PNF_INSTANCE: "aai_get_service_instance_pnfs",
	AAI_GET_VNF_INSTANCES_LIST: "aai_get_vnf_instances",
	AAI_GET_PNF_INSTANCES_LIST: "aai_get_pnf_instances",
	AAI_GET_BY_URI: "aai_get_by_uri/",
	AAI_GET_CONFIGURATION: "aai_get_configuration/",
    AAI_GET_TEST_ENVIRONMENTS: "get_operational_environments?operationalEnvironmentType=",
    GET_CATEGORY_PARAMETERS : "category_parameter",
	PARAMETER_STANDARDIZATION_FAMILY: "PARAMETER_STANDARDIZATION",
    TENANT_ISOLATION_FAMILY: "TENANT_ISOLATION",
	ASDC_GETMODEL_PATH : "asdc/getModel/",
	CREATE_INSTANCE_PATH : "/models/services/createInstance", AAI_GET_PNF_BY_NAME : "aai_get_pnfs/pnf/",
	//1710 scheduler contants
	POST_CREATE_NEW_VNF_CHANGE:"post_create_new_vnf_change",
	GET_POLICY:"get_policy",
	WORKFLOW: "workflow",
	GET_TIME_SLOTS:"get_time_slots",
	SUBMIT_VNF_CHANGE_TIMESLOTS:"submit_vnf_change_timeslots",

	FORWARD_SLASH : "/",
	GET_SYSTEM_PROP_VNF_PROV_STATUS_PATH : "get_system_prop_vnf_prov_status",
	GET_USER_ID : "getuserID",
	INSTANTIATE_ROOT_PATH : "#/instantiate?subscriberId=",
	INSTANTIATE_PATH : "/instantiate",
	INVALID_STRING : "/INVALID_STRING/",
	INVALID_STRING_MSO_CREATE_SVC_INSTANCE : "INVALID_STRING_mso_create_svc_instance",
	MSO: "mso",
	MSO_CREATE_NW_INSTANCE : "mso_create_nw_instance",
	MSO_CREATE_NW_INSTANCE_PATH : "mso_create_nw_instance/",
	MSO_CREATE_SVC_INSTANCE : "mso_create_svc_instance",
	MSO_DELETE_SVC_INSTANCE_PATH : "mso_delete_svc_instance/",
    MSO_ACTIVATE_INSTANCE: "mso/mso_activate_service_instance/@serviceInstanceId",
    MSO_DEACTIVATE_INSTANCE: "mso/mso_deactivate_service_instance/@serviceInstanceId",
    MSO_CREATE_REALATIONSHIP : "mso_add_relationship",
	MSO_REMOVE_RELATIONSHIP: "mso_remove_relationship",
	SELECTED_SERVICE_SUB_PATH : "#/instances/subdetails?",
	SELECTED_SERVICE_INSTANCE_SUB_PATH : "serviceInstanceIdentifier=",
	SELECTED_SUBSCRIBER_SUB_PATH : "subscriberId=",
	OWNING_ENTITY_SUB_PATH : "owningEntity=",
	PROJECT_SUB_PATH : "project=",
	SERVICE_TYPE_LIST_PATH : "#/instances/serviceTypes?serviceTypeList=",
	SERVICE_MODLES_INSTANCES_SUBSCRIBERS_PATH : 'serviceModels.htm#/instances/subscribers',
	SERVICES_DIST_STATUS_PATH : "rest/models/services?distributionStatus=",
	SERVICES_PATH : "rest/models/services/",
	SERVICETYPE_SUB_PATH : "&serviceType=",
	SERVICEINSTANCEID_SUB_PATH : "&serviceInstanceId=",
	SERVICEMODELS_INSTANCES_SERVICES_PATH : "serviceModels.htm#/instances/services",
	SERVICEMODELS_MODELS_SERVICES_PATH : "serviceModels.htm#/models/services",
	SUBDETAILS_SELECTEDSUBSCRIBER : "#subdetails?selectedSubscriber=",
	SUBSCRIBERNAME_SUB_PATH : "&subscriberName=",
	WELCOME_PATH : "welcome.htm",
    IS_PERMITTED_SUB_PATH: "&isPermitted=",

    //Test Environment Urls:
	OPERATIONAL_ENVIRONMENT_CREATE : "operationalEnvironment/create",
	OPERATIONAL_ENVIRONMENT_DEACTIVATE : "operationalEnvironment/deactivate?operationalEnvironment=",
	OPERATIONAL_ENVIRONMENT_ACTIVATE : "operationalEnvironment/activate?operationalEnvironment=",
	OPERATIONAL_ENVIRONMENT_STATUS : "operationalEnvironment/requestStatus?requestId=",

    //Template Urls
	AAI_GET_SUBS_URL : "app/vid/scripts/view-models/aaiGetSubs.htm",
	AAI_GET_SUBSCRIBER_URL : "app/vid/scripts/view-models/aaiGetSubscriberList.htm",
	AAI_SERVICE_TYPES_URL : "app/vid/scripts/view-models/aaiServiceTypes.htm",
	AAI_SUB_DETAILS_URL : "app/vid/scripts/view-models/aaiSubDetails.htm",
	CREATE_INSTANCE_SERVICE_MODELS_URL : "app/vid/scripts/view-models/createInstanceServiceModels.htm",
	INSTANTIATE_URL : "app/vid/scripts/view-models/instantiate.htm",
	SERVICE_MODELS : "app/vid/scripts/view-models/serviceModels.htm",



	FULL_NAME_MAP : {
	    "model-invariant-id" : "Model ID",
	    "model-version-id" : "Model Version ID"
	},
	PARTIAL_NAME_MAP : {
	    "id" : "ID",
	    "uuid" : "UUID",
	    "vfmodule" : "VF Module",
	    "vnf" : "VNF",
	    "volumegroup" : "Volume Group"
	},

	MANUAL_TASKS: {
        "retry": "retry",
        "rollback": "rollback",
        "abort": "abort",
        "skip": "skip"
	},

	WORKFLOWS: {
    	vnfConfigUpdate: "VNF Config Update",
		vnfInPlace: "VNF In Place Software Update",
		update: "update",
		replace: "replace"
	}

    };
})())
