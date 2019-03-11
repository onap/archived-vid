package org.onap.sdc.ci.tests.utilities;

import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.datatypes.UserRoleEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginUtils {

	private static final String WEB_SEAL_PASSWORD = "123123a";

	public static void loginToLocalWebsealSimulator(UserRoleEnum role) {
		WebDriver driver = GeneralUIUtils.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, 30);

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@method='" + "post" + "']"))));

		WebElement userIdTextbox = GeneralUIUtils.getWebElementBy(By.name("userId"));
		userIdTextbox.sendKeys(role.getUserId());
		WebElement passwordTextbox = GeneralUIUtils.getWebElementBy(By.name("password"));
		passwordTextbox.sendKeys(WEB_SEAL_PASSWORD);

		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@value='" + "Login" + "']")))).click();
	}
	
	public static void loginToLocalWebsealSimulator(UserCredentials user) {
		WebDriver driver = GeneralUIUtils.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, 30);

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@method='" + "post" + "']"))));

		WebElement userIdTextbox = GeneralUIUtils.getWebElementBy(By.name("userId"));
		userIdTextbox.sendKeys(user.getUserId());
		WebElement passwordTextbox = GeneralUIUtils.getWebElementBy(By.name("password"));
		passwordTextbox.sendKeys(user.getPassword());

		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@value='" + "Login" + "']")))).click();
	}
}
