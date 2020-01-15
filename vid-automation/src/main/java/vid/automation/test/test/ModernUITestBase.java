package vid.automation.test.test;

import static vid.automation.test.infra.ModelInfo.pasqualeVmxVpeBvService488Annotations;

import com.google.common.collect.ImmutableList;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.sections.ViewEditPage;

public class ModernUITestBase extends VidBaseTestCase {

    protected ViewEditPage viewEditPage = new ViewEditPage();

    protected void prepareServicePreset(ModelInfo modelInfo, boolean deploy) {
        String subscriberId = "e433710f-9217-458d-a79d-1c7aff376d89";

        if (deploy) {
            registerExpectationForServiceDeployment(
                ImmutableList.of(
                    modelInfo,
                    pasqualeVmxVpeBvService488Annotations
                ),
                subscriberId, null);
        } else {
            registerExpectationForServiceBrowseAndDesign(ImmutableList.of(modelInfo), subscriberId);
        }
    }

    @NotNull
    protected String uuid() {
        return UUID.randomUUID().toString();
    }
}
