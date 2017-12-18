package vid.automation.test.infra;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;

public class Wait {
    public static boolean byText(String text) {
        return GeneralUIUtils.findAndWaitByText(text, Constants.generalTimeout);
    }

    public static void angularHttpRequestsLoaded() {
        JavascriptExecutor js = (JavascriptExecutor) GeneralUIUtils.getDriver();
        for (int i=0; i<Constants.generalRetries; i++) {
            Object result = js.executeScript("return window.angular.element('body').injector().get('$http').pendingRequests.length;");
            if(result.toString().equals("0")) {
                break;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void modalToDisappear() {
        for (int i=0; i<Constants.generalRetries; i++) {
            try {
                Object modalElement =  Get.byCssSelector(Constants.Modals.modalClass);
                if(modalElement == null) {
                    break;
                } else {
                    Thread.sleep(1000);
                }
            } catch (NoSuchElementException e) {
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void modalToBeDisplayed() {
        for (int i=0; i<Constants.generalRetries; i++) {
            try {
                Object modalElement =  Get.byCssSelector(Constants.Modals.modalClass);
                if(modalElement == null) {
                    Thread.sleep(1000);
                } else {
                    break;
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}
