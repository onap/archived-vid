package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.UnhandledAlertException;
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

    public static void navigateToMacroInstantiationStatus() {
        navigateToPage("Macro Instantiation Status");
        new VidBasePage().goToIframe();
    }

    private static void navigateToPage(String PageName) {

        boolean findAndWaitByText = GeneralUIUtils.findAndWaitByText(PageName, 30);

        if (!findAndWaitByText) {
            doEvenIfAlertIsShown(SideMenu::navigateToWelcomePage);
            findAndWaitByText = GeneralUIUtils.findAndWaitByText(PageName, 10);
        }

        Assert.assertTrue(findAndWaitByText);
        doEvenIfAlertIsShown(() -> {
            try {
                GeneralUIUtils.clickOnElementByText(PageName, 50);
            } catch (ElementClickInterceptedException e) {
                navigateToWelcomePage();
                GeneralUIUtils.clickOnElementByText(PageName, 100);
            }
        });
        GeneralUIUtils.ultimateWait();
    }

    public static void navigateToWelcomePage() {
        doEvenIfAlertIsShown(() -> {
            VidBasePage base = new VidBasePage();
            base.navigateTo("welcome.htm");
        });
    }

    private static void doEvenIfAlertIsShown(Runnable runnable) {
        try {
            runnable.run();
        } catch (UnhandledAlertException e) {
            // an alert popup was shown; dismiss it if it's still there
            try {
                GeneralUIUtils.getDriver().switchTo().alert().dismiss();
            } catch (org.openqa.selenium.NoAlertPresentException e2) {
                // YOLO
            }
            runnable.run();
        }
    }
}
