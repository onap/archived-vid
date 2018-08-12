package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import vid.automation.test.Constants;
import vid.automation.test.model.Service;

/**
 * Created by itzikliderman on 13/06/2017.
 */
public class CreateNewInstancePage extends VidBasePage {

    public CreateNewInstancePage clickCloseButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.MSO_COMMIT_DIALOG_CLOSE_BUTTON, 30);
        return this;
    }

    public String generateInstanceName() {
        return generateInstanceName(Constants.CreateNewInstance.SERVICE_INSTANCE_NAME_PREFIX);
    }

}
