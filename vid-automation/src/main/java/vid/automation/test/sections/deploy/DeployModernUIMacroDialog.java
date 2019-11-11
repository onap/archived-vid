package vid.automation.test.sections.deploy;

import static org.testng.Assert.assertTrue;

public class DeployModernUIMacroDialog extends DeployModernUIBase {

    @Override
    public void assertDialog() {
        super.assertDialog();
        assertTrue(isLcpRegionExist());
    }

}
