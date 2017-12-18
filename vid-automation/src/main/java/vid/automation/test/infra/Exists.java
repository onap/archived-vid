package vid.automation.test.infra;

import org.openqa.selenium.NoSuchElementException;
import vid.automation.test.Constants;

public class Exists {
    public static boolean byId(String id) {
        return Get.byId(id) != null;
    }

    public static boolean byClass(String className) {
        return Get.byClass(className) != null;
    }

    public static boolean modal() {
        try {
            return Get.byCssSelector(Constants.Modals.modalClass) != null;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }
}
