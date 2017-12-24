package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import vid.automation.test.Constants;

public class SideMenu {
    public static void navigateToBrowseASDCPage() {
        navigateToPage(Constants.SideMenu.BROWSE_ASDC_SERVICE_MODELS);
    }

    public static void navigateToSearchExistingPage() {
        navigateToPage(Constants.SideMenu.SEARCH_EXISTING_SERVICE);
    }

    public static void navigateToCreateNewServicePage() {
        navigateToPage(Constants.SideMenu.CREATE_NEW_SERVICE);
    }

    public static void navigateToTestEnvironmentsPage() {
        navigateToPage(Constants.SideMenu.TEST_ENVIRONMENTS);
    }

    private static void navigateToPage(String PageName) {
        boolean findAndWaitByText = GeneralUIUtils.findAndWaitByText(PageName, 30);
        Assert.assertTrue(findAndWaitByText);
        GeneralUIUtils.clickOnElementByText(PageName, 30);
        GeneralUIUtils.ultimateWait();
    }
}
