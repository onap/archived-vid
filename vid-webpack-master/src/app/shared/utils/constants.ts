export module Constants {

  export class Component {
    public static NAME = 'name';
    public static SERVICE = 'service';
    public static A_LA_CARTE = 'a la carte';
    public static MACRO = 'Macro';
    public static SUBSCRIBER_NAME = 'subscriberName';
    public static OLDVERSION = 'oldversion';
    public static SERVICE_TYPE = 'serviceType';
  }

  export class Path {
    public static FORWARD_SLASH = '/';

    public static AAI_GET_FULL_SUBSCRIBERS = '../../aai_get_full_subscribers';
    public static AAI_REFRESH_FULL_SUBSCRIBERS = 'aai_refresh_full_subscribers';
    public static AAI_GET_SUBSCRIBERS = '../../aai_get_subscribers';

    public static AAI_GET_TENTANTS = '../../aai_get_tenants';
    public static AAI_REFRESH_SUBSCRIBERS = 'aai_refresh_subscribers';
    public static AAI_SUB_DETAILS = 'aai_sub_details';
    public static AAI_SUB_VIEWEDIT = '../../aai_sub_viewedit';

    public static GET_WORKFLOW = 'change-management/get_vnf_workflow_relation';
    public static GET_MSO_WORKFLOWS = 'change-management/mso';
    public static GET_SCHEDULER_CHANGE_MANAGEMENTS = 'change-management/scheduler';
    public static CANCEL_SCHEDULE_REQUEST = 'change-management/scheduler/schedules';
    public static ASSIGN = '?r=';
    public static AAI_OMIT_SERVICE_INSTANCES = '&omitServiceInstances=';
    public static AAI_GET_SERVICE_INSTANCE_PATH = 'aai_get_service_instance/';
    public static AAI_GET_SERVICES = '../../aai_get_services';
    public static AAI_GET_AIC_ZONES = '../../aai_get_aic_zones';
    public static AAI_GET_AIC_ZONE_FOR_PNF = 'aai_get_aic_zone_for_pnf/@globalCustomerId/@serviceType/@serviceInstanceId';
    public static AAI_GET_SERVICES_BY_TYPE = 'aai_get_models_by_service_type';
    public static AAI_GET_TENANTS = '../../aai_get_tenants/';
    public static AAI_SUB_DETAILS_PATH = '../../aai_sub_details/';
    public static AAI_GET_SERVICE_INSTANCE_TOPOLOGY_PATH = '../../aai_get_service_instance_topology/';
    public static AAI_GET_ACTIVE_NETWORKS_PATH = '../../aai_get_active_networks/';
    public static AAI_GET_VPNS_PATH = '../../aai_get_vpn_list/';
    public static AAI_GET_SERVICE_GROUP_MEMBERS_PATH = '../../aai_search_group_members/';
    public static AAI_GET_USER_ID_PATH = '../../getuserID';
    public static AAI_GET_VERSION_BY_INVARIANT_ID = 'aai_get_version_by_invariant_id/';
    public static SEARCH_SERVICE_INSTANCES = 'search_service_instances';
    public static AAI_GET_VNF_BY_CUSTOMERID_AND_SERVICETYPE = 'get_vnf_data_by_globalid_and_service_type/';
    public static AAI_GET_SERVICES_BY_OWNING_ENTITY_ID = 'aai_get_services_by_owning_entity_id';
    public static AAI_GET_VNF_INFO = 'aai_get_vnf_information';
    public static AAI_GET_PNF_INSTANCE = 'aai_get_service_instance_pnfs';
    public static AAI_GET_VNF_INSTANCES_LIST = 'aai_get_vnf_instances';
    public static AAI_GET_PNF_INSTANCES_LIST = 'aai_get_pnf_instances';
    public static AAI_GET_BY_URI = 'aai_get_by_uri/';
    public static AAI_GET_CONFIGURATION = 'aai_get_configuration/';
    public static AAI_GET_TEST_ENVIRONMENTS = 'get_operational_environments?operationalEnvironmentType=';
    public static GET_CATEGORY_PARAMETERS = '../../category_parameter';
    public static PARAMETER_STANDARDIZATION_FAMILY = 'PARAMETER_STANDARDIZATION';
    public static TENANT_ISOLATION_FAMILY = 'TENANT_ISOLATION';
    public static ASDC_GETMODEL_PATH = 'asdc/getModel/';
    public static CREATE_INSTANCE_PATH = '/models/services/createInstance';
    public static AAI_GET_PNF_BY_NAME = 'aai_get_pnfs/pnf/';

    public static GET_SYSTEM_PROP_VNF_PROV_STATUS_PATH = 'get_system_prop_vnf_prov_status';
    public static GET_USER_ID = 'getuserID';
    public static GET_MENU= '../../get_menu';
    public static GET_VERSION= '../../version';
    public static GET_APP_NAME= '../../get_app_name';
    public static INSTANTIATE_ROOT_PATH = '#/instantiate?subscriberId=';
    public static INSTANTIATE_PATH = '/instantiate';
    public static INVALID_STRING = '/INVALID_STRING/';
    public static INVALID_STRING_MSO_CREATE_SVC_INSTANCE = 'INVALID_STRING_mso_create_svc_instance';
    public static MSO = 'mso';
    public static MSO_CREATE_NW_INSTANCE = 'mso_create_nw_instance';
    public static MSO_CREATE_NW_INSTANCE_PATH = 'mso_create_nw_instance/';
    public static MSO_CREATE_SVC_INSTANCE = 'mso_create_svc_instance';
    public static MSO_CREATE_VNF_INSTANCE = '../../mso/mso_create_vnf_instance/';
    public static MSO_DELETE_SVC_INSTANCE_PATH = 'mso_delete_svc_instance/';
    public static MSO_ACTIVATE_INSTANCE = 'mso/mso_activate_service_instance/@serviceInstanceId';
    public static MSO_DEACTIVATE_INSTANCE = 'mso/mso_deactivate_service_instance/@serviceInstanceId';
    public static MSO_CREATE_REALATIONSHIP = 'mso_add_relationship';
    public static MSO_REMOVE_RELATIONSHIP = 'mso_remove_relationship';
    public static SELECTED_SERVICE_SUB_PATH = '#/instances/subdetails?';
    public static SELECTED_SERVICE_INSTANCE_SUB_PATH = 'serviceInstanceIdentifier=';
    public static SELECTED_SUBSCRIBER_SUB_PATH = 'subscriberId=';
    public static OWNING_ENTITY_SUB_PATH = 'owningEntity=';
    public static PROJECT_SUB_PATH = 'project=';
    public static SERVICE_TYPE_LIST_PATH = '#/instances/serviceTypes?serviceTypeList=';
    public static SERVICE_MODLES_INSTANCES_SUBSCRIBERS_PATH = 'serviceModels.htm#/instances/subscribers';
    public static SERVICES_DIST_STATUS_PATH = '../../rest/models/services?distributionStatus=';
    public static SERVICES_PATH = '../../rest/models/services/';
    public static SERVICE_LATEST_VERSION = '../../aai_get_newest_model_version_by_invariant/';
    public static SERVICETYPE_SUB_PATH = '&serviceType=';
    public static SERVICEINSTANCEID_SUB_PATH = '&serviceInstanceId=';
    public static SERVICEMODELS_INSTANCES_SERVICES_PATH = 'serviceModels.htm#/instances/services';
    public static SERVICEMODELS_MODELS_SERVICES_PATH = 'serviceModels.htm#/models/services';
    public static SUBDETAILS_SELECTEDSUBSCRIBER = '#subdetails?selectedSubscriber=';
    public static SUBSCRIBERNAME_SUB_PATH = '&subscriberName=';
    public static WELCOME_PATH = 'welcome.htm';
    public static IS_PERMITTED_SUB_PATH = '&isPermitted=';
    public static SERVICES_JOB_INFO_PATH = '../../asyncInstantiation';
    public static INSTANTIATION_TEMPLATES_PATH = '../../instantiationTemplates';
    public static SERVICE_MODEL_ID = 'serviceModelId';
    public static SERVICES_RETRY_TOPOLOGY = '../../asyncInstantiation/bulkForRetry';
    public static INSTANTIATION_TEMPLATE_TOPOLOGY = '../../instantiationTemplates/templateTopology';
    public static PRE_LOAD = '../../preload';
    public static CONFIGURATION_PATH = '../../get_property/{name}/defaultvalue';
    public static SERVICES_JOB_AUDIT_PATH = '/auditStatus';
    public static SERVICES_PROBE_PATH = "../../probe";
    public static FEATURES_FLAG_PATH ="../../flags";
    public static AUDIT_STATUS_FOR_RETRY_PATH = '../../asyncInstantiation/auditStatusForRetry';

    // Test Environment Urls =
    public static OPERATIONAL_ENVIRONMENT_CREATE = 'operationalEnvironment/create';
    public static OPERATIONAL_ENVIRONMENT_DEACTIVATE = 'operationalEnvironment/deactivate?operationalEnvironment=';
    public static OPERATIONAL_ENVIRONMENT_ACTIVATE = 'operationalEnvironment/activate?operationalEnvironment=';
    public static OPERATIONAL_ENVIRONMENT_STATUS = 'operationalEnvironment/requestStatus?requestId=';

  }

  export class Key {

    public static DESCRIPTION = 'description';
    public static GENERIC_VNF = 'generic-vnf';
    public static GLOBAL_CUSTOMER_ID = 'global-customer-id';
    public static GLOBAL_CUST_ID = 'globalCustomerId';
    public static IN_MAINT = 'in-maint';
    public static INVENTORY_RESPONSE_ITEMS = 'inventory-response-items';
    public static INVENTORY_RESPONSE_ITEM = 'inventory-response-item';
    public static L3_NETWORK = 'l3-network';
    public static SUB_NET = 'subnet';
    public static SUBNET_NAME = 'subnet-name';
    public static SUBNET_ID = 'subnet-id';
    public static GATEWAY_ADDRESS = 'gateway-address';
    public static NETWORK_START_ADDRESS = 'network-start-address';
    public static CIDR_MASK = 'cidr-mask';
    public static MODEL_CUSTOMIZATION_ID = 'model-customization-id';
    public static MODEL_INVAR_ID = 'model-invariant-id';
    public static MODEL_VERSION_ID = 'model-version-id';
    public static NETWORK_NAME = 'instanceName';
    public static NETWORK_ID = 'instanceId';
    public static NETWORK_TYPE = 'instanceType';
    public static NETWORKS = 'networks';
    public static OPERATIONAL_STATUS = 'operational-status';
    public static ORCHESTRATION_STATUS = 'orchStatus';
    public static PERCENT_PROGRESS = 'percent-progress';
    public static PERSONA_MODEL_ID = 'persona-model-id';
    public static PERSONA_MODEL_VERSION = 'persona-model-version';
    public static PERSONA_MODEL_CUSTOMIZATION_ID = 'persona-model-customization-id';
    public static PROV_STATUS = 'prov-status';

    public static RESOURCE_LINK = 'resource-link';
    public static RESULT_DATA = 'result-data';
    public static SERVICE_DESCRIPTION = 'service-description';
    public static SERVICE_ID = 'service-id';
    public static SERVICE_INSTANCE = 'service-instance';
    public static SERVICE_INSTANCES = 'service-instances';
    public static SERVICE_INSTANCE_ID = 'service-instance-id';
    public static SERVICE_INSTANCE_NAME = 'service-instance-name';
    public static SERVICE_SUBSCRIPTION = 'service-subscription';
    public static SERVICE_SUBSCRIPTIONS = 'service-subscriptions';
    public static SERVICETYPE = 'service-type';
    public static STATUS_MESSAGE = 'statusMessage';
    public static SUBNAME = 'subscriber-name';
    public static IS_PERMITTED = 'is-permitted';
    public static TIMESTAMP = 'timestamp';
    public static VF_MODULE = 'vf-module';
    public static VF_MODULES = 'vfModules';
    public static VF_MODULE_ID = 'vf-module-id';
    public static VF_MODULE_NAME = 'vf-module-name';
    public static VID = 'VID';
    public static VNF_ID = 'vnf-id';
    public static VNF_NAME = 'vnf-name';
    public static VNF_TYPE = 'vnf-type';
    public static VNFS = 'vnfs';
    public static AVAILABLEVOLUMEGROUPS = 'availableVolumeGroups';
    public static VOLUMEGROUPS = 'volumeGroups';
    public static VOLUME_GROUP = 'volume-group';
    public static VOLUME_GROUP_ID = 'volume-group-id';
    public static VOLUME_GROUP_NAME = 'volume-group-name';
    public static UPLOAD_SUPPLEMENTORY_DATA_FILE = 'uploadSupplementoryDataFile';
    public static SUPPLEMENTORY_DATA_FILE = 'supplementoryDataFile';
    public static ZONE_ID = 'zone-id';
    public static ZONE_NAME = 'zone-name';
    public static GENERIC_CONFIGURATION = 'configuration';
    public static CONFIGURATIONS = 'configurations';
    public static CONFIGURATION = 'configuration';
    public static CONFIGURATION_NAME = 'configuration-name';
    public static CONFIGURATION_TYPE = 'configuration-type';
    public static CONFIGURATION_ID = 'configuration-id';
    public static PORT = 'l-interface';
    public static PORT_ID = 'interface-id';
    public static PORT_NAME = 'interface-name';
    public static PORT_MIRRORED = 'is-port-mirrored';
  }

  export class Status {
    public static ALL = 'ALL';
    public static COMPLETE = 'Complete';
    public static DONE = 'Done';
    public static ERROR = 'Error';
    public static FAILED = 'Failed';
    public static FAILED_SERVICE_MODELS_ASDC = 'Failed to get service models from SDC.';
    public static FETCHING_SERVICE_TYPES = 'Fetching service types list from A&AI';
    public static FETCHING_SERVICE_CATALOG = 'Fetching service catalog from AAI.  Please wait.';
    public static FETCHING_SERVICE_CATALOG_ASDC = 'Fetching service catalog from SDC.  Please wait.';
    public static FETCHING_SUB_DETAILS = 'Fetching subscriber details from A&AI for ';
    public static FETCHING_SERVICE_INST_DATA = 'Fetching service instance data from A&AI for service-instance-id=';
    public static FETCHING_SUBSCRIBER_LIST_AAI = 'Fetching subscriber list from A&AI...';
    public static IN_PROGRESS = 'In Progress';
    public static IS_SUCCESSFUL = ' isSuccessful = ';
    public static MSO_FAILURE = 'msoFailure';
    public static NONE = 'None';
    public static NOT_FOUND = 'Not Found';
    public static NO_SERVICE_SUBSCRIPTION_FOUND = 'No Service Subscription Found';
    public static SUBMITTING_REQUEST = 'Submitting Request';
    public static SUCCESS_VNF_PROV_STATUS = 'Successfully set the VNF\'s Prov_Status to ';
    public static UNLOCKED = 'Unlocked';
    public static AAI_ACTIVE = 'Active';
    public static AAI_INACTIVE = 'Inactive';
    public static AAI_CREATED = 'Created';
    public static AAI_ENABLED = 'Enabled';
    public static AAI_DISABLED = 'Disabled';
  }

  export class Error {
    public static AAI = 'A&AI failure - see log below for details';
    public static AAI_ERROR = 'A&AI Error';
    public static AAI_FETCHING_CUST_DATA = 'Failed to fetch customer data from A&AI= Response Code= ';
    public static FETCHING_SERVICE_TYPES = 'Failed to fetch service types from A&AI= Response Code= ';
    public static FETCHING_SERVICES = 'Failed to fetch services from A&AI= Response Code= ';
    public static FETCHING_SERVICE_INSTANCE_DATA = 'Failed to fetch service instance data from A&AI= Response Code= ';
    public static INVALID_INSTANCE_NAME = 'Invalid instance name= ';
    // tslint:disable-next-line:max-line-length
    public static INSTANCE_NAME_VALIDATE = 'The instance name must contain only alphanumeric or \'_-.\' characters; and must start with an alphabetic character';
    public static INVALID_LIST = 'Invalid list parameter= ';
    public static INVALID_MAP = 'Invalid map parameter= ';
    public static LIST_VALIDATE = 'A list parameter value must have the following syntax= \'[<value1>;\.\.\.;<valueN>]\'';
    // tslint:disable-next-line:max-line-length
    public static MAP_VALIDATE = 'A map parameter value must have the following syntax= \'{ <entry_key_1>= <entry_value_1>; \.\.\.; <entry_key_n>= <entry_value_n> }\'';
    public static MAX_POLLS_EXCEEDED = 'Maximum number of poll attempts exceeded';
    public static MISSING_DATA = 'Missing data';
    public static MODEL_VERSION_ID_MISSING = 'Error= model-version-id is not populated in A&AI';
    public static MSO = 'MSO failure - see log below for details';
    public static NO_MATCHING_MODEL = 'No matching model found matching the persona Model Id = ';
    public static NO_MATCHING_MODEL_AAI = 'No matching model found matching the A&AI model version ID = ';
    public static SELECT = 'Please select a subscriber or enter a service instance';
    public static SERVICE_INST_DNE = 'That service instance does not exist.  Please try again.';
    public static SYSTEM_FAILURE = 'System failure';
    public static INVALID_DATA_FORMAT = 'Invalid data format.Please check your file content whether it is not in json or not.';
    public static MISSING_FILE = 'Please Select JSON File.';

    public static MISSING_VNF_DETAILS = 'Missing required information.\n' +
      'Please open and fill in the details.\n';
  }

  export class EventType {
    public static COMPONENT_STATUS = 'ComponentStatus';
    public static CREATE_COMPONENT = 'createComponent';
    public static DELETE_RESUME_COMPONENT = 'deleteResumeComponent';
  }

  export class ServicePopup {
    public static TITLE = 'Create a new service instance';
    public static TOOLTIP_UUID = 'Unique identifier for this service in this version.';
    public static TOOLTIP_INVARIANT_UUID = 'Unique identifier for this service cross versions.';

  }

  export class Parameter {
    public static BOOLEAN = "boolean";
    public static SELECT = "select";
    public static MULTI_SELECT = "multi_select";
    public static STRING = "string";
    public static NUMBER = "number";
    public static VALID_VALUES = "valid_values";
    public static EQUAL = "equal";
    public static LENGTH = "length";
    public static MAX_LENGTH = "max_length";
    public static MIN_LENGTH = "min_length";
    public static IN_RANGE = "in_range";
    public static CONSTRAINTS = "constraints";
    public static OPERATOR = "operator";
    public static CONSTRAINT_VALUES = "constraintValues";
    public static DEFAULT = "default";
    public static DESCRIPTION = "description";
    public static TYPE = "type";
    public static INTEGER = "integer";
    public static RANGE = "range";
    public static LIST = "list";
    public static MAP = "map";
    public static REQUIRED = "required";
    public static GREATER_THAN = "greater_than";
    public static LESS_THAN = "less_than";
    public static GREATER_OR_EQUAL = "greater_or_equal";
    public static LESS_OR_EQUAL = "less_or_equal";
    public static DISPLAY_NAME = "displayName";
    public static CHECKBOX ='checkbox';
    public static FILE ='file';
  }

  export class AuditInfoModal{
    public static TITLE = 'Service Instantiation Information';
  }

  export class LegacyRegion {
    public static MEGA_REGION = ['AAIAIC25'];
  }

  export class ModelInfo {
    public static UNLIMITED_DEFAULT = 'Unlimited (default)';
  }
}
