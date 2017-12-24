package vid.automation.test;

public class Constants {
    public static final String SERVICE_NAME = "Service Name";
    public static final String SERVICE_UUID = "Service UUID";
    public static final String SERVICE_INVARIANT_UUID = "Service Invariant UUID";
    public static final String SERVICE_VERSION = "Service Version";
    public static final String SERVICE_DESCRIPTION = "Service Description";
    public static final String SERVICE_CATEGORY = "Service Category";
    public static final String SUBSCRIBER_NAME = "Subscriber Name";
    public static final String SERVICE_TYPE = "Service Type";
    public static final String SERVICE_ROLE = "Service Role";
    public static final int generalTimeout = 20;
    public static final int generalRetries = 30;
    public static final String generalSubmitButtonId = "submit";
    public static final String generalCancelButtonId = "cancel";
    public static final String generalCloseModalButtonClass = "modal-close";
    public static final String generalModalTitleClass = "modal-title";
    public static final String DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE = "Dropdown permitted options are not according to user permissions.";
    public static final String CONFIRM_BUTTON_TESTS_ID = "confirmButton";
    public static final String CLOSE_BUTTON_TEXT = "Close";
    public static final String SUBMIT_BUTTON_TEXT = "Submit";
    public static final String SERVICE_TYPE_SELECT_TESTS_ID = "serviceType";
    public static final String BROWSE_SEARCH = "browseFilter";
    public static final String SUBSCRIBER_NAME_SELECT_TESTS_ID = "subscriberName";
    public static final String SUPPRESS_ROLLBACK_SELECT_TESTS_ID = "suppressRollback";
    public static final String INSTANCE_NAME_SELECT_TESTS_ID = "instanceName";
    public static final String DEPLOY_BUTTON_TESTS_ID_PREFIX = "deploy-";
    public static final String VIEW_BUTTON_TEXT = "View";
    public static final String VIEW_EDIT_BUTTON_TEXT = "View/Edit";
    public static final String VIEW_EDIT_TEST_ID_PREFIX = "view/edit-test-data-id-";
    public static final String INSTANCE_ID_FOR_NAME_TEST_ID_PREFIX = "instance-id-for-name-";
    public static final String MULTI_SELECT_UNSELECTED_CLASS = "item-unselected";
    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";


    public static final String PNF_SERVICE_TYPE = "pnf";

    public class SideMenu {
        public static final int numOfButtons = 7;
        public static final String buttonClass = "att-accordion__group";
        public static final String SEARCH_EXISTING_SERVICE = "Search for Existing Service Instances";
        public static final String BROWSE_ASDC_SERVICE_MODELS = "Browse ASDC Service Models";
        public static final String CREATE_NEW_SERVICE = "Create New Service Instance";
        public static final String VNF_CHANGES = "VNF Changes";
        public static final String TEST_ENVIRONMENTS = "Test Environments";
    }

    public class ChangeManagement {
        //general constants:
        public static final String pageHeadlineId = "change-management-headline";

        //headline bar constants:
        public static final String headlineNewButtonId = "change-management-new-button";
        public static final String headlineSchedulerButtonId = "change-management-scheduler-button";
        public static final String headlineSearchInputId = "change-management-search";

        //new change management modal constants:
        public static final String newModalSubscriberInputId = "subscriber";
        public static final String newModalServiceTypeInputId = "serviceType";
        public static final String newModalVNFTypeInputId = "vnfType";
        public static final String newModalFromVNFVersionInputId = "fromVNFVersion";
        public static final String newModalVNFNameInputId = "vnfName";
        public static final String newModalWorkFlowInputId = "workflow";
        public static final String newModalTargetVersionInputsClass = "vnf-versions-select-as-text";


        //Scheduler

        public static final String schedulerModalRangeLabel = "Range";
        public static final String schedulerModalNowLabel = "Now";
        public static final String schedulerModalStartDateInputId = "startDate";
        public static final String schedulerModalEndDateInputId = "endDate";
        public static final String schedulerModalNextMonthButtonClass = "adp-next";
        public static final String schedulerModalScheduleButtonText = "Schedule";
        public static final String schedulerModalTimeUnitSelectId = "timeUnitSelect";
        public static final String schedulerModalPolicySelectId = "policy";
        public static final String schedulerModalDurationInputTestId = "durationInput";
        public static final String schedulerModalFallbackInputTestId = "fallbackInput";
        public static final String schedulerModalConcurrencyLimitInputTestId = "concurrencyLimitInput";
        public static final String schedulerModalHoursOption = "hours";

