package vid.automation.test.sections;

import vid.automation.test.Constants;

/**
 * Created by itzikliderman on 13/06/2017.
 */
public class BrowseASDCPage extends VidBasePage {
    public String generateInstanceName() {
        return generateInstanceName(Constants.BrowseASDC.SERVICE_INSTANCE_NAME_PREFIX);
    }
}
