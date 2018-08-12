package vid.automation.test.test;


import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.*;
import vid.automation.test.model.User;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.DrawingBoardPage;
import vid.automation.test.sections.VidBasePage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static vid.automation.test.Constants.DrawingBoard.*;
import static vid.automation.test.infra.Features.FLAG_ASYNC_INSTANTIATION;

@FeatureTogglingTest(FLAG_ASYNC_INSTANTIATION)
public class DrawingBoardTest extends VidBaseTestCase {

    private DrawingBoardPage drawingBoardPage = new DrawingBoardPage();
    private String loadedServiceModelId = "";
    private final String defaultServiceModelForMostTests = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";


    @Override
    protected UserCredentials getUserCredentials() {
        String userName = Constants.Users.USP_VOICE_VIRTUAL_USP;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    public DrawingBoardTest() {
    }

    @AfterClass
    private void goOutFromIframe() {
        new VidBasePage().goOutFromIframe();
    }


    @BeforeMethod
    private void setNewServiceToDefault() throws Exception {
        final String currentUrl = getDriver().getCurrentUrl();
        System.out.println("currentUrl in @BeforeMethod setNewServiceToDefault: " + currentUrl);

        if (currentUrl.endsWith("/servicePlanning?serviceModelId=" + defaultServiceModelForMostTests)) {
            getDriver().navigate().refresh();
        } else {
            setNewService("service-Complexservice-csar.zip", defaultServiceModelForMostTests, null);
        }
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.goToIframe();
    }

    private void setNewService(String zipFileName, String serviceModelId, String instanceName) {
        goOutFromIframe();
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();

        loadedServiceModelId = serviceModelId;
        loadServicePopup(zipFileName, serviceModelId);

        if (instanceName != null) {
            Input.text(instanceName,Constants.BrowseASDC.NewServicePopup.INSTANCE_NAME);
        }

        Wait.waitByClassAndText("subscriber","",3);
        GeneralUIUtils.ultimateWait();
        VidBasePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
        GeneralUIUtils.ultimateWait();
        browseASDCPage.selectProductFamily("e433710f-9217-458d-a79d-1c7aff376d89");
        String serviceType = "VIRTUAL USP";
        Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
        browseASDCPage.selectServiceTypeByName(serviceType);
        GeneralUIUtils.ultimateWait();
        GeneralUIUtils.ultimateWait();
        browseASDCPage.selectLcpRegion("mtn6");
        browseASDCPage.selectTenant("3f21eeea6c2c486bba31dab816c05a32");
        Click.onFirstSelectOptionById(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);

        browseASDCPage.selectAicZone("NFT1");
        Click.onFirstSelectOptionById(Constants.OwningEntity.PROJECT_SELECT_TEST_ID);

        // select mandatory field
        Click.onFirstSelectOptionById(Constants.ServiceModelInfo.ROLLBACK_ON_FAILURE_ID);

        Click.byTestId("service-form-set");
        browseASDCPage.goOutFromIframe();
    }

    static final String leftTreeNodeName = "VF_vMee 0";
    static final String leftTreeNodeNameWithoutChildren = "Port Mirroring Configuration By Policy 0";
    static final String[] leftTreeNodeChildren = {"vf_vmee0..VfVmee..vmme_vlc..module-1", "vf_vmee0..VfVmee..vmme_gpb..module-2", "vf_vmee0..VfVmee..base_vmme..module-0"};
    static final String[] leftTreeInitialElements = new String[]{"V", "VF_vMee 0", "C","Port Mirroring Configuration By Policy 0","N", "ExtVL 0"};

    static final String rightTreeNodeName = "d6557200-ecf2-4641-8094-5393ae3aae60-VF_vMee 0";
    static final String rightTreeNodeNameWithoutChildren = "ddc3f20c-08b5-40fd-af72-c6d14636b986-ExtVL 0";
    static final String[] rightTreeNodeChildren = {"522159d5-d6e0-4c2a-aa44-5a542a12a830-vf_vmee0..VfVmee..vmme_vlc..module-1", "41708296-e443-4c71-953f-d9a010f059e1-vf_vmee0..VfVmee..vmme_gpb..module-2", "a27f5cfc-7f12-4f99-af08-0af9c3885c87-vf_vmee0..VfVmee..base_vmme..module-0"};

    private static final String EMPTY_BOARD_TITLE = "Please add objects (VNFs, network, modules etc.) from the left tree to design the service instance";
    private static final String EMPTY_BOARD_SUBTITLE = "Once done, click Deploy to start instantiation";


    static final String[] rightTreeInitialElements = new String[]{
        "Vnf:",
        "vnf instance 1",
        "Vf Module:",
        "vf module instance 1",
        "Vf Module:",
        "vf module instance 2",
        "Vf Module",
        "Configuration:",
        "configuration instance 1",
        "Network:",
        "network instance 1"};

    @Test
    private void expandCollapseLeftTreeByClickOnRow() {
        Wait.byText(leftTreeNodeName);
        drawingBoardPage.expandTreeByClickingNode(leftTreeNodeName, leftTreeNodeChildren);
        //should checking with Golan if click also collapse
        //drawingBoardPage.collapseTreeByClickingNode(leftTreeNodeName, leftTreeNodeChildren)));
        drawingBoardPage.verifyNonCollapsableTreeByClickingNode(leftTreeNodeName, leftTreeNodeChildren);
    }

    @Test
    private void expandCollapseLeftTreeByClickOnIcon() {
        drawingBoardPage.expandFirstItemInTreeByExpanderIcon(AVAILABLE_MODELS_TREE, leftTreeNodeChildren);
        drawingBoardPage.collapseFirstItemInTreeByCollapseIcon(AVAILABLE_MODELS_TREE, leftTreeNodeChildren);
    }

    @Test
    private void checkInitialStateLeftTree() {
        drawingBoardPage.assertInitalTextOfTree(AVAILABLE_MODELS_TREE, leftTreeInitialElements);
    }

//    @Test
//    private void checkAddButton() {
//        drawingBoardPage.checkAddButton(leftTreeRootElements);
//    }

    @Test
    private void checkLeafNodeLeftTreeHasNoExpander() {
        drawingBoardPage.checkLeafNodeHasNoExpander(leftTreeNodeNameWithoutChildren);
    }


    @Test(groups = { "underDevelopment" })
    private void collapseExpandRightTreeByClickOnRow() {
        drawingBoardPage.collapseFirstItemInTreeByCollapseIcon(DRAWING_BOARD_TREE, rightTreeNodeChildren);
        //should checking with Golan if click also collapse
        //drawingBoardPage.collapseTreeByClickingNode(rightTreeNodeName, rightTreeNodeChildren)));
        drawingBoardPage.expandTreeByClickingNode(rightTreeNodeName, rightTreeNodeChildren);
    }

    @Test(groups = { "underDevelopment" })
    private void collapseExpandRightTreeByClickOnIcon() {
        drawingBoardPage.collapseFirstItemInTreeByCollapseIcon(DRAWING_BOARD_TREE, rightTreeNodeChildren);
        drawingBoardPage.expandFirstItemInTreeByExpanderIcon(DRAWING_BOARD_TREE, rightTreeNodeChildren);
    }

    @Test(groups = { "underDevelopment" })
    private void checkInitialStateRightTree() {
        drawingBoardPage.assertInitalTextOfTree(DRAWING_BOARD_TREE, rightTreeInitialElements);
    }

    @Test(groups = { "underDevelopment" })
    private void checkLeafNodeRightTreeHasNoExpander() {
        drawingBoardPage.checkLeafNodeHasNoExpander(rightTreeNodeNameWithoutChildren);
    }

    @Test
    public void testEmptyDrawingBoard() {
        assertNotNull(Get.byClassAndText("text-title", EMPTY_BOARD_TITLE));
        assertNotNull(Get.byClassAndText("text-subtitle", EMPTY_BOARD_SUBTITLE));
        WebElement icon = Get.byTestId("no-content-icon");
        assertEquals("img", icon.getTagName());
        assertThat(icon.getAttribute("src"), endsWith("UPLOAD.svg"));
        assertThat(icon.getAttribute("class"), containsString("no-content-icon"));
        assertThat(icon.getAttribute("class"), containsString("upload-icon-service-planing"));
    }

    @Test(groups = { "underDevelopment" })
    private void clickRightTreeNode_verifyLeftTreeNodeIsBlueAndExpanded() {

        //// setup
        drawingBoardPage.checkNodesVisible(leftTreeNodeChildren, false);

        clickAndVerifyCrossTreeSync(
                DrawingBoardTest.rightTreeNodeName,
                DrawingBoardTest.leftTreeNodeName,
                DrawingBoardTest.leftTreeNodeChildren
        );
    }

    @Test(groups = { "underDevelopment" })
    private void clickLeftTreeNode_verifyRightTreeNodeIsBlueAndExpanded() {

        //// setup
        drawingBoardPage.collapseFirstItemInTreeByCollapseIcon(DRAWING_BOARD_TREE, rightTreeNodeChildren);

        clickAndVerifyCrossTreeSync(
                leftTreeNodeName,
                rightTreeNodeName,
                rightTreeNodeChildren
        );
    }

    @Test(groups = { "underDevelopment" })
    private void clickRightTreeChild_verifyLeftTreeChildIsBlueAndParentExpanded() {

        //// setup
        drawingBoardPage.checkNodesVisible(leftTreeNodeChildren, false);

        clickAndVerifyCrossTreeSync(
                rightTreeNodeChildren[0],
                leftTreeNodeChildren[0],
                leftTreeNodeChildren
        );
    }

    @Test(groups = { "underDevelopment" })
    private void clickLeftTreeChild_verifyRightTreeChildIsBlueAndParentExpanded() {

        //// setup
        drawingBoardPage.expandTreeByClickingNode(leftTreeNodeName, leftTreeNodeChildren);
        drawingBoardPage.collapseFirstItemInTreeByCollapseIcon(DRAWING_BOARD_TREE, rightTreeNodeChildren);

        clickAndVerifyCrossTreeSync(
                leftTreeNodeChildren[0],
                rightTreeNodeChildren[0],
                rightTreeNodeChildren
        );
    }

    private void clickAndVerifyCrossTreeSync(String clickOn, String verifyHighlight, String[] verifyVisible) {
        //// test
        drawingBoardPage.clickNode(clickOn);

        //// verify
        drawingBoardPage.checkNodesHighlighted(new String[]{verifyHighlight});
        drawingBoardPage.checkNodesVisible(verifyVisible, true);
    }


    /*
      + Search box
        - exists
        - works (skimmy)
    */
    @Test
    private void insertTestInSerachBox_verifyMatchesMarkedAndVisible() {
        Wait.byText("vf_vmee0..VfVmee..vmme_vlc..module-1");
        drawingBoardPage.checkSearch();

        final String searchTerm = "Vmee..vmme";
        GeneralUIUtils.setWebElementByTestId(SEARCH_LEFT_TREE, searchTerm);
        drawingBoardPage.checkNodesVisibleAndMatchIsHighlighted(searchTerm,"vf_vmee0..VfVmee..vmme_vlc..module-1", "vf_vmee0..VfVmee..vmme_gpb..module-2");
    }

    /*
      + Context menu
        - click opens popup
        - click closes popup
    */
    @Test(groups = { "underDevelopment" })
    private void clickOnParentEllipsis_menuIsVisible() {
        drawingBoardPage.checkContextMenu(rightTreeNodeNameWithoutChildren);
    }

    @Test(groups = { "underDevelopment" })
    private void clickOnNodeEllipsis_menuIsVisible() {
        drawingBoardPage.checkContextMenu(rightTreeNodeChildren[1]);
    }

    /*
      + Tooltip
        - hover: text appears
        - again for "child"
        - hover on no-icon: text does not appear
     */
    @Test(groups = { "underDevelopment" })
    private void hoverAboveParentAlert_verifyDescriptionAppears() {
        drawingBoardPage.showTooltipByHoverAboveAlertIcon(rightTreeNodeName);
    }

    @Test(groups = { "underDevelopment" })
    private void hoverAboveNodeAlert_verifyDescriptionAppears() {
        drawingBoardPage.showTooltipByHoverAboveAlertIcon(rightTreeNodeChildren[1]);
    }

    @Test
    private void clickOnHeaderEllipsis_menuIsVisible() {
        final String contextMenu = CONTEXT_MENU_HEADER_EDIT_ITEM;
        drawingBoardPage.checkThatContextMenuNotExist(contextMenu);
        Click.byTestId(CONTEXT_MENU_BUTTON_HEADER);
        drawingBoardPage.checkThatContextMenuExist(contextMenu);
    }

    private void setAndAssertServiceWithName(String instanceName){
        goOutFromIframe();
        setNewService("csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596.zip", "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", instanceName);
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.goToIframe();
        drawingBoardPage.checkServiceInstanceName(instanceName);
    }



    @Test
    private void editPopup_quantityNumberAndNameAreUpdated() {
        String initialInstanceName = "MyInstanceName";
        String updatedInstanceName = "UpdatedInstanceName";
        setAndAssertServiceWithName(initialInstanceName);
        final int updatedQuantity = 10;
        Click.byTestId(CONTEXT_MENU_BUTTON_HEADER);
        Click.byTestId(CONTEXT_MENU_HEADER_EDIT_ITEM);
        Assert.assertTrue(Exists.byId("service-popup"), "context menu should not appear");
        SelectOption.byIdAndVisibleText("quantity-select", String.valueOf(updatedQuantity));
        Input.replaceText(updatedInstanceName,Constants.BrowseASDC.NewServicePopup.INSTANCE_NAME );
        Click.byTestId(Constants.BrowseASDC.NewServicePopup.SET_BUTTON);
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.checkQuantityNumberIsCorrect(updatedQuantity);
        drawingBoardPage.checkServiceInstanceName(updatedInstanceName);
    }

    @Test
    private void cancelEditPopup_quantityNumberAndNameNotUpdated() {
        String initialInstanceName = "MyInstanceName";
        String updatedInstanceName = "UpdatedInstanceName";
        setAndAssertServiceWithName(initialInstanceName);
        final int updatedQuantity = 10;
        String initialQuantity = Get.byTestId(SERVICE_QUANTITY).getText();
        Click.byTestId(CONTEXT_MENU_BUTTON_HEADER);
        Click.byTestId(CONTEXT_MENU_HEADER_EDIT_ITEM);
        Assert.assertTrue(Exists.byId("service-popup"), "context menu should not appear");
        SelectOption.byIdAndVisibleText("quantity-select", String.valueOf(updatedQuantity));
        Input.replaceText(updatedInstanceName,Constants.BrowseASDC.NewServicePopup.INSTANCE_NAME );
        Click.byTestId(Constants.BrowseASDC.NewServicePopup.CANCEL_BUTTON);
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.checkQuantityNumberIsCorrect(Integer.valueOf(initialQuantity));
        drawingBoardPage.checkServiceInstanceName(initialInstanceName);
    }


    @Test
    private void checkHeader_verifyElementsExist(){
        drawingBoardPage.checkExistsAndEnabled(BACK_BUTTON);
        drawingBoardPage.checkServiceInstanceName(DEFAULT_SERVICE_NAME);
        drawingBoardPage.checkServiceStatus();
        drawingBoardPage.checkQuantityNumberIsCorrect(1);
        drawingBoardPage.checkExistsAndEnabled(CONTEXT_MENU_BUTTON_HEADER);
        //drawingBoardPage.checkDeployButtonDisabled();
    }

    private void assertResultsInBrowseAsdcPage(){
        goOutFromIframe();
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        browseASDCPage.assertSearchFilterValue(loadedServiceModelId);
    }

    @Test
    private void BackButtonWithCancel_verifyStayInTheSamePage(){
        String currentUrl = getCurrentUrl();
        Click.byTestId(BACK_BUTTON);
        Click.byText(Constants.Modals.CANCEL);
        String newUrl = getCurrentUrl();
        org.testng.Assert.assertEquals(currentUrl, newUrl);
    }

    private String getCurrentUrl(){
        WebDriver driver = GeneralUIUtils.getDriver();
        return driver.getCurrentUrl();
    }

    @Test
    private void BackButton_verifyReturnToSearchResults(){
        Click.byTestId(BACK_BUTTON);
        Click.byText(Constants.Modals.STOP_INSTANTIATION);
        assertResultsInBrowseAsdcPage();
    }

    @Test
    private void deleteButon_verifyReturnToSearchResults() {
        Click.byTestId(CONTEXT_MENU_BUTTON_HEADER);
        Click.byTestId(CONTEXT_MENU_HEADER_DELETE_ITEM);
        Click.byText(Constants.Modals.STOP_INSTANTIATION);
        assertResultsInBrowseAsdcPage();
    }






}

