package vid.automation.test.infra;


import org.apache.commons.io.FileUtils;
import org.togglz.core.context.StaticFeatureManagerProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.file.FileBasedStateRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.apache.commons.codec.Charsets.UTF_8;


public class FeaturesTogglingConfiguration {

    private static FeatureManager createFeatureManager() {
        return new FeatureManagerBuilder()
                .featureEnum(Features.class)
                .stateRepository(getStateRepository())
                .build();
    }

    public static void initializeFeatureManager(){
        StaticFeatureManagerProvider.setFeatureManager(createFeatureManager());
        for (Features feature : Features.values()) {
            System.out.println("FeaturesTogglingConfiguration: " + feature.name() + ": " + feature.isActive());
        }
    }

    private static StateRepository getStateRepository() {

        final URL propertiesAsResource = FeaturesTogglingConfiguration.class.getClassLoader().getResource("features.properties");

        final String featuresFile =
                System.getProperty(
                        "FEATURES_FILE",
                        propertiesAsResource != null ? propertiesAsResource.getFile() : null
                );

        System.out.println("features file: " + featuresFile);
        try {
            System.out.println(FileUtils.readFileToString(new File(featuresFile), UTF_8));
        } catch (IOException e) {
            // YOLO
        }
        return new FileBasedStateRepository(new File(featuresFile));
    }
}
