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

package org.onap.sdc.ci.tests.utilities;

import org.onap.sdc.ci.tests.datatypes.DataTestIdEnum;
import org.onap.sdc.ci.tests.execute.setup.SetupCDTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.Status;

public final class FindUtils {

	
	public static void findComponentAndClick(String componentName) throws Exception {
		SetupCDTest.getExtendTest().log(Status.INFO, "finding component " + componentName);
		GeneralUIUtils.getWebElementByTestID(DataTestIdEnum.MainMenuButtons.SEARCH_BOX.getValue()).sendKeys(componentName);
		WebElement foundComp = null;
		try {
			foundComp = GeneralUIUtils.getWebElementByTestID(componentName);
			foundComp.click();
			GeneralUIUtils.waitForLoader();
			GeneralUIUtils.getWebElementByTestID(DataTestIdEnum.GeneralElementsEnum.LIFECYCLE_STATE.getValue());
		} catch (Exception e) {
			String msg = String.format("DID NOT FIND A COMPONENT NAMED %s", componentName);
			SetupCDTest.getExtendTest().log(Status.FAIL, msg);
			System.out.println(msg);
			Assert.fail(msg);
		}
	}
	
	

}
