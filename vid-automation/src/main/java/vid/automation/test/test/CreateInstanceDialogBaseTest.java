package vid.automation.test.test;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.model.Service;

public class CreateInstanceDialogBaseTest extends VidBaseTestCase {
    void assertServiceMetadata(String expectedMetadata, String actualMetadata) {
        WebElement serviceNameElem = GeneralUIUtils.getWebElementByTestID(actualMetadata);
        String actualServiceName = serviceNameElem.getText();
        Assert.assertEquals(expectedMetadata, actualServiceName);
    }

    void validateServiceCreationDialog(Service expectedService) {
        assertThatServiceCreationDialogIsVisible();
        assertServiceMetadata(expectedService.name, Constants.SERVICE_NAME);
        assertServiceMetadata(expectedService.uuid, Constants.SERVICE_UUID);
        assertServiceMetadata(expectedService.invariantUuid, Constants.SERVICE_INVARIANT_UUID);
        assertServiceMetadata(expectedService.category, Constants.SERVICE_CATEGORY);
        assertServiceMetadata(expectedService.version, Constants.SERVICE_VERSION);
        assertServiceMetadata(expectedService.description, Constants.SERVICE_DESCRIPTION);
    }

    void assertThatServiceCreationDialogIsVisible() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.BrowseASDC.CREATE_SERVICE_INSTANCE, 15);
        Assert.assertTrue(byText);
    }

    void assertSuccessfulServiceInstanceCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.BrowseASDC.SERVICE_INSTANCE_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.BrowseASDC.SERVICE_INSTANCE_CREATION_FAILED_MESSAGE, byText);
    }
}