        //dashboard constants:
        public static final String dashboardActiveTabId = "active-tab";
        public static final String dashboardFinishedTabId = "finished-tab";
        public static final String dashboardActiveTableId = "active-table";
        public static final String dashboardFinishedTableId = "finished-table";
        public static final String dashboardInProgressTheadId = "in-progress-table-head";
        public static final String dashboardPendingTheadId = "pending-table-head";
        public static final String dashboardFinishedTheadId = "finished-table-head";
        public static final String failedIconClass = "icon-x";
        public static final String processIconClass = "icon-process";
        public static final String alertIconClass = "icon-alert";
        public static final String pendingIconClass = "icon-pending";
        public static final String viewIconClass = "icon-view";
        public static final String pendingTableId = "pending-table";
        public static final String cancelPendingButtonClass = "cancel-action";

        //failed change management modal constants:
        public static final String failedModalHeaderId = "failed-modal-header";
        public static final String failedModalContentId = "failed-modal-content";
        public static final String failedModalRetryButtonId = "failed-retry-button";
        public static final String failedModalRollbackButtonId = "failed-rollback-button";

        //in progress change management modal constants:
        public static final String inProgressModalHeaderId = "in-progress-modal-header";
        public static final String inProgressModalContentId = "in-progress-modal-content";
        public static final String inProgressModalStopButtonId = "in-progress-stop-button";
        public static final String inProgressModalRollbackButtonId = "in-progress-rollback-button";

        //alert change management modal constants:
        public static final String alertModalHeaderId = "alert-modal-header";
        public static final String alertModalContentId = "alert-modal-content";
        public static final String alertModalContinueButtonId = "alert-continue-button";
        public static final String alertModalRollbackButtonId = "alert-rollback-button";

        //pending change management modal constants:
        public static final String pendingModalHeaderId = "pending-modal-header";
        public static final String pendingModalContentId = "pending-modal-content";
        public static final String pendingModalRescheduleButtonId = "pending-reschedule-button";
        public static final String pendingModalRollbackButtonId = "pending-rollback-button";
        public static final String pendingModalCancelWorkflowButtonClass = "btn-cancel-workflow";
    }

    public class TestEnvironments {
        // general constants:
        public static final String PAGE_HEADLINE = "test-environments-headline";

        // headline bar constants:
        public static final String HEADLINE_NEW_BUTTON = "test-environments-new-button";
        public static final String HEADLINE_SEARCH_INPUT = "test-environments-search";
        public static final String SEARCH_INPUT = "test-search-input";
        public static final String TEXT_TO_FILTER = "4eb";

        public static final String REFRESH_BUTTON = "test-environments-refresh";
        public static final String NO_DATA_MESSAGE = "test-environments-no-data";
        public static final String ERROR_MESSAGE = "test-environments-error";
        public static final String TRY_AGAIN_BUTTON = "try-again";
        public static final String ENVIRONMENTS_TABLE = "test-environments-table";
        public static final String TABLE_HEADER_ASC = "tablesorter-headerAsc";

        // new popup
        public static final String NEW_ENVIRONMENT_FORM = "test-new-environment-form";
        public static final String INSTANCE_NAME_INPUT = "environment-name";
        public static final String ECOMP_ID_DROP_DOWN = "ecomp-instance-id";
        public static final String ECOMP_NAME_INPUT = "ecomp-instance-name";
        public static final String TENANT_CONTEXT_INPUT = "tenant-context";
        public static final String ENVIRONMENT_TYPE_DROP_DOWN = "operational-environment-type";
        public static final String WORKLOAD_CONTEXT_DROP_DOWN = "workload-context";
        public static final String SUBMIT_BUTTON = "submit-button";
        public static final String CANCEL_BUTTON = "cancel-button";
        public static final String MODAL_CLOSE_BUTTON_CLASS = "modal-close";
        public static final String POPUP_ERROR_MESSAGE = "test-new-environment-error";
        public static final String environmentCreatedSuccesfullyMessage = "Operational Environment successfully created";

        // json content
        public static final String HEADERS_LIST = "headers";
        public static final String BASIC_LIST = "basicList";
        public static final String FULL_LIST = "fullList";
        public static final String FILTERED_LIST = "filteredList";
        public static final String SORTED_LIST = "sortedList";

