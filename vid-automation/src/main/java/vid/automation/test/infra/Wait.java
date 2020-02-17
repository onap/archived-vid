package vid.automation.test.infra;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import vid.automation.test.Constants;

public class Wait {
    public static boolean byText(String text) {
        return GeneralUIUtils.findAndWaitByText(text, Constants.generalTimeout);
    }

    public static <T> boolean waitFor(Predicate<T> predicate, T input, int numOfRetries, int interval, TimeUnit intervalUnit) {
        Throwable lastError = null;
        for (int i=0; i<numOfRetries; i++) {
            try {
                if (predicate.test(input)) {
                    return true;
                }
            }
            catch (Throwable t) {
                lastError = t;
                System.out.println(String.format("a retry failed due to: %s %s", t, t.getMessage()));
            }
            try {
                intervalUnit.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lastError != null) {
            throw ExceptionUtils.<RuntimeException>rethrow(lastError);
        } else {
            return false;
        }
    }

    public static <T> boolean waitFor(Predicate<T> predicate, T input, int numOfRetries, int interval) {
        return waitFor(predicate, input, numOfRetries, interval, TimeUnit.SECONDS);
    }

    public static boolean waitByClassAndText(String className, String text, int timeoutInSeconds) {
        return waitFor((x->Get.byClassAndText(className, text, 1)!=null), null, timeoutInSeconds, 1);
    }

    public static boolean waitByClassAndTextXpathOnly(String className, String text, int timeoutInSeconds) {
        try {
            return CollectionUtils.isNotEmpty(
                    GeneralUIUtils.getWebElementsListByContainsClassNameAndText(className, text, timeoutInSeconds));
        }
        catch (RuntimeException exception) {
            System.out.println(
                String.format("Failed to waitByClassAndText, after %d seconds. Class name: %s, Text: %s. Cause: %s",
                    timeoutInSeconds, className, text, exception.toString()));
            return false;
        }
    }

    public static boolean waitByTestId(String dataTestId,  int timeoutInSeconds) {
        return waitFor((x->Get.byTestId(dataTestId)!=null),null, timeoutInSeconds, 1);
    }

    public static boolean waitByIdAndText(String id,  String text, int timeoutInSeconds) {
        return waitFor((x->Get.byId(id).getText().equals(text)),null, timeoutInSeconds, 1);
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
