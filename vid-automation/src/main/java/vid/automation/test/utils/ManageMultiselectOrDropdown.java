package vid.automation.test.utils;
import java.util.List;
import vid.automation.test.infra.SelectOption;

 public  class ManageMultiselectOrDropdown {
    private boolean isMultiSelect;

    public ManageMultiselectOrDropdown(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
    }

    public void selectItemByDataTestId(String dataTestId, List<String> options){
        if (isMultiSelect) {
            SelectOption.selectOptionsFromMultiselectById(dataTestId, options);
        } else {
            SelectOption.byValue(options.get(0), dataTestId);
        }
    }
}
