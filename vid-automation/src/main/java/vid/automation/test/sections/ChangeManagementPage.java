package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;

public class ChangeManagementPage extends VidBasePage {
    public static void openChangeManagementPage() {
        Click.byText(Constants.SideMenu.VNF_CHANGES);
    }

    public static void openNewChangeManagementModal() {
        ChangeManagementPage.openChangeManagementPage();
        Click.byId(Constants.ChangeManagement.headlineNewButtonId);
    }
    public static void clickOnRefreshButton(){
        Click.byTestId("refresh-cm");
        GeneralUIUtils.ultimateWait();
    }
}
