package vid.automation.test.utils;

import org.onap.sdc.ci.tests.datatypes.Configuration;
import vid.automation.test.infra.FeaturesTogglingConfiguration;

public class TestConfigurationHelper {

    private TestConfigurationHelper(){}

    public static Configuration getEnvConfiguration() {

        try {
            String envUrl = System.getProperty("ENV_URL");
            boolean isCustomLogin = Boolean.parseBoolean(System.getProperty("CUSTOM_LOGIN"));

            Configuration configuration = new Configuration(envUrl, isCustomLogin);

            String geckoDriverPath = System.getProperty("GECKO_PATH");
            if(geckoDriverPath == null){
                throw new RuntimeException("Missing path to gecko driver." +
                        " Make sure to provide path to the gecko driver executable with -DGECKO_PATH=<path>");
            }

            configuration.setGeckoDriverPath(geckoDriverPath);
            FeaturesTogglingConfiguration.initializeFeatureManager();
            return configuration;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
