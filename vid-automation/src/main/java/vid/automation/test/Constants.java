package vid.automation.test;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String PREVIOUS_VERSION = "Previous Versions";
    public static final String BROWS_SDC_SERVICE_MODELS = "Browse SDC Service Models";
    public static final String SERVICE_NAME = "Service Name";
    public static final String SERVICE_INSTANCE_NAME = "Service Instance Name";
    public static final String SERVICE_UUID = "Service UUID";
    public static final String SERVICE_INVARIANT_UUID = "Service Invariant UUID";
    public static final String SERVICE_VERSION = "Service Version";
    public static final String SERVICE_DESCRIPTION = "Service Description";
    public static final String SERVICE_CATEGORY = "Service Category";
    public static final String SUBSCRIBER_NAME = "Subscriber Name";
    public static final String SERVICE_TYPE = "Service Type";
    public static final String SERVICE_ROLE = "Service Role";
    public static final String RESOURCE_NAME = "Resource Name 1";
    public static final String RESOURCE_DESCRIPTION = "Resource Description 1";
    public static final int generalTimeout = 20;
    public static final int generalRetries = 30;
    public static final String generalSubmitButtonId = "submit";
    public static final String generalCancelButtonId = "cancel";
    public static final String generalCloseModalButtonClass = "modal-close";
    public static final String generalModalTitleClass = "modal-title";
    public static final String DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE = "Dropdown permitted options are not according to user permissions.";
    public static final String CONFIRM_BUTTON_TESTS_ID = "confirmButton";
    public static final String CONFIRM_RESUME_DELETE_TESTS_ID = "confirmResumeDeleteButton";
    public static final String SOFT_DELETE_TESTS_ID = "softDeleteButton";
    public static final String CLOSE_BUTTON_TEXT = "Close";
    public static final String CANCEL_BUTTON_TEST_ID = "cancelButton";
    public static final String COMMIT_CLOSE_BUTTON_ID = "msoCommitDialogCloseButton";
    public static final String SUBMIT_BUTTON_TEXT = "Submit";
    public static final String SERVICE_TYPE_SELECT_TESTS_ID = "serviceType";
    public static final String BROWSE_SEARCH = "browseFilter";
    public static final String BROWSE_RESULTS_TABLE = "browseResultsTable";
    public static final String SUBSCRIBER_NAME_SELECT_TESTS_ID = "subscriberName";
    public static final String SUPPRESS_ROLLBACK_SELECT_TESTS_ID = "suppressRollback";
    public static final String INSTANCE_NAME_SELECT_TESTS_ID = "instanceName";
    public static final String CREATE_MODAL_TITLE_ID = "create-modal-title";
    public static final String DEPLOY_BUTTON_TESTS_ID_PREFIX = "deploy-";
    public static final String VIEW_BUTTON_TEXT = "View";
    public static final String VIEW_EDIT_BUTTON_TEXT = "View/Edit";
    public static final String VIEW_EDIT_TEST_ID_PREFIX = "view/edit-test-data-id-";
    public static final String INSTANCE_ID_FOR_NAME_TEST_ID_PREFIX = "instance-id-for-name-";
    public static final String SERVICE_NAME_TEST_ID_PREFIX = "service-name-test-data-id-";
    public static final String SUBSCRIBER_NAME_TEST_ID_PREFIX = "subscriber-name-test-data-id-";
    public static final String FILTER_SUBSCRIBER_DETAILS_ID = "filter-subscriber-details";
    public static final String SERVICE_INSTANCEID_TH_ID = "service-instanceId-th-id";
    public static final String MULTI_SELECT_UNSELECTED_CLASS = "item-unselected";
    public static final String MULTI_SELECT_SELECTED_CLASS = "item-selected";
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String MSO_COMMIT_DIALOG_CLOSE_BUTTON = "msoCommitDialogCloseButton";
    public static final String REQUIRED = "is required";
    public static final String MISSING_DATA = "Missing data (\"%s\")";
    public static final String ANGULAR2_TESTS = "Angular2Tests";
    public static final String UNDER_DEVELOPMENT = "underDevelopment";
    public static final String DELETE_CONFIGURATION_BUTTON = "deleteConfigurationButton";
    public static final String ACTIVATE_DEACTIVATE_BUTTON = "activateDeactivateButton";
    public static final String ENABLE_DISABLE_BUTTON = "enableDisableButton";
    public static final String SUBSCRIBER_SELECT_ID= "subscriber-name-select";

    public static final String PNF_SERVICE_TYPE = "pnf";
    public static final String GR_API = "GR_API";

    public class bugFixes{
        public static final String HEADER_CONTAINER = "headerContainer";
    }

    public class SideMenu {
        public static final int numOfButtons = 7;
        public static final String buttonClass = "att-accordion__group";
        public static final String SEARCH_EXISTING_SERVICE = "Search for Existing Service Instances";
        public static final String BROWSE_SDC_SERVICE_MODELS = "Browse SDC Service Models";
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

        public static final String refreshBtnTestId = "refresh-cm";

        //new change management modal constants:
        public static final String newModalSubscriberInputId = "subscriber";
        public static final String newModalServiceTypeInputId = "serviceType";
        public static final String newModalVNFTypeInputId = "vnfType";
        public static final String newModalVNFTypeInputId1 = "vnfTypeInput";
        public static final String newModalVNFCloudRegion = "cloudRegion";
        public static final String newModalVNFSearchVNF = "searchVNF";
        public static final String newModalFromVNFVersionInputId = "fromVNFVersion";
        public static final String newModalVNFNameInputId = "vnfName";
        public static final String newModalWorkFlowInputId = "workflow";
        public static final String newModalConfigUpdateInputId = "internal-workflow-parameter-file-5-attach-configuration-file";
        public static final String newModalTargetVersionInputsClass = "vnf-versions-select-as-text";
        public static final String newModalSubscriberText = "CRAIG/ROBERTS";
        public static final String newModalServiceTypeText = "vRichardson";
        public static final String newModalVnfTypeText = "vMobileDNS";
        public static final String newModalSourceVersionText = "1.0";
        public static final String newModalVnfNameText = "zolson3amdns02test2";
        public static final String newModalWorkflowText = "VNF Config Update";


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
        public static final String dashboardPendingTableId = "pending-table";
        public static final String dashboardPendingTheadId = "pending-table-head";
        public static final String dashboardFinishedTheadId = "finished-table-head";
        public static final String failedIconClass = "icon-x";
        public static final String processIconClass = "icon-process";
        public static final String alertIconClass = "icon-alert";
        public static final String pendingIconClass = "icon-pending";
        public static final String viewIconClass = "icon-view";
        public static final String pendingTableId = "pending-table";
        public static final String cancelPendingButtonClass = "cancel-action";
        public static final String activeTableRowId = "active-table-cm-row";
        public static final String pendingTableRowId = "pending-table-cm-row";

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
    public class DrawingBoard
    {
        public static final String AVAILABLE_MODELS_TREE = "available-models-tree";
        public static final String DRAWING_BOARD_TREE = "drawing-board-tree";
        public static final String NODE_PREFIX = "node-";
        public static final String SEARCH_LEFT_TREE = "search-left-tree-input";
        public static final String ADD_BUTTON = "-add-btn";
        public static final String CONTEXT_MENU_BUTTON = "-menu-btn";
        public static final String ALERT_ICON = "-alert-icon";
        public static final String CONTEXT_MENU_EDIT = "context-menu-edit";
        public static final String TOGGLE_CHILDREN = "toggle-children";
        public static final String TREE_NODE_LEAF = "tree-node-leaf";
        public static final String HIGHLIGHTED_COLOR = "rgb(0, 159, 219)";
        public static final String STATUS_TEXT = "Designing a new service";
        public static final String CONTEXT_MENU_BUTTON_HEADER = "openMenuBtn";
        public static final String CONTEXT_MENU_HEADER_EDIT_ITEM = "context-menu-header-edit-item";
        public static final String CONTEXT_MENU_HEADER_RESUME_ITEM = "context-menu-header-resume-item";
        public static final String CONTEXT_MENU_HEADER_DELETE_ITEM = "context-menu-header-delete-item";
        public static final String SERVICE_QUANTITY = "servicesQuantity";
        public static final String BACK_BUTTON = "backBtn";
        public static final String STOP_INSTANTIATION_BUTTON = "button-stop-instantiation";
        public static final String CANCEL_BUTTON = "button-cancel";
        public static final String DEPLOY_BUTTON = "deployBtn";
        public static final String DEFAULT_SERVICE_NAME = "<Automatically Assigned>";
        public static final String SERVICE_NAME = "serviceName";
        public static final String SERVICE_STATUS = "serviceStatus";
        public static final String SERVICE_INSTANCE_VALUE = "Service instance:";
        public static final String SERVICE_INSTANCE_TEST_ID = "serviceInstance";
        public static final String QUANTITY_LABEL_TEST_ID = "quantityLabel";
        public static final String QUANTITY_LABEL_VALUE = "Scale Times:";
        public static final String CONTEXT_MENU_SHOW_AUDIT = "context-menu-showAuditInfo";



    }

    public class InstantiationStatus {
        public static final String CONTEXT_MENU_REMOVE = "context-menu-remove";
        public static final String CONTEXT_MENU_DELETE = "context-menu-delete";
        public static final String CONTEXT_MENU_HIDE = "context-menu-hide";
        public static final String CONTEXT_MENU_HEADER_OPEN_ITEM = "context-menu-open";
        public static final String CONTEXT_MENU_RETRY = "context-menu-retry";
        public static final String CONTEXT_MENU_HEADER_AUDIT_INFO_ITEM = "context-menu-audit-info";
        public static final String CONTEXT_MENU_DUPLICATE = "context-menu-duplicate";
        public static final String TD_JOB_STATUS_ICON = "jobStatusIcon";
    }

    public class AuditInfoModal{
        public static final String CANCEL_BUTTON = "cancelButton";

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
        public static final String ENVIRONMENT_RELEASE = "operational-release";
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
        public static final String FULL_LIST_WITHOUT_RELEASE_LABEL = "fullListWithoutReleaseLabel";
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
        public static final String STOP_INSTANTIATION = "Stop Instantiation";
        public static final String CANCEL = "Cancel";
    }

    public static class BrowseASDC {
        public static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss-SSS";
        public static final String SERVICE_INSTANCE_NAME_PREFIX = "vid-e2e-test_service_";
        public static final String DEPOLY_SERVICE_CLASS = "deploy-service-class";
        public static final String SERVICE_INSTANCE_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - Service Instance was created successfully.";
        public static final String SERVICE_INSTANCE_CREATION_FAILED_MESSAGE = "failed to create service instance";
        public static final String CREATE_SERVICE_INSTANCE = "Create Service Instance";
        public static final String AIC_OPTION_CLASS = "aicZoneOption";
        public static class NewServicePopup {
            public static final String SET_BUTTON = "form-set";
            public static final String CANCEL_BUTTON = "cancelButton";
            public static final String INSTANCE_NAME = "instanceName";
            public static final String SERVICE_UUID = "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd";
            public static final String SERVICE_MODEL_DATA_TEST_ID_VALUE_PREFIX = "model-item-value-";
            public static final Map<String, String> SERVICE_MODEL_FIELD_TO_DATA_TESTS_ID = new HashMap<String, String>()
            {{
                put("version", "modelVersion");
                put("description", "description");
                put("category",  "category");
                put("uuid", "uuid");
                put("invariantUuid", "invariantUuid");
                put("type", "serviceType");
                put("serviceRole", "serviceRole");
            }};
        }




    }

    public class EditExistingInstance {
        public static final String SELECT_SERVICE_INSTANCE = "Select a Service Instance";
        public static final String SERVICE_INSTANCE_ID = "Service Instance Id";
        public static final String SERVICE_INSTANCE_NAME = "Service Instance Name";
        public static final String SEARCH_FOR_EXISTING_INSTANCES_INPUT = "searchForExistingInstancesText";
        public static final String SELECT_OWNING_ENTITY_ID = "selectOwningEntity";
        public static final String SELECT_SUBSCRIBER = "selectSubscriber";
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
        public static final String OWNING_ENTITY_OPTION = "owningEntityOption";
        public static final String SELECTED_SUBSCRIBER_NAME_TEST_ID = "selectedSubscriberName";
        public static final String SELECTED_SERVICE_TYPE_NAME_TEST_ID = "selectedServiceTypeName";
        public static final String SERVICE_INSTANCE_NAME_PREFIX = "vid-e2e-test_aai_service_";
        public static final String MODEL_NAME = "Model Name";
        public static final String MODEL_INVARIANT_UUID = "Model Invariant UUID";
        public static final String MODEL_VERSION = "Model Version";
        public static final String MODEL_UUID = "Model UUID";
        public static final String MODEL_CUSTOMIZATION_UUID = "Model Customization UUID";
        public static final String RESOURCE_NAME = "Resource Name";
        public static final String NF_TYPE = "NF Type";
        public static final String NF_ROLE = "NF Role";
        public static final String NF_FUNCTION = "NF Function";
        public static final String NF_NAMING_CODE = "NF Naming Code";
    }

    public class ViewEdit {
        public static final String SERVICE_DELETED_SUCCESSFULLY_TEXT = "COMPLETE - Service has been deleted successfully.";
        public static final String VNF_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - Vnf has been created successfully.";
        public static final String VNF_DELETED_SUCCESSFULLY_TEXT = "COMPLETE - Vnf has been deleted successfully.";
        public static final String VL_DELETED_SUCCESSFULLY_TEXT = "COMPLETE - Network has been deleted successfully.";
        public static final String VOLUME_GROUP_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - Volume Group has been created successfully.";
        public static final String VOLUME_GROUP_DELETED_SUCCESSFULLY_TEXT = "COMPLETE - Volume Group has been deleted successfully.";
        public static final String VF_MODULE_CREATED_SUCCESSFULLY_TEXT = "COMPLETE - VF Module has been created successfully.";
        public static final String VF_MODULE_DELETED_SUCCESSFULLY_TEXT = "COMPLETE - VF Module has been deleted successfully.";
        public static final String MSO_SUCCESSFULLY_TEXT = "COMPLETE - Success";
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
        public static final String ROLLBACK_TEST_ID = "rollback";
        public static final String VOLUME_GROUP_OPTION_TEST_ID_PREFIX = "addVolumeGroupOption-";
        public static final String VF_MODULE_OPTION_TEST_ID_PREFIX = "addVFModuleOption-";
        public static final String VNF_OPTION_TEST_ID_PREFIX = "addVNFOption-";
        public static final String VOLUME_GROUP_INSTANCE_NAME_PREFIX = "vid-e2e-test-volume_group_";
        public static final String NETWORK_INSTANCE_NAME_PREFIX = "vid-e2e-test-network_";
        public static final String VF_MODULE_INSTANCE_NAME_PREFIX = "vid-e2e-test-volume_vf_module_";
        public static final String ADD_VNF_BUTTON_TEST_ID = "addVNFButton";
        public static final String ADD_VF_MODULE_TEST_ID = "addVFModuleButton";
        public static final String ADD_VOLUME_GROUP_BUTTON_TEST_ID = "addVolumeGroupButton";
        public static final String VF_MODULE_RESUME_ID_PREFIX = "resumeVFModuleButton-";
        public static final String ADD_VF_MODULE_BUTTON_TEST_ID = "addVFModuleButton";
        public static final String DELETE_VNF_BUTTON_TEST_ID = "deleteVNFButton";
        public static final String DELETE_NETWORK_BUTTON_TEST_ID = "deleteNetworkButton";
        public static final String DELETE_VF_MODULE_BUTTON_TEST_ID = "deleteVFModuleButton-";
        public static final String DELETE_VNF_VOLUME_GROUP_BUTTON_TEST_ID = "deleteVNFVolumeGroupButton";
        public static final String ACTIVATE_BUTTON_TEST_ID = "activateButton";
        public static final String ACTIVATE_FABRIC_CONFIGURATION_BUTTON_TEST_ID = "activateFabricConfigurationButton";
        public static final String SHOW_ASSIGNMENTS_BUTTON_TEST_ID = "showAssignmentsButton";
        public static final String OPTION_IN_DROPDOWN_NOT_EXISTS = "the option %s in dropdown %s not exists";
        public static final String DISSOCIATE_BTN_CLASS = "dissociate-pnf";
        public static final String DISSOCIATE_CONFIRM_MODAL_TEXT = "Are you sure you would like to dissociate %s from the service instance?";
        public static final String DEACTIVATE_BUTTON_TEST_ID = "deactivateButton";
        public static final String ENABLE_ERROR_MESSAGE = "The %s option should not be enabled";
        public static final String DISABLE_ERROR_MESSAGE = "The %s option should be enabled";
        public static final String DISSOCIATE_CONFIRM_MODAL_BTN_ID = "ok-button";
        public static final String COLLECTIONDIV = "collectionDiv";
        public static final String COLLECTIONNETWORKDIV = "collectionNetworkDiv";
        public static final String INFOSERVICEBUTTON = "infoServiceButton";
        public static final String DELETESERVICEBUTTON = "deleteServiceButton";
        public static final String SERVICE_INSTANCE_ID = "Service Instance ID";
        public static final String DETAILS_LOG = "detailsLog";
        public static final String DETAILS_CLOSE_BTN = "detailsCloseBtn";
        public static final String MSO_COMMIT_LOG = "msoCommitLog";

        public static final String NETWORK_OPTION_TEST_ID_PREFIX = "addNetworkOption-";
        public static final String ADD_NETWORK_BUTTON_TEST_ID = "addNetworkButton" ;
        public static final String LINE_OF_BUSINESS_SELECT_TESTS_ID = "lineOfBusiness";
        public static final String LINE_OF_BUSINESS_MULTI_SELECT_TESTS_ID = "multi-lineOfBusiness";
        public static final String LINE_OF_BUSINESS_TEXT = "Line Of Business";

        public static final String CANCEL_CREATION_BUTTON_ID = "cancelCreation";
        public static final String MODEL_VERSION_ID_MISSING_MSG= "Error: model-version-id is not populated in A&AI";
        public static final String SUBDETAILS_ERROR_MESSAGE_TEST_ID = "subDetailsErrMsg";

        public static final String ERROR_CLASS = "error";
        public static final String COMMON_PORT_MIRRORING_PORT_NAME = "i am a port";
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
    public class ConfigurationCreation{
        public static final String NEXT_BUTTON_TEST_ID = "nextButton";
        public static final String CREATE_BUTTON_TEST_ID = "createButton";
        public static final String BACK_BUTTON_TEST_ID = "backButton";
        public static final String SUPPRESS_ROLLBACK_INPUT_TEST_ID = "suppressRollbackInput";
        public static final String INSTANCE_NAME_INPUT_TEST_ID = "instanceNameInput";
        public static final String REGION_DROPDOWN_TEST_ID = "lcpRegion";
        public static final String TENANT_DROPDOWN_TEST_ID = "tenantDropDown";
        public static final String ENABLE_NEXT_ERROR_MESSAGE = "The Next button is not enabled";
        public static final String DISABLE_NEXT_ASSOCIATE_ERROR_MESSAGE = "The Next button enabled when fields are empty";
        public static final String SOURCE_DROPDOWN_TEST_ID = "sourceDropDown";
        public static final String COLLECTOR_DROPDOWN_TEST_ID = "collectorDropDown";
        public static final String COLLECTOR_INSTANCE_NAME_TEST_ID = "collectorInstanceName";
        public static final String SOURCE_INSTANCE_NAME_TEST_ID = "sourceInstanceName";
        public static final String MODEL_NAME_TEST_ID = "modelName";
        public static final String COLLECTOR_NO_RESULT_MSG_TEST_ID = "collectorNoResults";
        public static final String SOURCE_NO_RESULT_MSG_TEST_ID = "sourceNoResults";
        public static final String SOURCE_INFO_BUTTON_TEST_ID = "sourceInfoButton";
        public static final String COLLECTOR_INFO_BUTTON_TEST_ID = "collectorInfoButton";
        public static final String SOURCE_INSTANCE_SELECTED_ICON_TEST_ID ="sourceInstanceSelectedIcon";
        public static final String COLLECTOR_INSTANCE_SELECTED_ICON_TEST_ID ="collectorInstanceSelectedIcon";
    }

    public class ServiceModelInfo {
        public static final String INFO_TEST_ID_PREFIX = "info-test-data-id-";
        public static final String SERVIICE_NAME_KEY = "ServiceName";
        public static final String ROLLBACK_ON_FAILURE_ID = "rollbackOnFailure";
        public static final String ROLLBACK_ON_FAILURE_TEST_ID = "rollback";
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

    public class ServiceProxyModelInfo {
        public static final String MODEL_NAME = "Name";
        public static final String MODEL_TYPE = "Type";
        public static final String MODEL_DESCRIPTION = "Description";
        public static final String MODEL_INVARIANT_UUID = "InvariantUUID";
        public static final String MODEL_VERSION = "Version";
        public static final String MODEL_UUID = "UUID";
        public static final String MODEL_CUSTOMIZATION_UUID = "CustomizationUUID";
        public static final String SOURCE_MODEL_UUID = "SourceModelUuid";
        public static final String SOURCE_MODEL_INVARIANT = "SourceModelInvariant";
        public static final String SOURCE_MODEL_NAME = "SourceModelName";
    }
    public class NetworkModelInfo {
        public static final String SERVICE_NAME = "Service Name";
        public static final String SUBSCRIBER_NAME = "Subscriber Name";
        public static final String SERVICE_INSTANCE_NAME = "Service Instance Name";
        public static final String MODEL_NAME = "Model Name";
        public static final String MODEL_VERSION = "Model Version";
        public static final String MODEL_INVARIANT_UUID = "Model Invariant UUID";
        public static final String MODEL_UUID = "Model UUID";
        public static final String MODEL_CUSTOMIZATION_UUID = "Model Customization UUID";
    }
    public class OwningEntity {
        public static final String PROJECT_SELECT_TEST_ID = "project";
        public static final String OWNING_ENTITY_SELECT_TEST_ID = "owningEntity";
        public static final String PLATFORM_SELECT_TEST_ID = "platform";
        public static final String PLATFORM_MULTI_SELECT_TEST_ID = "multi-selectPlatform";
        public static final String LOB_SELECT_TEST_ID = "lineOfBusiness";
    }

    public class Users {
        public static final String READONLY = "readonly";
        public static final String SILVIA_ROBBINS_TYLER_SILVIA = "uspVoiceVirtualUsp";
        public static final String EMANUEL_EMANUEL = "emanuelEmanuel";
        public static final String CRAIG_ROBERTS_AIM_TRANSPORT = "FIREWALL_AIM_Trans";
        public static final String EMANUEL_vWINIFRED = "emanuelvWINIFRED";
        public static final String PORFIRIO_GERHARDT = "Porfirio Gerhardt";
    }

    public class RegisterToSimulator {

        //separated simulated json files according to pages
        public class SearchForServiceInstance {
            public static final String GET_SERVICES = "search_for_service_instance/aai_get_services.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID = "search_for_service_instance/aai_filter_service_instance_by_id.json";
            public static final String FILTER_CR_SERVICE_INSTANCE_BY_ID = "search_for_service_instance/aai_filter_cr_service_instance_by_id.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID_2 = "search_for_service_instance/aai_filter_service_instance_by_id_2.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID_PM = "search_for_service_instance/aai_filter_service_instance_by_id_test_sssdad.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID_NO_MODEL_VER_ID = "search_for_service_instance/aai_filter_service_instance_by_id_test_without_model_ver_id.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS = "search_for_service_instance/aai_get_subscribers_for_customer_SILVIA-ROBBINS.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS_CR = "search_for_service_instance/aai_get_subscribers_for_customer_SILVIA-ROBBINS_cr.json";
            public static final String GET_MSO_INSTANCE_ORCH_STATUS_REQ = "search_for_service_instance/mso_instance_orch_status_req.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_Emanuel = "search_for_service_instance/aai_get_subscribers_for_customer_Emanuel.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_CRAIG_ROBERTS = "search_for_service_instance/aai_get_subscribers_for_customer_CRAIG-ROBERTS.json";
            public static final String NAMED_QUERY_VIEW_EDIT = "search_for_service_instance/aai_named_query_for_view_edit.json";
            public static final String NAMED_QUERY_CR_VIEW_EDIT = "search_for_service_instance/aai_named_query_for_cr_view_edit.json";
            public static final String NAMED_QUERY_VIEW_EDIT_2 = "search_for_service_instance/aai_named_query_for_view_edit_2.json";
            public static final String NAMED_QUERY_VIEW_EDIT_PM = "search_for_service_instance/aai_named_query_for_view_edit_test_sssdad.json";
            public static final String GET_SDC_CATALOG_SERVICE_VID_TEST_444 = "search_for_service_instance/get_sdc_catalog_services_vid-test-444.json";
            public static final String GET_SDC_CATALOG_SERVICE_VID_TEST_CR = "search_for_service_instance/get_sdc_catalog_services_vid-test-cr.json";
            public static final String GET_SDC_CATALOG_SERVICE_PM = "search_for_service_instance/get_sdc_catalog_services_test_sssdad.json";
        }

        public class AddSubinterface {
            public static final String FILTER_VFC_IG_SERVICE_INSTANCE_BY_ID = "add_subinterface/aai_filter_vfc_ig_service_instance_by_id.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS_VFC_IG = "add_subinterface/aai_get_subscribers_for_customer_SILVIA-ROBBINS_vfc_ig.json";
            public static final String GET_MSO_VFC_IG_INSTANCE_ORCH_STATUS_REQ = "add_subinterface/mso_vfc_ig_instance_orch_status_req.json";
            public static final String NAMED_QUERY_VFC_IG_VIEW_EDIT = "add_subinterface/aai_named_query_for_vfc_ig_view_edit.json";
            public static final String GET_SDC_CATALOG_SERVICE_VID_TEST_444 = "add_subinterface/get_sdc_catalog_services_vid-test-333.json";
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
            public static final String ACTIVATE_SERVICE_INSTANCE = "activateDeactivate/mso_activate_service_instance.json";
            public static final String ACTIVATE_SERVICE_INSTANCE_ERROR = "activateDeactivate/mso_activate_service_instance_error.json";
            public static final String ACTIVATE_SERVICE_INSTANCE_ORCH_REQUEST = "activateDeactivate/mso_activate_service_instance_orch_req.json";
        }

        public class createConfiguration {
            public static final String GET_TENANTS = "create_configuration/aai_get_tenants.json";
            public static final String GET_VNF_INSTANCES = "create_configuration/aai_get_vnf_instances_by_region.json";
            public static final String GET_PNF_INSTANCES = "create_configuration/aai_get_pnf_instances_by_region.json";
            public static final String GET_MODEL_BY_ONE_INVARIANT_ID = "create_configuration/aai_get_model_by_one_invariant_id.json";
            public static final String GET_MODEL_BY_2_INVARIANT_IDS = "create_configuration/aai_get_model_by_2_invariant_ids.json";
        }

        public class addNetwork{
            public static final String GET_SDC_CATALOG_SERVICES_NETWORK = "add_network/get_sdc_catalog_services_SI_network11.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_NAME = "add_network/aai_filter_service_instance_by_name_network-vl.json";
            public static final String FILTER_SERVICE_INSTANCE_BY_ID = "add_network/aai_filter_service_instance_by_id_network-vl.json";
            public static final String AAI_NAMED_QUERY_FOR_VIEW_EDIT ="add_network/aai_named_query_for_view_edit_test_network-vl.json";
            public static final String AAI_GET_TENANTS = "add_network/aai_get_tenants.json";
            public static final String MSO_ADD_NETWORK_ERROR = "add_network/mso_add_network_error.json";
        }

        public class CreateNewServiceInstance {
            public static final String GET_SERVICES = "create_new_instance/aai_get_services.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_CAR_2020_ER = "create_new_instance/aai_get_subscribers_for_customer_CAR_2020_ER.json";
            public static final String GET_MODELS_BY_SERVICE_TYPE_CAR_2020_ER = "create_new_instance/aai_get_models_by_service_type_CAR_2020_ER.json";
            public static final String GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS = "create_new_instance/aai_get_subscribers_for_customer_SILVIA_ROBBINS.json";
            public static final String GET_MODELS_BY_SERVICE_TYPE_SILVIA_ROBBINS= "create_new_instance/aai_get_models_by_service_type_SILVIA_ROBBINS.json";

            public class deploy {
                public static final String SDC_GET_CATALOG = "create_new_instance/deploy/get_sdc_catalog_services_1707vidnf.json";
                public static final String GET_AIC_ZONES = "create_new_instance/deploy/aai_get_aic_zones.json";
                public static final String MSO_CREATE_SVC_INSTANCE = "create_new_instance/deploy/mso_create_svc_instance.json";
                public static final String MSO_CREATE_SVC_INSTANCE_ORCH_REQ = "create_new_instance/deploy/mso_create_svc_instance_orch_req.json";
            }
        }

        public class genericRequest{
            public static final String ECOMP_PORTAL_GET_SESSION_SLOT_CHECK_INTERVAL = "ecompportal_getSessionSlotCheckInterval.json";
        }
    }
    public class VlanTagging {
        public static final String MODEL_ITEM_LABEL_SERVICE_INSTANCENAME = "model-item-label-serviceInstanceName";
        public static final String MODEL_ITEM_VALUE_SERVICE_INSTANCENAME = "model-item-value-serviceInstanceName";
        public static final String MODEL_ITEM_LABEL_MODEL_INVARIANT_UUID = "model-item-label-modelInvariantUUID";
        public static final String MODEL_ITEM_VALUE_MODEL_INVARIANT_UUID = "model-item-value-modelInvariantUUID";
        public static final String MODEL_ITEM_LABEL_MODEL_VERSION = "model-item-label-modelVersion";
        public static final String MODEL_ITEM_VALUE_MODEL_VERSION = "model-item-value-modelVersion";
        public static final String MODEL_ITEM_LABEL_MODEL_UUID = "model-item-label-modelUuid";
        public static final String MODEL_ITEM_VALUE_MODEL_UUID = "model-item-value-modelUuid";
        public static final String MODEL_ITEM_LABEL_CUSTOMIZATION_UUID = "model-item-label-modelCustomizationUuid";
        public static final String MODEL_ITEM_VALUE_CUSTOMIZATION_UUID = "model-item-value-modelCustomizationUuid";
        public static final String MODEL_ITEM_LABEL_GROUP_NAME = "model-item-label-groupName";
        public static final String MODEL_ITEM_VALUE_GROUP_NAME = "model-item-value-groupName";
        public static final String MODEL_ITEM_LABEL_NETWORK_COLLECTION_FUNCTION = "model-item-label-networkCollectionFunction";
        public static final String MODEL_ITEM_VALUE_NETWORK_COLLECTION_FUNCTION = "model-item-value-networkCollectionFunction";
        public static final String MODEL_ITEM_LABEL_INSTANCE_GROUP_FUNCTION = "model-item-label-instanceGroupFunction";
        public static final String MODEL_ITEM_VALUE_INSTANCE_GROUP_FUNCTION = "model-item-value-instanceGroupFunction";
        public static final String MODEL_ITEM_LABEL_PARENT_PORT_ROLE = "model-item-label-parentPortRole";
        public static final String MODEL_ITEM_VALUE_PARENT_PORT_ROLE = "model-item-value-parentPortRole";
        public static final String MODEL_ITEM_LABEL_SUBINTERFACE_ROLE = "model-item-label-subInterfaceRole";
        public static final String MODEL_ITEM_VALUE_SUBINTERFACE_ROLE = "model-item-value-subInterfaceRole";
    }
}
