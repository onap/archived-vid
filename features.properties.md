
### Feature Flags

* FLAG_ADD_MSO_TESTAPI_FIELD

  As MSO are trying a macro/a-la-carte API consolidation, this feature will signal
  MSO whether to use the old API or the new one.
  If enabled, VID will add the field "testApi" (with a selectable value) to many 
  requests' payloads.

* FLAG_NETWORK_TO_ASYNC_INSTANTIATION

  If FLAG_NETWORK_TO_ASYNC_INSTANTIATION is enabled - services that contain networks will also use the new macro instantiation flow
  as describes under: FLAG_ASYNC_INSTANTIATION
  Combination of FLAG_ASYNC_INSTANTIATION- enabled and FLAG_NETWORK_TO_ASYNC_INSTANTIATION- disabled - may break tests
  that contain csars with networks and expected to go to new flow

* FLAG_5G_IN_NEW_INSTANTIATION_UI

  Enable deployment of 5G a-la-carte services in the "new" Angular 2 instantiation pages.
  If disabled, the deploy process will be in old UI. 

* FLAG_SHOW_ASSIGNMENTS

* FLAG_ASYNC_ALACARTE_VNF

  enable creating vnfs via async instantiation of a-la-carte service

* FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS

    
* FLAG_A_LA_CARTE_AUDIT_INFO

  This flag enable show a-la-carte mso audit info, online from mso


* FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS

  Once a Provider Network is instantiated and the user goes to View / Edit, she will
  be able to view the Tenant Networks associated with a Provider network.
  
  This information about the networks is retrieved from A&AI.
  
  If flag disabled, A&AI is not approached, and View / Edit shows no underlying
  VLANs.
  
* FLAG_ASYNC_ALACARTE_VFMODULE

  Enable creating vfModules and volume groups via async instantiation of a-la-carte 
  service. If turned off, only VNFs will be created; vf modules will be ignored. 
  
* FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI
  
  Experimental flag that route any a-la-carte service deployment to "new" Angular 2 instantiation pages.
  This flag is currently only for development propose and shall not be enabled in testing/production.
  
* FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST

  When this flag is is on, VID is adding cloudOwner field into CloudConfiguration section of MSO requests.
  Relevant requests for 1810 :
  
    * Create VNF
    * Create Volume Group
    * Create VfModule
    * Create Network  
    * Create Macro service
    * Delete VNF
    * Delete Volume Group
    * Delete VfModule
    * Delete Network  
    * Delete Macro service

 
* FLAG_1810_AAI_LOCAL_CACHE

  Enables the caching of selected AAI responses.
  
  
* FLAG_1810_IDENTIFY_SERVICE_FOR_NEW_UI
  
  Enables to identify the service for new UI.
  
  
* FLAG_EXP_USE_DEFAULT_HOST_NAME_VERIFIER

  Enables using DefaultHostnameVerifier in HttpAuthClient in order to enable a more secure connection
 
 
* FLAG_1902_NEW_VIEW_EDIT

  Enable users to go to new view service instance page that is based on service planning page.
  If the feature flag is on, once a use click open a service on instantiation status dashboard,
  the user is redirected to the new view page.
  
  
* FLAG_1902_VNF_GROUPING

  Support services with vnf grouping. If the flag is enabled, clicking on deploy of service with vnf grouping 
  would open the new UI of deploy service. Also if the flag is enabled, view/edit of such a service is service planning 
  new UI in view/edit mode.
  
* FLAG_1902_RETRY_JOB
  Support retry of failed job. Once async job has failed, the user is able to retry execute the job again.
    
* FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY

  While creating a port-mirroring configuration, user will be able to choose the service-type for
  both pnf anv vnf (aka pprobe and vprobe).
  
  Disable this flag to go back to original behaviour, that pnf defaults to the service's service-
  type, without a visible queue nor an option to change.
  
* FLAG_EXP_CREATE_RESOURCES_IN_PARALLEL

  Enable the user to create resources (like VNF, NETWORK, VF_MODULE) in parallel during ALaCarte.
  For 1902 version the flag is false, since SDNC doesn't support creation of resources in parallel
  for ALaCarte scenarios.
  
* FLAG_1906_COMPONENT_INFO
  
  Show in drawing board an information for each resource, when the resource is selected in 
  the drawing board tree
  
* FLAG_1906_INSTANTIATION_API_USER_VALIDATION
  
  Enable user role validation for the Backend API instantiation request. The validation is applied for subscriber
  and service type. There is no tenants validation.
  
* FLAG_1906_AAI_SUB_DETAILS_REDUCE_DEPTH
  
  Enable using the depth=1 instead of depth=2 parameter in outgoing A&AI GET 'business/customers/customer/{subscriberId}' 
  request when the Frontend sends the GET '/aai_sub_details/{subscriberId}' request with additional parameter 
  'omitServiceInstances=true' to the Backend. 
  
  Relevant for these specific cases:
  1) Service types fetching on the "Create New Service Instance" page after subscriber choosing.
  2) Service types fetching on Service Instance creation popup from the "Browse SDC models" page.
  
* FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI
  Enable opening transport service (service with type:TRANSPORT) in new instantiation UI. 
  
* FLAG_1908_INFRASTRUCTURE_VPN
  Enable opening VRF service (service with type:BONDING, role: INFRASTRUCTURE-VPN) in new instantiation UI.

* FLAG_1908_RESUME_MACRO_SERVICE
  Enable resume macro service from new view edit page, if :
  * Service model has a Macro deployment 
  * Service Instance is in Assigned / Inventoried Ocrh. Status
  * Service instance Service Type != Transport (PNFs)
  
* FLAG_1908_VNF_FABRIC_CONFIGURATION_NEW_INSTANTIATION_UI
  Enable open the  "new" Angular 2 instantiation pages for service with service-role = "VNF"
  
* FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT
  Adds a button in legacy View/Edit screen that reopens the service in the _new_ View/Edit screen.
  This button is not displayed when no "Edit" permissions. 

* FLAG_FLASH_REPLACE_VF_MODULE
  Enable Replace VF module for upgrade flows, requested by the Flash team.
  When upgrading a VF module VID will invoke the MSO POST VF-module/replace request
  
* FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT
  Enable New UI on View Edit for Macro, NON TRANSPORT services 
  
* FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH
  Add cloud-region and nf-role as an optional search criteria fields when fetching VNF details.

* FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE
  Let the user set the order of resource instantiation while using drawing board (new view edit)
  for a-la-carte instantiation.

* FLAG_SHOW_ORCHESTRATION_TYPE
  enables showing/hiding (true/false) column with orchestration type in Service Model browser.
  The types are fetched from aai.
  
   
* FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI
  Any a-la-carte new service deployment will be open in modern UI, besides excluded services : Port mirroring, VLAN Tagging

* FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS
  When flag is true the new popup will be opened with additional options to perform on VFM.
  
* FLAG_2002_IDENTIFY_INVARIANT_MACRO_UUID_BY_BACKEND,
  When flag is true, VID use macro_services_by_invariant_uuid.json file to identify if csar without instantiation type is macro service.  
  Otherwise, MACRO_SERVICES list in vidConfiguration.js is used to identify if it's a macro service (in ng1 code)

* FLAG_2002_VNF_PLATFORM_MULTI_SELECT
  When flag is true the platform will appear as a multi select field, if false the platform will be dropdown list.
  
* FLAG_2004_INSTANTIATION_STATUS_FILTER
  When flag is true the user can filter rows in instantiation status by using text input
  
* FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE
  When flag is true, the user see in instantiation status page, an option to recreate another instance.
  This option is enabled only for instantiate jobs.
  If the user clicks on this option, the previous instantiation is opened in drawing board, 
  and the user can create another one from this template.
  
* FLAG_2004_INSTANTIATION_TEMPLATES_POPUP
  Enables a designated Templates selection modal, accessible when creating a new instance through "Browse SDC".

* FLAG_2006_VFM_SDNC_PRELOAD_FILES
  Enables upload files when SDNC preload checkbox is checked

* FLAG_2002_UNLIMITED_MAX 
  when flag is true and max_instances is not declare than user can add unlimited VND, NETWORK, VFMODULE,
  User can duplicate up to 10 record in single time.
  If the flag is false and max_instances is not declare the max will be 1 else max_instances value.

* FLAG_MORE_AUDIT_INFO_LINK_ON_AUDIT_INFO
  On the "audit info" modal (available on Instantiation Status page), shows a link navigating to
  the read-only RETRY page with more audit info.
  
* FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY 
  When flag is true the user will be provided with edit permissions by owning entity id even the user have no permission by Subscriber,
  when the flag is false the user provided with edit permission by Subscriber.
  
* FLAG_2006_LIMIT_OWNING_ENTITY_SELECTION_BY_ROLES
  While service instantiation, when flag is enabled, a user will not be able to choose Owning Entity which she has no 
  matching role for. Relevant roles can be provided by using FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY.
  
* FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF
  When flag is true new VF Modules on Alacarte service will inherit LCP-Region and Tenant from parent VNF.
  When off, user is requested to specify LCP-Region and Tenant for each VF Module.
  
* FLAG_2006_NETWORK_PLATFORM_MULTI_SELECT
  When flag is true the platform will appear as a multi select field, if false the platform will be limited to a single value.
  
* FLAG_2006_NETWORK_LOB_MULTI_SELECT
  When flag is true the LOB will appear as a multi select field, if false the Line Of Business will be limited to a single value.
 
 * FLAG_2006_VNF_LOB_MULTI_SELECT
   When flag is true the LOB will appear as a multi select field, if false the Line Of Business  will be limited to a single value.
  
* FLAG_EXP_USE_FORMAT_PARAMETER_FOR_CM_DASHBOARD
  When flag is true VID will use the format=simpleNoTaskInfo parameter in addition to the filter parameter when fetching orchestration requests for the change-management dashboard.
  When OFF, VID will use only the filter parameter