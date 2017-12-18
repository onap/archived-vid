package vid.automation.test.test;


//import com.sun.tools.internal.jxc.ap.Const;
import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.*;
import vid.automation.test.sections.ChangeManagementPage;

import java.util.List;

public class ChangeManagementTest extends VidBaseTestCase {
    @Test
    public void testLeftPanelChangeManagementButton() {
        List<WebElement> leftPanelButtons = Get.byClass(Constants.SideMenu.buttonClass);
        Assert.assertTrue(leftPanelButtons.size() == Constants.SideMenu.numOfButtons);
        Assert.assertTrue(Wait.byText(Constants.SideMenu.VNF_CHANGES));
    }

    @Test
    public void testChangeManagementHeaderLine() {
        ChangeManagementPage.openChangeManagementPage();
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pageHeadlineId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.headlineNewButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.headlineSchedulerButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.headlineSearchInputId));
    }

    @Test
    public void testOpenNewChangeManagementModal() {
        ChangeManagementPage.openNewChangeManagementModal();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalSubscriberInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalServiceTypeInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFNameInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFTypeInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalFromVNFVersionInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalWorkFlowInputId));
        Assert.assertTrue(Exists.byId(Constants.generalSubmitButtonId));
        Assert.assertTrue(Exists.byId(Constants.generalCancelButtonId));
        Click.byId(Constants.generalCancelButtonId);
        Wait.modalToDisappear();
    }

    @Test
    public void testNewChangeManagementModalFunctionality() {
        ChangeManagementPage.openNewChangeManagementModal();
        Wait.angularHttpRequestsLoaded();
        Click.onFirstSelectOptionById(Constants.ChangeManagement.newModalSubscriberInputId);
        Wait.angularHttpRequestsLoaded();
        Click.onFirstSelectOptionById(Constants.ChangeManagement.newModalServiceTypeInputId);
        Wait.angularHttpRequestsLoaded();
        Click.onFirstSelectOptionById(Constants.ChangeManagement.newModalVNFTypeInputId);
        Wait.angularHttpRequestsLoaded();
        Click.onFirstSelectOptionById(Constants.ChangeManagement.newModalFromVNFVersionInputId);
        Wait.angularHttpRequestsLoaded();
        Click.byId(Constants.ChangeManagement.newModalVNFNameInputId);
        Click.byClass(Constants.ChangeManagement.newModalVNFNameInputFirstElementClass);
        Wait.angularHttpRequestsLoaded();
        Click.onFirstSelectOptionById(Constants.ChangeManagement.newModalWorkFlowInputId);

        Assert.assertTrue(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        Click.byId(Constants.generalCancelButtonId);
        Wait.modalToDisappear();
    }

    public void scheduleChange(String subscriberId, String serviceType, String vnfType, String vnfVersion,
                               String workflow, String duration, String fallback, String concurrencyLimit, String policy) {
        ChangeManagementPage.openNewChangeManagementModal();
        Wait.angularHttpRequestsLoaded();
        ChangeManagementPage.selectSubscriberById(subscriberId);
        Wait.angularHttpRequestsLoaded();
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalServiceTypeInputId, serviceType);
        Wait.angularHttpRequestsLoaded();
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalVNFTypeInputId, vnfType);
        Wait.angularHttpRequestsLoaded();
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalFromVNFVersionInputId, vnfVersion);
        Wait.angularHttpRequestsLoaded();
        Click.byId(Constants.ChangeManagement.newModalVNFNameInputId);
        Click.byClass(Constants.ChangeManagement.newModalVNFNameInputFirstElementClass);
        Wait.angularHttpRequestsLoaded();
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalWorkFlowInputId, workflow);

        Assert.assertTrue(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        Click.byId(Constants.generalSubmitButtonId);

        Wait.byText(Constants.ChangeManagement.schedulerModalNowLabel);
        Click.byText(Constants.ChangeManagement.schedulerModalNowLabel);

//        Click.byId(Constants.ChangeManagement.schedulerModalStartDateInputId); //next month must be in the future
//        Click.byClass(Constants.ChangeManagement.schedulerModalNextMonthButtonClass);
//        Wait.byText(startDate);
//        Click.byText(startDate);
//
//        Click.byId(Constants.ChangeManagement.schedulerModalEndDateInputId); //next month must be in the future
//        Click.byClass(Constants.ChangeManagement.schedulerModalNextMonthButtonClass);
//        Wait.byText(endDate);
//        Click.byText(endDate);

        SelectOption.byValue(Constants.ChangeManagement.schedulerModalHoursOption, Constants.ChangeManagement.schedulerModalTimeUnitSelectId);

        Input.text(duration, Constants.ChangeManagement.schedulerModalDurationInputTestId);
        Input.text(fallback, Constants.ChangeManagement.schedulerModalFallbackInputTestId);
        Input.text(concurrencyLimit, Constants.ChangeManagement.schedulerModalConcurrencyLimitInputTestId);
        Wait.angularHttpRequestsLoaded();
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.schedulerModalPolicySelectId, policy);

        Click.byText(Constants.ChangeManagement.schedulerModalScheduleButtonText);

    }

    @Test
    public void testUpdateWorkflowNow() {
        String subscriberId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        String serviceType  = "Mobility";
        String vnfType = "nf-role in model";
        String vnfVersion = "1.0";
        String workflow = "Update";

        String duration = "1";
        String fallback = "1";
        String concurrencyLimit = "1";
        String policy = "SNIRO_1710.Config_MS_PlacementOptimizationPolicy_dhv_v1.1.xml";

        scheduleChange(subscriberId, serviceType, vnfType, vnfVersion, workflow, duration,
                fallback, concurrencyLimit, policy);
    }

    @Test
    public void testNewChangeManagementCreation() {
        ChangeManagementPage.openChangeManagementPage();

        //TODO: After scheduler will be ready than we will examine if the creation working fine.
    }

    @Test
    public void testMainDashboardTable() {
        ChangeManagementPage.openChangeManagementPage();
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardActiveTabId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardFinishedTabId));

        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardActiveTableId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardInProgressTheadId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardPendingTheadId));

        Click.byId(Constants.ChangeManagement.dashboardFinishedTabId);
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardFinishedTableId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardFinishedTheadId));
        Click.byId(Constants.ChangeManagement.dashboardActiveTabId);
    }

    @Test
    public void testMainDashboardTableContent() {
        ChangeManagementPage.openChangeManagementPage();

        //TODO: After scheduler will be ready than we will examine if the content is valid.
    }

    @Test
    public void testOpenFailedStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if(!Exists.byClass(Constants.ChangeManagement.failedIconClass)) {
            //TODO: Create a job which will shown with status fail.
        }

        Click.byClass(Constants.ChangeManagement.failedIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalRetryButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test
    public void testOpenInProgressStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if(!Exists.byClass(Constants.ChangeManagement.processIconClass)) {
            //TODO: Create a job which will shown with status in-progress.
        }

        Click.byClass(Constants.ChangeManagement.processIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalStopButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test
    public void testOpenAlertStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if(!Exists.byClass(Constants.ChangeManagement.alertIconClass)) {
            //TODO: Create a job which will shown with status alert.
        }

        Click.byClass(Constants.ChangeManagement.alertIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalContinueButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test
    public void testOpenPendingStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if(!Exists.byClass(Constants.ChangeManagement.pendingIconClass)) {
            //TODO: Create a job which will shown with status pending.
        }

        Click.byClass(Constants.ChangeManagement.pendingIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalRescheduleButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }
}
