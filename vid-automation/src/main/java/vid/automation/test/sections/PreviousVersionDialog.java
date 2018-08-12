package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import vid.automation.test.infra.Get;

public class PreviousVersionDialog extends VidBasePage{

    public void assertPreviousVersionButtonExists(String expectedInvariantUUID){

        WebElement viewPreviousButtonWebElement = GeneralUIUtils.getWebElementByTestID("Invariant-" + expectedInvariantUUID);
        Assert.assertEquals(expectedInvariantUUID, viewPreviousButtonWebElement.getText());
    }

    public void assertVersionRow(String expectedInvariantId, String expectedmodelVersionId, String expectedVersionId , String testId){

        WebElement invariantUUIDTableCell = GeneralUIUtils.getWebElementByTestID(testId + expectedmodelVersionId);
        String modelVersionId = ((RemoteWebElement) invariantUUIDTableCell).findElementsByTagName("td").get(1).getText();
        String invariantId = ((RemoteWebElement) invariantUUIDTableCell).findElementsByTagName("td").get(2).getText();
        String versionId = ((RemoteWebElement) invariantUUIDTableCell).findElementsByTagName("td").get(4).getText();
        Assert.assertEquals(expectedmodelVersionId, modelVersionId);
        Assert.assertEquals(expectedInvariantId, invariantId);
        Assert.assertEquals(expectedVersionId, versionId);
    }

    public void assertHighestVersionNotExists( String expectedmodelVersionId){

        Assert.assertNull(Get.byTestId("Previous-version-pop-up-uuid-" + expectedmodelVersionId));

        }
    }

