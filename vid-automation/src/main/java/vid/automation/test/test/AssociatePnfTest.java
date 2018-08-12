package vid.automation.test.test;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Wait;
import vid.automation.test.sections.PnfSearchAssociationPage;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

public class AssociatePnfTest extends VidBaseTestCase {

    private VidBasePage vidBasePage = new VidBasePage();
    private ViewEditPage viewEditPage = new ViewEditPage();
    private PnfSearchAssociationPage pnfSearchAssociationPage =  new PnfSearchAssociationPage();
    private String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    private String pnfInstanceName = "MX_960-F722";
    private String pnfModelName = "pnf 0";

    @Test
    public void testAssociatePnf() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.associatePnf();

        goToExistingInstanceById(serviceInstanceId);//vid-test-444
        addPNF(pnfModelName);//vid-test-444
        searchPNF(pnfInstanceName);
        testResultSearchPNF();
        associatePNF();
        vidBasePage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
        vidBasePage.clickCloseButton();
    }

    @Test
    public void testRainyAssociatePnf() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.associatePnfError();

        goToExistingInstanceById(serviceInstanceId);
        addPNF(pnfModelName);
        searchPNF(pnfInstanceName);
        associatePNF();
        vidBasePage.assertMsoRequestModal("Error");
        vidBasePage.clickCloseButton();
    }
    @Test
    public void testRainyChoosePnf() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.associatePnfError();

        goToExistingInstanceById(serviceInstanceId);
        String pnfName= "pnf 1";
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.ADD_VNF_BUTTON_TEST_ID, 60);
        String message = String.format(Constants.ViewEdit.OPTION_IN_DROPDOWN_NOT_EXISTS,
                Constants.ViewEdit.ADD_VNF_BUTTON_TEST_ID,"Add network instance");
        boolean waitForElementResult = Wait.waitByTestId(Constants.ViewEdit.VNF_OPTION_TEST_ID_PREFIX + pnfName, 60);
        Assert.assertTrue(message, !waitForElementResult);
    }
    @Test
    public void testRainySearchPnfInstance() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.associatePnfError();

        goToExistingInstanceById(serviceInstanceId);
        addPNF(pnfModelName);
        searchPNF("AAAAA");
        Wait.angularHttpRequestsLoaded();
        WebElement errorMessageWebElement = GeneralUIUtils.getWebElementByTestID(Constants.PnfAssociation.NOT_FOUND_ERROR_TEST_ID, 60);
        Assert.assertEquals("not found PNF instance, error message not equals the expected message","The specified PNF instance AAAAA does not exist in A&AI.\n" +
                "Please note: A&AI is case sensitive",errorMessageWebElement.getText());
        Assert.assertTrue(Constants.PnfAssociation.NOT_FOUND_ERROR_MESSAGE, errorMessageWebElement!=null);
        assertAssociateButtonState(false);
    }

    private void assertAssociateButtonState(boolean shouldBeEnabled) {
        WebElement associateWebElement = GeneralUIUtils.getWebElementByTestID(Constants.PnfAssociation.ASSOCIATE_PNF_TEST_ID, 60);
        boolean enabledAssociate=associateWebElement.isEnabled();

        if(shouldBeEnabled) {
            Assert.assertTrue(Constants.PnfAssociation.PNF_ENABLE_ASSOCIATE_ERROR_MESSAGE, enabledAssociate);
        }else{
            Assert.assertTrue(Constants.PnfAssociation.PNF_DISABLE_ASSOCIATE_ERROR_MESSAGE, !enabledAssociate);
        }
        GeneralUIUtils.ultimateWait();
    }
    private void associatePNF() throws InterruptedException {
        pnfSearchAssociationPage.clickAssociateButton();
    }

    private void addPNF(String name){
        viewEditPage.selectNodeInstanceToAdd(name);
        checkServiceModelInfo();
        assertAssociateButtonState(false);
    }

    private void searchPNF(String name){
        pnfSearchAssociationPage.setPnfName(name);
        pnfSearchAssociationPage.clickSearchButton();
    }

    private void testResultSearchPNF(){
        checkPnfProperties();
        Wait.angularHttpRequestsLoaded();
        assertAssociateButtonState(true);
    }
    private void checkServiceModelInfo() {
        Wait.angularHttpRequestsLoaded();
        //Service name
        String elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.SERVIICE_NAME_KEY;
        String infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"Demo Service 1");
        //model name
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.MODEL_NAME;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"Test Pnf");
        //service instance name
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.SERVICE_INSTANCE_NAME;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"vid-test-444");
        //Model Invariant UUID
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.MODEL_INVARIANT_UUID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"61eba322-c758-48f6-8942-1a7625aaaffb");
        //ubscriber NameModel Invariant UUID
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.SUBSCRIBER_NAME_KEY;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"USP VOICE");
        //Model Version
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.MODEL_VERSION;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"0.1");
        //Model UUID
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.MODEL_UUID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"089b1c03-ff6b-4914-8c20-a7de3d375e8d");
        //Model Customization UUID
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.MODEL_CUSTOMIZATION_UUID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,"cabf6d26-c362-4444-ba06-f850e8af2d35");
        //Resource Name
        elementTestId = Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX + Constants.ServiceModelInfo.RESOURCE_NAME;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE,elementTestId),infoItemText,pnfModelName);
     }

    private void checkPnfProperties() {
        //Pnf Instance unique name
        String elementTestId = Constants.PnfAssociation.PNF_INSTANCE_NAME_TEST_ID;
        String infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,pnfInstanceName);
        //Pnf Instance name2
        elementTestId = Constants.PnfAssociation.PNF_INSTANCE_NAME2_TEST_ID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,"MX_960-F722-name-2");
        //Pnf Instance name2 source
        elementTestId = Constants.PnfAssociation.PNF_INSTANCE_NAME2_SOURCE_TEST_ID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,"MX_960-F722-name-2-source");
        //Pnf Instance Id
        elementTestId = Constants.PnfAssociation.PNF_INSTANCE_ID_TEST_ID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,"MX_960-F722-id");
        //Pnf Instance Equipment type
        elementTestId = Constants.PnfAssociation.PNF_INSTANCE_EQUIP_TYPE_TEST_ID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,"Switch");
        //Pnf Instance Equipment vendor
        elementTestId = Constants.PnfAssociation.PNF_INSTANCE_EQUIP_VENDOR_TEST_ID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,"Cisco");
        //Pnf Instance Equipment model
        elementTestId = Constants.PnfAssociation.PNF_INSTANCE_EQUIP_MODEL_TEST_ID;
        infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertEquals(String.format(Constants.PnfAssociation.PNF_INSTANCE_ERROR_MESSAGE,elementTestId),infoItemText,"ASR1002-X");
    }



}