        // activate
        public static final String environmentActivatedSuccesfullyMessage = "Operational Environment successfully activated";
        public static final String environmentDeactivatedSuccesfullyMessage = "Operational Environment successfully deactivated";
        public static final String activateButtonIdPrefix = "testEnvActivate-";
        public static final String deactivateButtonIdPrefix = "testEnvDeactivate-";
        public static final String environmentStatusIdPrefix = "testEnvStatus-";
        public static final String attachButtonIdPrefix = "testEnvAttach-";
    }

    public class Modals {
        public static final String modalClass = "div[modal-animation='true']";
    }

    public class BrowseASDC {
        public static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss-SSS";
        public static final String SERVICE_INSTANCE_NAME_PREFIX = "vid-e2e-test_service_";
        public static final String DEPOLY_SERVICE_CLASS = "deploy-service-class";
        public static final String SERVICE_INSTANCE_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - Service Instance was created successfully.";
        public static final String SERVICE_INSTANCE_CREATION_FAILED_MESSAGE = "failed to create service instance";
        public static final String CREATE_SERVICE_INSTANCE = "Create Service Instance";
        public static final String MSO_COMMIT_DIALOG_CLOSE_BUTTON = "msoCommitDialogCloseButton";
        public static final String AIC_OPTION_CLASS = "aic_zoneOption";
    }

    public class EditExistingInstance {
        public static final String SELECT_SERVICE_INSTANCE = "Select a Service Instance";
        public static final String SERVICE_INSTANCE_ID = "Service Instance Id";
        public static final String SERVICE_INSTANCE_NAME = "Service Instance Name";
        public static final String SEARCH_FOR_EXISTING_INSTANCES_INPUT = "searchForExistingInstancesText";
        public static final String SELECT_OWNING_ENTITY_ID = "selectOwningEntity";
        public static final String SELECT_PROJECT_ID = "selectProject";

        public static final String DELETE_VNF_BTN = "delete-vnf-btn";
        public static final String ADD_VNF_MODULE_DROPDOWN = "add-vnf-module-dropdown";
        public static final String ADD_CUSTOM_VNF_MODULE_DROPDOWN = "add-custom-vnf-module-dropdown";
        public static final String ADD_CUSTOM_VOLUME_GROUP = "add-custom-volume-group";
        public static final String ADD_VOLUME_GROUP = "add-volume-group";
        public static final String DELETE_VF_MODULE = "delete-vf-module";
        public static final String DELETE_VNF_VOLUME_GROUP = "delete-vnf-volume-group";
        public static final String DELETE_NETWORK = "delete-network";
    }

    public class CreateNewInstance {
        public static final String SUBSCRIBER_NAME_OPTION_CLASS = "subscriberNameOption";
        public static final String SERVICE_TYPE_OPTION_CLASS = "serviceTypeOption";
        public static final String SELECTED_SUBSCRIBER_NAME_TEST_ID = "selectedSubscriberName";
        public static final String SELECTED_SERVICE_TYPE_NAME_TEST_ID = "selectedServiceTypeName";
        public static final String SERVICE_INSTANCE_NAME_PREFIX = "vid-e2e-test_aai_service_";
    }

