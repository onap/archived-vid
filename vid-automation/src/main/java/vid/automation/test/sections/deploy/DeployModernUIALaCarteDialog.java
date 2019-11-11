package vid.automation.test.sections.deploy;

import static org.testng.AssertJUnit.assertFalse;

public class DeployModernUIALaCarteDialog extends DeployModernUIBase {

    @Override
    public void assertDialog() {
        super.assertDialog();
        assertFalse(isLcpRegionExist());
    }
}
