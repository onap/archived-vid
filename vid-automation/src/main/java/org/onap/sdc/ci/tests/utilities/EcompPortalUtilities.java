package org.onap.sdc.ci.tests.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class EcompPortalUtilities {

	public static void swichFrames(By by){
		WebElement appImage = GeneralUIUtils.getClickableButtonBy(by, 3 * 60);
		appImage.click();
		GeneralUIUtils.getDriver().switchTo().frame(1);
    	GeneralUIUtils.waitForBackLoader();
		GeneralUIUtils.waitForAngular();
		GeneralUIUtils.getWebElementByClassName("applicationWindow");
	}

}
