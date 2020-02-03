package vid.automation.test.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.model.User;
import vid.automation.test.sections.SearchExistingPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

public class SearchExistingInstanceTest extends VidBaseTestCase {

    public static final String serviceIdOeWirelineProjectX1 = "7e4f8130-5dee-47c4-8770-1abc5f5ded83";
    public static final String serviceIdOeWirelineProjectYYY1 = "13695dfb-db99-4c2f-905e-fe7bf2fc7b9f";

    @BeforeClass
    public void registerToSimulatorAndLogin() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        BulkRegistration.searchExistingServiceInstanceByOEAndProject();
    }

    @Test
    public void testSearchExistingInstanceByOwningEntitySingleValue() {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchByOwningEntity("Melissa");
        searchExistingPage.clickSubmitButton();
        GeneralUIUtils.ultimateWait();
        assertTrue(searchExistingPage.checkEditOrViewExistsByInstanceId(serviceIdOeWirelineProjectX1));
        assertTrue(searchExistingPage.checkEditOrViewExistsByInstanceId(serviceIdOeWirelineProjectYYY1));
    }

    @Test(groups = { "shouldBeMigratedToWorkWithSimulator" })
    private void testSearchExistingInstanceByOwningEntityMultiValue() throws Exception {
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_OWNING_ENTITY_ID);
        Thread.sleep(1000);
        Click.byText("owning-entity-SDN-RXU4");
        Click.byText("owning-entity-SDN-LT1");

        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("SDN-LT1-B-servInstance-E1802");
    }

    @Test
    private void testSearchExistingInstanceByProjectSingleValue() {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchByProject("x1");
        searchExistingPage.clickSubmitButton();
        GeneralUIUtils.ultimateWait();
        assertTrue(searchExistingPage.checkEditOrViewExistsByInstanceId(serviceIdOeWirelineProjectX1));
        assertFalse(searchExistingPage.checkEditOrViewExistsByInstanceId(serviceIdOeWirelineProjectYYY1));
    }

    @Test
    private void testSearchExistingInstanceByProjectAndOwningEntity() {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchByProject("yyy1");
        searchExistingPage.searchByOwningEntity("Melissa");
        searchExistingPage.clickSubmitButton();
        GeneralUIUtils.ultimateWait();
        assertTrue(searchExistingPage.checkEditOrViewExistsByInstanceId(serviceIdOeWirelineProjectYYY1));
        assertFalse(searchExistingPage.checkEditOrViewExistsByInstanceId(serviceIdOeWirelineProjectX1));
    }


    @Test(groups = { "shouldBeMigratedToWorkWithSimulator" })
    private void testSearchExistingInstanceByProjectWithSpecialCharacters() throws Exception {
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_PROJECT_ID);
        Thread.sleep(1000);
        Click.byText("VIP(VelocitytoIP)");//must be in DB
        Click.byId(Constants.EditExistingInstance.SELECT_PROJECT_ID);
        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("c8a85099-e5a3-4e4d-a75d-afa3e2ed2a94");
    }


    @Test(groups = { "shouldBeMigratedToWorkWithSimulator" })
    private void testSearchExistingInstanceByProjectMultiValue() throws Exception {
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_PROJECT_ID);
        Thread.sleep(1000);
        Click.byText("x1");
        Click.byText("x3");
        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("SDN-LT1-B-servInstance-E1802");
    }
}
