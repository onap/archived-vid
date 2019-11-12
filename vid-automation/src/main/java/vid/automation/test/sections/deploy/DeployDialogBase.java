package vid.automation.test.sections.deploy;

import vid.automation.test.sections.VidBasePage;

public abstract class DeployDialogBase extends VidBasePage {

    public abstract void closeDialog();

    public abstract void assertDialog();

    public abstract void waitForDialogToLoad();

    public void waitForDialogAssertAndClose() {
        waitForDialogToLoad();
        assertDialog();
        closeDialog();
    }

}
