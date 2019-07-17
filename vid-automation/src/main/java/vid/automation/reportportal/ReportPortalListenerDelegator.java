package vid.automation.reportportal;

import static org.apache.commons.beanutils.MethodUtils.invokeStaticMethod;

import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.factory.javassist.JavassistProxyFactory;
import org.apache.commons.proxy.invoker.NullInvoker;
import org.openqa.selenium.WebDriver;
import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;

/**
 * Loads and delegates to ReportPortalListener. When class not found -- yields no-op object, and no side-effect.
 */
public class ReportPortalListenerDelegator implements IExecutionListener, ISuiteListener, IResultListener2 {

    private static final String CLASSNAME_REPORT_PORTAL_LISTENER = "com.att.automation.common.report_portal_integration.listeners.ReportPortalListener";
    private static final String CLASSNAME_WEB_DRIVER_SCREENSHOTS_PROVIDER = "com.att.automation.common.report_portal_integration.screenshots.WebDriverScreenshotsProvider";

    private static final Object instance = createReportPortalListener();

    private final IExecutionListener iExecutionListener;
    private final ISuiteListener iSuiteListener;
    private final IResultListener2 iResultListener2;

    public ReportPortalListenerDelegator() {
        iExecutionListener = ((IExecutionListener) instance);
        iSuiteListener = ((ISuiteListener) instance);
        iResultListener2 = ((IResultListener2) instance);
    }

    public static void setScreenShotsWebDriver(WebDriver driver) {
        try {
            invokeStaticMethod(instance.getClass(), "setScreenShotsProvider", createScreenshotsProvider(driver));
        } catch (ClassNotFoundException e) {
            // if class not found, don't bother
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void beforeConfiguration(ITestResult tr) {
        iResultListener2.beforeConfiguration(tr);
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        iResultListener2.onConfigurationSuccess(itr);
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        iResultListener2.onConfigurationFailure(itr);
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        iResultListener2.onConfigurationSkip(itr);
    }

    @Override
    public void onExecutionStart() {
        iExecutionListener.onExecutionStart();
    }

    @Override
    public void onExecutionFinish() {
        iExecutionListener.onExecutionFinish();
    }

    @Override
    public void onStart(ISuite suite) {
        iSuiteListener.onStart(suite);

    }

    @Override
    public void onFinish(ISuite suite) {
        iSuiteListener.onFinish(suite);
    }

    @Override
    public void onTestStart(ITestResult result) {
        iResultListener2.onTestStart(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        iResultListener2.onTestSuccess(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        iResultListener2.onTestFailure(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        iResultListener2.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        iResultListener2.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onStart(ITestContext context) {
        iResultListener2.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        iResultListener2.onFinish(context);
    }


    private static Object createReportPortalListener() {
        try {
            final Class<?> classToLoad = Class.forName(CLASSNAME_REPORT_PORTAL_LISTENER,
                true, ReportPortalListenerDelegator.class.getClassLoader());
            return classToLoad.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            // Fallback to NullInvoker
            final Class[] classes = {IExecutionListener.class, ISuiteListener.class, IResultListener2.class};
            final ProxyFactory proxyFactory = new JavassistProxyFactory();

            return proxyFactory.createInvokerProxy(new NullInvoker(), classes);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object createScreenshotsProvider(WebDriver driver) throws ReflectiveOperationException {
        Class<?> classToLoad = Class.forName(CLASSNAME_WEB_DRIVER_SCREENSHOTS_PROVIDER,
            true, driver.getClass().getClassLoader());

        return classToLoad.getDeclaredConstructor(WebDriver.class).newInstance(driver);
    }

}
