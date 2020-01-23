/*-
 * ============LICENSE_START=======================================================
 * SDC
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.sdc.ci.tests.execute.setup;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import net.lightbody.bmp.core.har.Har;
import org.onap.sdc.ci.tests.datatypes.Configuration;
import org.onap.sdc.ci.tests.datatypes.User;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.datatypes.UserRoleEnum;
import org.onap.sdc.ci.tests.execute.setup.ExtentManager.suiteNameXml;
import org.onap.sdc.ci.tests.run.StartTest;
import org.onap.sdc.ci.tests.utilities.FileHandling;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public abstract class SetupCDTest extends DriverFactory {

	private static final String RE_RUN = "<html><font color=\"red\">ReRun - </font></html>";

	/**************** PRIVATES ****************/
	private static String url;

	protected static ITestContext myContext;

	/**************** METHODS ****************/
	public static ExtentTest getExtendTest() {
		return ExtentTestManager.getTest();
	}

	public static WindowTest getWindowTest() {
		return WindowTestManager.getWindowMap();
	}

	public static String getScreenshotFolder() {
		return getConfiguration().getScreenshotFolder();
	}

	public static String getHarFilesFolder() {
		return getConfiguration().getHarFilesFolder();
	}
	
	public static String getReportFolder(){
		return getConfiguration().getReportFolder();
	}
	
	public static String getReportFilename(){
		return getConfiguration().getReportFileName();
	}
	
	protected abstract UserCredentials getUserCredentials();
	protected abstract Configuration getEnvConfiguration();
	protected abstract void loginToLocalSimulator(UserCredentials userCredentials);
	
	

	/**************** BEFORE ****************/

	@BeforeSuite(alwaysRun = true)
	public void setupBeforeSuite(ITestContext context) throws Exception {
		setUrl();
		initReport(context);
	}

	private void initReport(ITestContext context) throws Exception {
		myContext = context;
		ExtentManager.initReporter(getConfiguration(), context);
	}

	@BeforeMethod(alwaysRun = true)
	public void setBrowserBeforeTest(java.lang.reflect.Method method, ITestContext context) throws Exception {
		boolean emptyDataProvider = isDataProviderEmpty(method);
		if (emptyDataProvider) {
			System.out.println("ExtentReport instance started from BeforeMethod...");
			String suiteName = ExtentManager.getSuiteName(context);
			if (suiteName.equals(suiteNameXml.TESTNG_FAILED_XML_NAME.getValue())) {
				ExtentTestManager.startTest(RE_RUN + method.getName());
			} else {
				ExtentTestManager.startTest(method.getName());
			}

			ExtentTestManager.assignCategory(this.getClass());
			setBrowserBeforeTest(getUserCredentials());
		} else {
			System.out.println("ExtentReport instance started from Test...");
		}

		getConfiguration().setWindowsDownloadDirectory(getWindowTest().getDownloadDirectory());

		if (getConfiguration().isCaptureTraffic()) {
			try {
				MobProxy.getPoxyServer().newHar(method.getName() + ".har");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isDataProviderEmpty(java.lang.reflect.Method method) {
		return method.getAnnotation(Test.class).dataProvider().isEmpty();
	}

	/**************** AFTER ****************/
	@AfterMethod(alwaysRun = true)
	public void quitAfterTest(ITestResult result, ITestContext context) throws Exception {

		try {
			ReportAfterTestManager.report(result, context);
			GeneralUIUtils.closeErrorMessage();
		} finally {

			if (getConfiguration().isCaptureTraffic()) {
//				addTrafficFileToReport(result);
			}

			ExtentTestManager.endTest();
			ExtentManager.closeReporter();
			FileHandling.cleanCurrentDownloadDir();
		}

	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		if (getConfiguration().isUseBrowserMobProxy()) {
			MobProxy.getPoxyServer().stop();
		}
	}

	protected static String setUrl() {
		url = getConfiguration().getUrl();
		if (url == null) {
			String message = "no URL found";
			System.out.println(message);
			Assert.fail(message);
		} 
		return url;
	}

	public static void navigateToUrl(String url)  {
		try {
//			System.out.println("Deleting cookies...");
//			deleteCookies();

			System.out.println("Navigating to URL : " + url);
			getDriver().navigate().to(url);
			GeneralUIUtils.waitForLoader();

			System.out.println("Zooming out...");
			GeneralUIUtils.windowZoomOutUltimate();

		} catch (Exception e) {
			String msg = "Browser is unreachable";
			System.out.println(msg);
			e.printStackTrace();
			getExtendTest().log(Status.ERROR, msg);
			Assert.fail(msg);
		}
	}

	private static void deleteCookies() throws Exception {
		getDriver().manage().deleteAllCookies();
		Thread.sleep(1000);

		int attempts = 0;
		final int max_attempts = 3;

		while (!getDriver().manage().getCookies().isEmpty() && attempts < max_attempts) {
			getExtendTest().log(Status.INFO,
					"Trying to delete cookies one more time - " + (attempts + 1) + "/" + max_attempts + "attempts");
			String deleteCookiesJS = "document.cookie.split(';').forEach(function(c) { document.cookie = c.replace(/^ +/, '').replace(/=.*/, '=;expires=' + new Date().toUTCString() + ';path=/'); });";
			((JavascriptExecutor) getDriver()).executeScript(deleteCookiesJS);
			attempts++;

			if (attempts == max_attempts) {
				String msg = "Did not delete cookies, can't login with the userId "
						+ WindowTestManager.getWindowMap().getUserCredentials().getUserId();
				System.out.println(msg);
				getExtendTest().log(Status.ERROR, msg);
				Assert.fail(msg);
			}
		}
	}

	protected void loginToSystem(UserCredentials userCredentials) throws Exception {
		if (getConfiguration().isUseCustomLogin()) {
			loginToLocalSimulator(userCredentials);
		} 
		else {
			sendUserAndPasswordKeys(userCredentials);
			WebElement submitButton = GeneralUIUtils.getWebElementBy(By.name("btnSubmit"), 30);
			submitButton.click();
			WebElement buttonOK = GeneralUIUtils.getWebElementBy(By.name("successOK"), 30);
			Assert.assertTrue(buttonOK.isDisplayed(), "OK button is not displayed.");
			buttonOK.click();
		}
		GeneralUIUtils.ultimateWait();
		getWindowTest().setUserCredentials(userCredentials);
	}

	private void setRefreshAttempts(int refreshAttempts) {
		getWindowTest().setRefreshAttempts(refreshAttempts);
	}

	private void sendUserAndPasswordKeys(UserCredentials userCredentials) {
		System.out.println("Login with the userId : " + userCredentials.getUserId());
		WebElement userNameTextbox = GeneralUIUtils.getWebElementBy(By.name("userid"));
		userNameTextbox.sendKeys(userCredentials.getUserId());
		WebElement passwordTextbox = GeneralUIUtils.getWebElementBy(By.name("password"));
		passwordTextbox.sendKeys(userCredentials.getPassword());
	}

	public void loginWithUser(UserCredentials userCredentials) {
		try {
			getExtendTest().log(Status.INFO, String.format("Login with the userId %s", userCredentials.getUserId()));
			loginToSystem(userCredentials);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			getWindowTest().setPreviousUser(getWindowTest().getUserCredentials().getUserId());
		}
	}

	private void setUser(UserCredentials userCredentials) {
		User user = new User();
		user.setUserId(userCredentials.getUserId());
		user.setFirstName(userCredentials.getFirstName());
		user.setRole(userCredentials.getRole());
		user.setLastName(userCredentials.getLastName());
		getWindowTest().setUserCredentials(userCredentials);
	}

	public User getUser() {
		return getWindowTest().getUserCredentials();
	}

	private void setBrowserBeforeTest(UserCredentials userCredentials) {
		System.out.println(String.format("Setup before test with the userId %s.", userCredentials.getUserId()));
		try {
			System.out.println("Previous userId is : " + getWindowTest().getPreviousUser() + " ; Current userId is : " + userCredentials.getUserId());
			if (!getWindowTest().getPreviousUser().toLowerCase().equals(userCredentials.getUserId())) {
				System.out.println("User IDs are different. navigating and login.");
				navigateAndLogin(userCredentials);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void navigateAndLogin(UserCredentials userCredentials)  {
		int refreshAttempts = getWindowTest().getRefreshAttempts() != 0 ? getWindowTest().getRefreshAttempts() : 0;
		setRefreshAttempts(refreshAttempts);
		setUser(userCredentials);
		navigateToUrl(url);
		loginWithUser(userCredentials);
		GeneralUIUtils.ultimateWait();
	}

	public User getUser(UserRoleEnum role) {
		User user = new User();
		user.setUserId(role.getUserId());
		user.setFirstName(role.getFirstName());
		user.setLastName(role.getLastName());
		user.setRole(role.name());
		return user;
	}

	protected void reloginWithNewRole(UserCredentials userCredentials)  {
		System.out.println(String.format("Setup before relogin with the userId %s", userCredentials.getUserId()));
		navigateAndLogin(userCredentials);
	}

	public void addTrafficFileToReport(ITestResult result) {
		try {
			// Get the HAR data
			Har har = MobProxy.getPoxyServer().getHar();
			String shortUUID = UUID.randomUUID().toString().split("-")[0];
			File harFile = new File(getHarFilesFolder() + result.getName() + shortUUID + ".har");
			new File(getHarFilesFolder()).mkdirs();

			har.writeTo(harFile);

			String pathToFileFromReportDirectory = getReportFolder() + File.separator + harFile.getName();
			ExtentTestActions.addFileToReportAsLink(harFile, pathToFileFromReportDirectory,
					"File with captured traffic");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/*
	 * * Start section of test in ExtentReport with DataProvider parameters,
	 * should be started from test method, see example in onboardVNFTest
	 */
	public void setLog(String fromDataProvider) {

		String suiteName = ExtentManager.getSuiteName(myContext);
		if (suiteName.equals(suiteNameXml.TESTNG_FAILED_XML_NAME.getValue())) {
			ExtentTestManager.startTest(RE_RUN + Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fromDataProvider);
		} else {
			ExtentTestManager.startTest(Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fromDataProvider);
		}

		getWindowTest().setAddedValueFromDataProvider(fromDataProvider);
		ExtentTestManager.assignCategory(this.getClass());
		setBrowserBeforeTest(getUserCredentials());
	}
	
	public static void main(String[] args) {
		System.out.println("---------------------");
		System.out.println("running test from CLI");
		System.out.println("---------------------");

		String testSuite = System.getProperty("testSuite");
		String[] testSuiteArr = {testSuite};
		StartTest.main(testSuiteArr);
	}
	
}
