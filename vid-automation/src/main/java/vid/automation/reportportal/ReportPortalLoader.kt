package vid.automation.reportportal

import org.apache.commons.lang3.reflect.MethodUtils
import org.apache.commons.proxy.factory.javassist.JavassistProxyFactory
import org.apache.commons.proxy.invoker.NullInvoker
import org.openqa.selenium.WebDriver
import org.testng.*
import org.testng.internal.IResultListener2


private val proxyFactory = JavassistProxyFactory() // can proxy a class without an interface

private val instance: Any by lazy {
    return@lazy try {
        val classToLoad = Class.forName("com.att.automation.common.report_portal_integration.listeners.ReportPortalListener", true, ReportPortalListenerDelegator::class.java.classLoader)
        classToLoad.getConstructor().newInstance()
    } catch (e: ClassNotFoundException) {
        proxyFactory.createInvokerProxy(NullInvoker(), arrayOf<Class<*>>(
                IExecutionListener::class.java, ISuiteListener::class.java, IResultListener2::class.java
        ))
    }
}

fun setScreenShotsWebDriver(driver: WebDriver) {
    val classToLoad: Class<*>
    try {
        classToLoad = Class.forName("com.att.automation.common.report_portal_integration.screenshots.WebDriverScreenshotsProvider", true, driver.javaClass.classLoader)
    } catch (e: ClassNotFoundException) {
        return // if class not found, don't bother
    }

    val screenshotsProvider = classToLoad.getDeclaredConstructor(WebDriver::class.java).newInstance(driver)

    MethodUtils.invokeStaticMethod(instance.javaClass, "setScreenShotsProvider", screenshotsProvider)
}

class ReportPortalListenerDelegator : IExecutionListener, ISuiteListener, IResultListener2 {

    private val iExecutionListener: IExecutionListener by lazy {
        instance as IExecutionListener
    }


    override fun onExecutionFinish() = iExecutionListener.onExecutionFinish()

    override fun onExecutionStart() = iExecutionListener.onExecutionStart()



    private val iSuiteListener: ISuiteListener by lazy {
        instance as ISuiteListener
    }

    override fun onFinish(suite: ISuite?) = iSuiteListener.onFinish(suite)

    override fun onStart(suite: ISuite?) = iSuiteListener.onStart(suite)



    private val iResultListener2: IResultListener2 by lazy {
        instance as IResultListener2
    }

    override fun onFinish(context: ITestContext?) = iResultListener2.onFinish(context)

    override fun onStart(context: ITestContext?) = iResultListener2.onStart(context)

    override fun onTestSkipped(result: ITestResult?) = iResultListener2.onTestSkipped(result)

    override fun onTestSuccess(result: ITestResult?) = iResultListener2.onTestSuccess(result)

    override fun onTestFailure(result: ITestResult?) = iResultListener2.onTestFailure(result)

    override fun onConfigurationSkip(itr: ITestResult?) = iResultListener2.onConfigurationSkip(itr)

    override fun onConfigurationFailure(itr: ITestResult?) = iResultListener2.onConfigurationFailure(itr)

    override fun beforeConfiguration(tr: ITestResult?) = iResultListener2.beforeConfiguration(tr)

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult?) = iResultListener2.onTestFailedButWithinSuccessPercentage(result)

    override fun onConfigurationSuccess(itr: ITestResult?) = iResultListener2.onConfigurationSuccess(itr)

    override fun onTestStart(result: ITestResult?) = iResultListener2.onTestStart(result)
}
