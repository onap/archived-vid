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
        //general constants:
        public static final String pageHeadlineId = "test-environments-headline";

        //headline bar constants:
        public static final String headlineNewButtonId = "test-environments-new-button";
        public static final String headlineSearchInputId = "test-environments-search";

        public static final String refreshButtonId = "test-environments-refresh";
        public static final String noDataMessage = "test-environments-no-data";
        public static final String environmentsTable = "test-environments-table";
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
}
