package vid.automation.test.test;

import org.onap.sdc.ci.tests.datatypes.Configuration;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.execute.setup.DriverFactory;
import vid.automation.test.utils.TestConfigurationHelper;

public abstract class VidBaseTestWithoutLogin extends DriverFactory {

    @Override
    protected UserCredentials getUserCredentials() {
        return null;
    }

    @Override
    protected Configuration getEnvConfiguration() {

        return TestConfigurationHelper.getEnvConfiguration();
    }
}