    public class ViewEdit {
        public static final String VNF_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - Vnf has been created successfully.";
        public static final String VOLUME_GROUP_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - Volume Group has been created successfully.";
        public static final String VF_MODULE_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - VF Module has been created successfully.";
        public static final String VNF_CREATION_FAILED_MESSAGE = "failed to create service instance VNF";
        public static final String VOLUME_GROUP_CREATION_FAILED_MESSAGE = "failed to create Volume Group";
        public static final String VF_MODULE_CREATION_FAILED_MESSAGE = "failed to create VF Module";
        public static final String PRODUCT_FAMILY_SELECT_TESTS_ID = "productFamily";
        public static final String LCP_REGION_SELECT_TESTS_ID = "lcpRegion";
        public static final String LEGACY_REGION_INPUT_TESTS_ID = "lcpRegionText";
        public static final String TENANT_SELECT_TESTS_ID = "tenant";
        public static final String TENANT_OPTION_CLASS = "tenantOption";
        public static final String VNF_INSTANCE_NAME_PREFIX = "vid-e2e-test_vnf_";
        public static final String AIC_ZONE_TEST_ID = "aic_zone";
        public static final String VOLUME_GROUP_OPTION_TEST_ID_PREFIX = "addVolumeGroupOption-";
        public static final String VF_MODULE_OPTION_TEST_ID_PREFIX = "addVFModuleOption-";
        public static final String VNF_OPTION_TEST_ID_PREFIX = "addVNFOption-";
        public static final String VOLUME_GROUP_INSTANCE_NAME_PREFIX = "vid-e2e-test-volume_group_";
        public static final String VF_MODULE_INSTANCE_NAME_PREFIX = "vid-e2e-test-volume_vf_module_";
        public static final String ADD_VNF_BUTTON_TEST_ID = "addVNFButton";
        public static final String ADD_VOLUME_GROUP_BUTTON_TEST_ID = "addVolumeGroupButton";
        public static final String ADD_VF_MODULE_GROUP_BUTTON_TEST_ID = "addVFModuleButton";
        public static final String DELETE_VNF_BUTTON_TEST_ID = "deleteVNFButton";
        public static final String DELETE_VF_MODULE_BUTTON_TEST_ID = "deleteVFModuleButton";
        public static final String DELETE_VNF_VOLUME_GROUP_BUTTON_TEST_ID = "deleteVNFVolumeGroupButton";
        public static final String ACTIVATE_BUTTON_TEST_ID = "activateButton";
        public static final String OPTION_IN_DROPDOWN_NOT_EXISTS = "the option %s in dropdown %s not exists";
        public static final String DISSOCIATE_BTN_CLASS = "dissociate-pnf";
        public static final String DISSOCIATE_CONFIRM_MODAL_TEXT = "Are you sure you would like to dissociate %s from the service instance?";
        public static final String DEACTIVATE_BUTTON_TEST_ID = "deactivateButton";
        public static final String ENABLE_ERROR_MESSAGE = "The %s option should not be enabled";
        public static final String DISABLE_ERROR_MESSAGE = "The %s option should be enabled";
        public static final String DISSOCIATE_CONFIRM_MODAL_BTN_ID = "ok-button";

    }
    public class PnfAssociation {
        public static final String PNF_NAME_TEST_ID = "pnfName";
        public static final String SEARCH_PNF_TEST_ID = "searchPnf";
        public static final String ASSOCIATE_PNF_TEST_ID = "associatePnf";
        public static final String PNF_ENABLE_ASSOCIATE_ERROR_MESSAGE = "The Associate option not enabled";
        public static final String PNF_DISABLE_ASSOCIATE_ERROR_MESSAGE = "The Associate option enabled when pnf is not found";
        public static final String PNF_ASSOCIATED_SUCCESSFULLY_TEXT = "COMPLETE - PNF has been associated successfully.";
        public static final String PNF_ASSOCIATED_FAILED_MESSAGE = "failed to associate PNF to service instance";
        public static final String NOT_FOUND_ERROR_TEST_ID = "pnfNotFoundErrorMessage";
        public static final String NOT_FOUND_ERROR_MESSAGE = "Errorn with the error messge not found PNF";

        public static final String MSO_MODAL_TEST_ID = "msoPopup";
        public static final String MSO_MODAL_STATUS_TEST_ID = "msoRequestStatus";
        //pnf properties
        public static final String PNF_INSTANCE_NAME_TEST_ID = "pnfInstancePnfName";
        public static final String PNF_INSTANCE_NAME2_TEST_ID = "pnfInstancePnfName2";
        public static final String PNF_INSTANCE_NAME2_SOURCE_TEST_ID = "pnfInstancePnfName2Source";
        public static final String PNF_INSTANCE_ID_TEST_ID = "pnfInstancePnfId";
        public static final String PNF_INSTANCE_EQUIP_TYPE_TEST_ID = "pnfInstanceEquipType";
        public static final String PNF_INSTANCE_EQUIP_VENDOR_TEST_ID = "pnfInstanceEquipVendor";
        public static final String PNF_INSTANCE_EQUIP_MODEL_TEST_ID = "pnfInstanceEquipModel";
        public static final String PNF_INSTANCE_ERROR_MESSAGE = "The PNF Instance info %s does not match";

    }
    public class serviceModelInfo{
        public static final String INFO_TEST_ID_PREFIX = "info-test-data-id-";
        public static final String SERVIICE_NAME_KEY = "ServiceName";
        public static final String SUBSCRIBER_NAME_KEY = "SubscriberName";
        public static final String SERVICE_INSTANCE_NAME = "ServiceInstanceName";
        public static final String MODEL_NAME = "ModelName";
        public static final String MODEL_INVARIANT_UUID = "ModelInvariantUUID";
        public static final String MODEL_VERSION = "ModelVersion";
        public static final String MODEL_UUID = "ModelUUID";
        public static final String MODEL_CUSTOMIZATION_UUID = "ModelCustomizationUUID";
        public static final String RESOURCE_NAME = "ResourceName";
        public static final String NF_TYPE = "NFType";
        public static final String NF_ROLE = "NFRole";
        public static final String NF_FUNCTION = "NFFunction";
        public static final String NF_NAMING_CODE = "NFNamingCode";
        public static final String METADETA_ERROR_MESSAGE = "The service model info %s does not match";
    }

