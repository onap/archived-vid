/*-
 * ============LICENSE_START=======================================================
 * VID
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

package org.openecomp.ecomp.vid.selenium;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * The Class LogOutLeftPane.
 */
@Test(enabled=true)
public class LogOutLeftPane {

	/** The login button. */
	WebElement loginButton;
	
	/** The login. */
	WebElement login;
	
	/** The pwd. */
	WebElement pwd;
	
	/** The log. */
	Logger log;
	
	/** The errormessage. */
	WebElement errormessage;
	
	/** The driver. */
	WebDriver driver=new FirefoxDriver();
	
	
	/** The config prop. */
	private final Properties configProp = new Properties();
	
	
	/**
	 * Instantiates a new log out left pane.
	 */
	private  LogOutLeftPane() {
		// TODO Auto-generated constructor stub
		//
		try{
		//	InputStream input =this.getClass().getClassLoader().getResourceAsStream("objectmap.properties");
		//FileInputStream input1 = new FileInputStream("objectmap.properties");
			
		InputStream input =new FileInputStream("objectconfig.properties");
		System.out.println("Read all properties from file");
		configProp.load(input);
		System.out.println("Read all properties from file completed");
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	   
	   
	/**
	 * Do before test.
	 */
	// TODO Auto-generated method stub
	@BeforeTest
	public void doBeforeTest()
	{
		//WebDriver driver=new FirefoxDriver();
		
		log = Logger.getLogger(LogOutLeftPane.class.getName());
	
	
	
	// Get url
	driver.get("http://vid.openecomp.org:9080/vid/login_external.htm");
	driver.manage().window().maximize();
	
	
	 login = driver.findElement(By.xpath("//input[@class='fn-ebz-text ng-pristine ng-valid']"));
	 pwd = driver.findElement(By.xpath("//input[@class='span3 ng-pristine ng-valid']"));
	 loginButton = driver.findElement(By.id("loginBtn"));
	}
	
	
	/**
	 * Expand collapse panel.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test(priority=1)
	public void expandCollapsePanel() throws InterruptedException
	{

		
		
		login.clear();
		login.sendKeys("su");
		pwd.clear();
		pwd.sendKeys("fusion");
		//driver.findElement(By.partialLinkText("Click here to login")).click();
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		loginButton.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		
		log.info("Clicking Profile link from left pane");
		Thread.sleep(3000);
		System.out.println("properties file details --->"+configProp.getProperty("profilelink"));
		driver.findElement(By.xpath(configProp.getProperty("profilelink"))).click();
		Thread.sleep(3000);
		
		//Verify whether the sub panel is displayed
		//To verify the following :Search import from webphone and Self 
		driver.findElement(By.xpath(".//*[@id='panel4']")).isDisplayed();
		log.info("Expand and collapse passed for Profile link");
		
		//For Admin
		//Verify expand and collapse working for ADMIN
		log.info("Clicking Admin link from left pane");
		driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[6]/a/span")).click();
		//To verify the following: Roles, Roles Functions, Usages
		driver.findElement(By.xpath(".//*[@id='panel5']")).isDisplayed();
		
	
		log.info("Expand and collapse passed for ADMIN link");

		log.info("VID-11 TC-3 PASSED");
		
	}
	
	
	/**
	 * Drop down list.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test(priority=2)
	public void dropDownList() throws InterruptedException
	{
		//VID-12 TC-3
		log.info("VID-12 TC-3");
		//driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[2]/div/select"));
		
		//driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[2]/td[2]/div/select"));
		Thread.sleep(5000);
		
		//Infrastructure Subscriber Name
		Select oSelect = new Select(driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[2]/div/select")));
		Select iSelect = new Select(driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[2]/td[2]/div/select")));
		
		List <WebElement> elementCount = oSelect.getOptions();
		log.info("Subscriber Name Drop Down");
		System.out.println(elementCount.size());
		oSelect.selectByIndex(1);
		log.info("Subscriber name selected");
		//String selectedOption = new Select(driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[2]/div/select"))).getFirstSelectedOption().getText();
		
		
		List <WebElement> count = iSelect.getOptions();
		log.info("Subscriber type drop down");
		System.out.println(count.size());
		oSelect.selectByIndex(1);
		log.info("Subscriber type selected");
		

				
		//Submit button is clicked
		driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[3]/td/div/button")).click();
		
		
		//Verify whether the page header is displayed "Selected Subscriber's Service Instance Details:"
		driver.findElement(By.xpath(".//*[@id='mContent']/div/div/div/h1")).isDisplayed();
		log.info("VID-12 TC-3 PASSED");
	}
	
	
	
	
	/**
	 * Logout left pane.
	 */
	@Test(priority=3)
	public void logoutLeftPane()
	{
		//To Verify if the logout link redirects to Login page when clicked.
		
		
		/*log.info("----------------VID-11 TC-4----------------");
		login.clear();
		login.sendKeys("ss749s");
		pwd.clear();
		pwd.sendKeys("abc123");
		//driver.findElement(By.partialLinkText("Click here to login")).click();
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		loginButton.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);*/
		log.info("----------------VID-11 TC-4----------------");
		driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[7]/a")).click();
		//Validate that the user has logged out of VID. Displays "Portal"
		Assert.assertTrue(driver.getPageSource().contains("Portal"));
		
		log.info("VID 11 TC-4 PASSED");
		
		driver.close();
		
	}
	
}
