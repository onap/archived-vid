package vid.automation.test.test;

import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.execute.setup.DriverFactory;
import vid.automation.test.utils.TestConfigurationHelper;

public abstract class VidBaseTestWithoutLogin extends DriverFactory {

    @Override
    protected UserCredentials getUserCredentials() {
        return null;
    }

    @Override
    protected org.openecomp.sdc.ci.tests.datatypes.Configuration getEnvConfiguration() {

        return TestConfigurationHelper.getEnvConfiguration();
    }
}
