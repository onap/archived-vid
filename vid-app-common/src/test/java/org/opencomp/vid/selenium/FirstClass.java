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

package org.opencomp.vid.selenium;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.asserts.*;

/**
 * The Class FirstClass.
 */
public class FirstClass {
	
		/** The login button. */
		WebElement loginButton;
		
		/** The eg. */
		String eg;
		
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
		 * Instantiates a new first class.
		 */
		private  FirstClass() {
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
		
		
		/*
		
		
		@BeforeClass
		public void setUp() {
			System.out.println("*******************");
			System.out.println("launching IE browser");
			System.setProperty("webdriver.ie.driver", driverPath+"IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			driver.findElement(By.className());
			driver.manage().window().maximize();
			

			 login = driver.findElement(By.xpath("//input[@class='fn-ebz-text ng-pristine ng-valid']"));
			 pwd = driver.findElement(By.xpath("//input[@class='span3 ng-pristine ng-valid']"));
			 loginButton = driver.findElement(By.id("loginBtn"));
		}
		
		
	*/
		/**
		 * Sets the up.
		 */
		// TODO Auto-generated method stub
		@BeforeClass
		public void setUp()
		{
			
						
			//WebDriver driver=new FirefoxDriver();
			
			log = Logger.getLogger(FirstClass.class.getName());
		
		
		
		// Get url
		driver.get(configProp.getProperty("baseURL"));
		driver.manage().window().maximize();
		
		
		 login = driver.findElement(By.xpath(configProp.getProperty("login")));
		 
		 pwd = driver.findElement(By.xpath(configProp.getProperty("pwd")));
		 loginButton = driver.findElement(By.id(configProp.getProperty("loginButton")));
		}
		
		
		/**
		 * Empty username password.
		 */
		@Test(priority=1)
		public void emptyUsernamePassword()
		{
		
			
		//User Name and Password field is empty
		log.info("-----VID-11 TC-8----Username and password empty");
		loginButton.click();
		errormessage=driver.findElement(By.xpath("//*[@id='errorInfo']/span"));
		String errmsg= errormessage.getText();
		//System.out.println("Error message is"+errmsg);
		//String expected = "Invaild username or password, Please try again";
		
		//Assert.assertEquals(errmsg,expected);

		Boolean str = driver.getPageSource().contains("Invalid username or password, Please try again");
		System.out.println(driver.getPageSource().contains("Invalid username or password, Please try again"));
		
		if(str==true)
		{
			log.info("Error message validated");
			log.info("VID-11 TC-8 PASSED");
			
		}else
			log.error("Failed validation");
		
		}


		
		/**
		 * Invalid user name.
		 */
		@Test(priority=2)
		public void invalidUserName()
		{
		
		log.info("-----VID-11 TC-6----Invalid Username and Valid Password");
		
		
		login.sendKeys("xxx");
		pwd.sendKeys("abc123");
		loginButton.click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String errmsg= errormessage.getText();
		String expected = "Invalid username or password, Please try again";
		Assert.assertEquals(errmsg,expected);
		//Boolean str1 = driver.getPageSource().contains("Invalid username or password, Please try again");
		//System.out.print(str1);
		
		log.info("VID-11 TC-6 PASSED");
				
		}
		
		
		/**
		 * Invalid password.
		 */
		@Test(priority=3)
		public void invalidPassword()
		{
		log.info("-----VID-11 TC-7----Valid Username and Invalid Password");
		//  Valid user name and Invalid password.
				login.clear();
				pwd.clear();
				login.sendKeys("testuser");
				pwd.sendKeys("xxx");
				loginButton.click();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
				
				String errmsg= errormessage.getText();
				String expected = "Invalid username or password, Please try again";
				Assert.assertEquals(errmsg,expected);
				
				//Boolean str2 = driver.getPageSource().contains("Invaild username or password, Please try again");
				//System.out.print(str2);
				
				log.info("VID-11 TC-7 PASSED");
		}
		
			
		/**
		 * Login successful.
		 */
		@Test(priority=4)
		public void loginSuccessful()
		{
		log.info("-----VID-11 TC-1----Valid Username and Valid Password");
		//Login with valid user name and password.
		login.clear();
		login.sendKeys("su");
		pwd.clear();
		pwd.sendKeys("fusion");
		
				
		loginButton.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(driver.getPageSource().contains("Search Existing Service Instances"));
		log.info("VID-11 TC-1 PASSED");
		
		}
		
		
		/**
		 * Verify home page elements left pane.
		 */
		@Test(priority=5)
		public void verifyHomePageElementsLeftPane()
		
		{
			
			
			log.info("VID-10 TC 1 ");
			//VID Home
			log.info("VID 11 TC-2");
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[1]/a/span")).isDisplayed();
			//Create New Service Instance
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[2]/a/span")).isDisplayed();
			
			//Browse Service Type
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[3]/a/span")).isDisplayed();
			//View Log		
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[4]/a/span")).isDisplayed();
			
			//Profile
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[5]/a/span")).isDisplayed();
			//Admin
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[6]/a/span")).isDisplayed();
			//Logout
			driver.findElement(By.xpath("html/body/div[1]/div[1]/div/div/div[2]/div/div/div[1]/div/div/accordion/div/div[7]/a/span")).isDisplayed();
			
			//Infrastructure Subscriber Name
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[1]/div/label")).isDisplayed();
			//Infrastructure Subscriber Name Select Drop down
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[2]/div/select")).isDisplayed();
			//Infrastructure Service Type
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[2]/td[1]/div/label")).isDisplayed();
			//Infrastructure Service Type Select Drop down
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[2]/td[2]/div/select")).isDisplayed();
			//Submit button 
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[3]/td/div/button")).isDisplayed();
			
			//Login Snippet Icon
			driver.findElement(By.xpath(".//*[@class='icon-user-small login-snippet-icon']")).isDisplayed();
			
			
			//String bodyText = driver.findElement(By.tagName("body")).getText();
			//Assert.assertTrue("Text not found!", bodyText.contains("Search Existing Service Instances"));
			//Assert.IsTrue(driver.getPageSource.Contains("Search Existing Service Instances"));
			 
			log.info("VID-12 TC-1");
			Assert.assertTrue(driver.getPageSource().contains("Search Existing Service Instances"));
			Assert.assertTrue(driver.getPageSource().contains("Please search by the Subscriber name or Service Type from below:"));
			log.info("VID-12 TC-1 PASSED");
			log.info("VID-10 TC 1 PASSED");
			log.info("VID-11 TC-2 PASSED");
		}
		
		
		/**
		 * Disabled submit button.
		 */
		@Test(priority=6)
		public void disabledSubmitButton()
		{
			log.info("VID-12 TC-13");
			//Assert submit button disabled.
			Assert.assertFalse(driver.findElement(By.xpath(configProp.getProperty("submitButton"))).isEnabled());
			log.info("VID-12 TC-13 PASSED");

		} 
		
		/**
		 * Default list box value.
		 */
		@Test(priority=7)
		public void defaultListBoxValue()
		{
			log.info("VID-12 TC-2");
			
			
			//WebElement subscribername =driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[2]/div/select"));
			
			Select oSelect = new Select(driver.findElement(By.xpath(configProp.getProperty("subscriberNameDropDown"))));
			Select iSelect = new Select(driver.findElement(By.xpath(configProp.getProperty("serviceTypeDropDown"))));
			
			
			WebElement ielement=iSelect.getFirstSelectedOption();
			WebElement oelement=oSelect.getFirstSelectedOption();
			String defaultsubscribername=oelement.getText();
			String defaultservicetype=ielement.getText();
			
			Assert.assertEquals(defaultsubscribername,"Select Subscriber Name");
			Assert.assertEquals(defaultservicetype,"Select Service Type");
			
		
			
			
		//Verify Select Subscriber Name isDisplayed.
		//driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[1]/td[2]/div/select/option[1]")).isSelected();
				
		//Verify Select Service Type isDisplayed.	
		//driver.findElement(By.xpath(".//*[@id='mContent']/div/div/table/tbody/tr[2]/td[2]/div/select/option[1]")).isSelected();
			
			
			log.info("VID-12 TC-2 PASSED");

		}


		
		/**
		 * Select subscriber name drop down.
		 *
		 * @throws InterruptedException the interrupted exception
		 */
		@Test(priority=8)
		public void selectSubscriberNameDropDown() throws InterruptedException
		{
			log.info("------------------VID-10 TC-2,VID-12 TC-11, VID-12 TC-9, VID 12 TC-10,VID-12 TC-6, VID 12 TC-5--------------------");
			
			
			driver.findElement(By.xpath(configProp.getProperty("subscriberNameDropDown")));
			
			driver.findElement(By.xpath(configProp.getProperty("serviceTypeDropDown")));
			Thread.sleep(5000);
			
			//Infrastructure Subscriber Name
			Select oSelect = new Select(driver.findElement(By.xpath(configProp.getProperty("subscriberNameDropDown"))));
			
			List <WebElement> elementCount = oSelect.getOptions();
			log.info("Select Element Count of Service Name");
			System.out.println(elementCount.size());
			
			
			//Verifying getInfrastructureSubscribersList
			log.info("VID-29 TC-1");
			Assert.assertTrue(elementCount.size()>0);
			log.info("VID-29 TC-1 PASSED");
			
			oSelect.selectByIndex(2);
			String selectedOption = new Select(driver.findElement(By.xpath(configProp.getProperty("subscriberNameDropDown")))).getFirstSelectedOption().getText();

			System.out.println("Service Name selected is " +selectedOption);
			log.info("VID-10 TC-2 PASSED");
					
			//Submit button is clicked
			driver.findElement(By.xpath(configProp.getProperty("submitButton"))).click();
			
			
			//Verify whether the page header is displayed "Selected Subscriber's Service Instance Details:"
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/div/h1")).isDisplayed();
			//Assert.assertTrue(driver.getPageSource().contains("Selected Subscriber's Service Instance Details:"))
			
			
			//Verify whether the page header is displayed "Global Customer ID"
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/div/div/table/thead/tr/th[2]/div")).isDisplayed();
			Assert.assertTrue(driver.getPageSource().contains("Global Customer ID"));
			Assert.assertTrue(driver.getPageSource().contains("Subscriber Name"));
			Assert.assertTrue(driver.getPageSource().contains("Service Type"));
			Assert.assertTrue(driver.getPageSource().contains("Service Instance ID"));
			
			log.info("VID-12 TC-5 PASSED");
			log.info("VID-12 TC-11 PASSED");
			
			WebElement serviceinstancetable =driver.findElement(By.xpath("//table[@class='tablesorter tablesorter-default ng-isolate-scope']"));
			
			List<WebElement> rows_table = serviceinstancetable.findElements(By.tagName("tr"));
			  //To calculate no of rows In table.
			  int rows_count = rows_table.size();
			  
			  //Loop will execute till the last row of table.
			  for (int row=0; row<rows_count; row++){
			   //To locate columns(cells) of that specific row.
			   List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
			   //To calculate no of columns(cells) In that specific row.
			   int columns_count = Columns_row.size();
			   //System.out.println("Number of cells In Row "+row+" are "+columns_count);
			   
			   //Loop will execute till the last cell of that specific row.
			   for (int column=0; column<columns_count; column++){
			    //To retrieve text from that specific cell.
			    String celtext = Columns_row.get(column).getText();
			    //System.out.println("Cell Value Of row number "+row+" and column number "+column+" Is "+celtext);
			   
			    
			    
			    //log.info("Testing Get column and row value");
			    List <WebElement> exx= rows_table.get(1).findElements(By.tagName("td"));
			     eg=Columns_row.get(2).getText();
			   // System.out.println("Cell value of row 1 and column 2 is" +eg);
			   }
			  }
		
			  
			
			
			//Verify View/Edit isDisplayed and Click
			
			driver.findElement(By.xpath("//a[@alt='View/Edit']")).isDisplayed();
			
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/div/div/table/tbody/tr[1]/td[1]/div/a")).click();
			
			log.info("User clicked View/Edit");
			
			//Verify the Subscriber Name displayed.	
			String header= driver.findElement(By.xpath("//h1[@class='heading1 ng-binding']")).getText();
			//System.out.println(header);
			
			
			if(header.contains(eg))
			{
				System.out.println("Header contains the subscriber name");
			}else
				System.out.println("Header does not contain the subscriber name");
			
			
			
			Assert.assertTrue(driver.getPageSource().contains("PerfTest Subscriber00020021"));
			log.info("VID-12 TC-6 PASSED");
			
			
				
			driver.navigate().back();
			//Cancel button isDisplayed
			driver.findElement(By.xpath("//button[@class='button button--small button--primary']")).isDisplayed();
			log.info("VID-12 TC-9 PASSED");
			
			//Cancel button is clicked
			driver.findElement(By.xpath("//button[@class='button button--small button--primary']")).click();
			log.info("Cancel button is clicked");
			
			//Verifying VID Home page is displayed
			Assert.assertTrue(driver.getPageSource().contains("Search Existing Service Instances"));
			log.info("VID-12 TC-10 PASSED");
			
			   }
		
		
		/**
		 * Refresh subscriber name.
		 */
		@Test(priority=9)
		public void refreshSubscriberName()
		{
			log.info("VID-10 TC-4");
			
			
			driver.findElement(By.xpath(configProp.getProperty("refreshButtonSubscriberName"))).isDisplayed();
			
			log.info("VID-10 TC-4 PASSED");
			
			
		}

		
		/**
		 * Select subscriber type drop down.
		 *
		 * @throws InterruptedException the interrupted exception
		 */
		@Test(priority=9)
		public void selectSubscriberTypeDropDown() throws InterruptedException
		{
			Thread.sleep(5000);
			log.info("------------------VID-10 TC-3, VID-12 TC-12,--------------------");
			//Infrastructure Subscriber Type
			Select iSelect = new Select(driver.findElement(By.xpath(configProp.getProperty("serviceTypeDropDown"))));
			
			List <WebElement> ielementCount = iSelect.getOptions();
			log.info("Select Element Count of Service type");
			System.out.println(ielementCount.size());
			iSelect.selectByIndex(1);
			
			log.info("VID-10 TC-3 PASSED");
			
						
			//Submit button is clicked
			driver.findElement(By.xpath(configProp.getProperty("submitButton"))).click();
			
			//Verify whether the page header is displayed "Selected Subscriber's Service Instance Details:"
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/div/h1")).isDisplayed();
			//Assert.assertTrue(driver.getPageSource().contains("Selected Subscriber's Service Instance Details:"))
			log.info("Page Header: Selected Subscriber's Service Instance Details");
			
			
			//Verify whether the page header is displayed "Global Customer ID"
			driver.findElement(By.xpath(".//*[@id='mContent']/div/div/div/div/table/thead/tr/th[2]/div")).isDisplayed();
			
			//Assert.assertTrue(driver.getPageSource().contains("Global Customer ID"));
			log.info("Table is displayed");
			
			log.info("VID-12 TC-12 PASSED");
			
		}
		
		
		
		/**
		 * Logout under profile.
		 */
		@Test(priority=10)
		public void logoutUnderProfile()
		{
			
			log.info("-----------VID-11 TC-5---------------------");
			//driver.findElement(By.partialLinkText("Click here to login")).click();
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
		
			driver.findElement(By.xpath(".//*[@class='icon-user-small login-snippet-icon']")).click();			
			driver.findElement(By.xpath(".//*[@id='reg-logout-div']/a")).click();
			//Validate that the user has logged out of VID. Displays "Portal"
			Assert.assertTrue(driver.getPageSource().contains("Portal"));
			
			log.info("VID-11 TC-5 PASSED");
			
		}

		
		
		/**
		 * Tear down.
		 */
		@AfterClass
		public void tearDown()
		{
			driver.close();
		
		}
		
		
		
	}


	

