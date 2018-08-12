package vid.automation.test.utils;

import vid.automation.test.infra.FeaturesTogglingConfiguration;

public class TestConfigurationHelper {

    private TestConfigurationHelper(){}

    public static org.openecomp.sdc.ci.tests.datatypes.Configuration getEnvConfiguration() {

        try {
            String envUrl = System.getProperty("ENV_URL");
            boolean isCustomLogin = Boolean.parseBoolean(System.getProperty("CUSTOM_LOGIN"));

            org.openecomp.sdc.ci.tests.datatypes.Configuration configuration = new org.openecomp.sdc.ci.tests.datatypes.Configuration(envUrl, isCustomLogin);

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