    public class OwningEntity {
        public static final String PROJECT_SELECT_TEST_ID = "project";
        public static final String OWNING_ENTITY_SELECT_TEST_ID = "owningEntity";
        public static final String PLATFORM_SELECT_TEST_ID = "platform";
        public static final String LOB_SELECT_TEST_ID = "lineOfBusiness";
    }

    public class Users {
        public static final String READONLY = "readonly";
        public static final String USP_VOICE_VIRTUAL_USP = "uspVoiceVirtualUsp";
        public static final String MOBILITY_MOBILITY = "mobilityMobility";
        public static final String SUPRE_USER = "su";
    }

    public class RegisterToSimulator {

        //separated simulated json files according to pages
        public class SearchForServiceInstance {
            public static final String GET_FULL_SUBSCRIBERS = "search_for_service_instance/aai_get_full_subscribers.json";
            public static final String GET_SERVICES = "search_for_service_instance/aai_get_services.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID = "search_for_service_instance/aai_filter_service_instance_by_id.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID_2 = "search_for_service_instance/aai_filter_service_instance_by_id_2.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER = "search_for_service_instance/aai_get_subscribers_for_customer.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_2 = "search_for_service_instance/aai_get_subscribers_for_customer_2.json";
            public static final String NAMED_QUERY_VIEW_EDIT = "search_for_service_instance/aai_named_query_for_view_edit.json";
            public static final String NAMED_QUERY_VIEW_EDIT_2 = "search_for_service_instance/aai_named_query_for_view_edit_2.json";
        }

        public class pProbe {
            public static final String GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS = "pProbe/aai_get_service_instance_with_logical_links.json";
            public static final String GET_LOGICAL_LINK = "pProbe/aai_get_logical_link.json";
            public static final String GET_SPECIFIC_PNF = "pProbe/aai_get_specific_pnf.json";
            public static final String GET_SPECIFIC_PNF_ERROR = "pProbe/aai_get_specific_pnf_error.json";
            public static final String ADD_PNF_RELATIONSHIP = "pProbe/mso_add_pnf_relationship.json";
            public static final String ADD_PNF_RELATIONSHIP_ERROR = "pProbe/mso_add_pnf_relationship_error.json";
            public static final String GET_ADD_PNF_RELATIONSHIP_ORCH_REQ = "pProbe/mso_get_add_relationship_orch_req.json";
            public static final String REMOVE_PNF_RELATIONSHIP = "pProbe/mso_remove_pnf_relationship.json";
            public static final String GET_REMOVE_PNF_RELATIONSHIP_ORCH_REQ = "pProbe/mso_get_remove_relationship_orch_req.json";
            public static final String REMOVE_PNF_RELATIONSHIP_ERROR = "pProbe/mso_remove_pnf_relationship_error.json";
        }

        public class activateDeactivate{
            public static final String AAI_GET_SERVICE_INSTANCE = "activateDeactivate/aai_get_service_instance.json";
            public static final String ACTIVATE_SERVICE_INSTANCE = "activateDeactivate/mso_activate_service_instance.json";
            public static final String ACTIVATE_SERVICE_INSTANCE_ERROR = "activateDeactivate/mso_activate_service_instance_error.json";
            public static final String ACTIVATE_SERVICE_INSTANCE_ORCH_REQUEST = "activateDeactivate/mso_activate_service_instance_orch_req.json";
        }

        public class genericRequest{
            public static final String ECOMP_PORTAL_GET_SESSION_SLOT_CHECK_INTERVAL = "ecompportal_getSessionSlotCheckInterval.json";
        }
    }
}
