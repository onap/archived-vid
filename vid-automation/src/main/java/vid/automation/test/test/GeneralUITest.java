package vid.automation.test.test;

import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;
import vid.automation.test.sections.SideMenu;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeneralUITest extends VidBaseTestCase {

    @Test
    public void testHeaderContainerDisplayed() {
        SideMenu.navigateToWelcomePage();
        assertThat(Get.byId(Constants.bugFixes.HEADER_CONTAINER).isDisplayed(), is(false));
    }

}
