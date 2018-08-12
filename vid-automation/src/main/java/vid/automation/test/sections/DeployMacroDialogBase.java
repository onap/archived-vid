package vid.automation.test.sections;

public abstract class DeployMacroDialogBase extends VidBasePage {

    public abstract void assertTitle();
    public abstract void closeDialog();
    public abstract void assertDialogExists();
    public abstract void clickOwningEntitySelect();
    public abstract void clickProjectSelect();


}
