package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;

public class PnfSearchAssociationPage extends VidBasePage {
    public PnfSearchAssociationPage setPnfName(String pnfName){
        setInputText(Constants.PnfAssociation.PNF_NAME_TEST_ID, pnfName);
        return this;
    }
    public PnfSearchAssociationPage clickSearchButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.PnfAssociation.SEARCH_PNF_TEST_ID, 60);
        return this;
    }
    public PnfSearchAssociationPage clickAssociateButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.PnfAssociation.ASSOCIATE_PNF_TEST_ID, 100);
        return this;
    }



}
