package org.onap.sdc.ci.tests.run;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class RunTest {

	public static void runTestSuite() {
		TestNG testng = new TestNG();
		List<String> suites = new ArrayList<String>();
		
		String testSuite = System.getProperty("testSuite");
		if (testSuite == null){
			System.out.println("No test suite file was found, please provide test suite.");
			System.exit(1);
		}
		suites.add(testSuite);
		testng.setTestSuites(suites);
		testng.setUseDefaultListeners(true);
		testng.setOutputDirectory("target/");
		testng.run();
	}
}
