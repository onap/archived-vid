package vid.automation.test.sections;

public abstract class DeployDialogBase extends VidBasePage {

    public abstract void closeDialog();

    public abstract void assertDialog();

    public abstract void waitForDialogToLoad();
}
