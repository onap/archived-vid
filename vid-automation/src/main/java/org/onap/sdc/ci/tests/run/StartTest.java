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

package org.onap.sdc.ci.tests.run;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.TestNG;

public class StartTest {

	public static long timeOfTest = 0;

	public static boolean debug = false;

	public static AtomicBoolean loggerInitialized = new AtomicBoolean(false);

	protected static Logger logger = null;

	public static void main(String[] args) {

		String debugEnabled = System.getProperty("debug");
		if (debugEnabled != null && debugEnabled.equalsIgnoreCase("true")) {
			debug = true;
		}
		System.out.println("Debug mode is " + (debug ? "enabled" : "disabled"));

		enableLogger();

		TestNG testng = new TestNG();

		List<String> suites = new ArrayList<String>();
		suites.add(args[0]);
		testng.setTestSuites(suites);
		testng.setUseDefaultListeners(true);
		testng.setOutputDirectory("target/");

		testng.run();

	}

	public StartTest() {
		logger = Logger.getLogger(StartTest.class.getName());
	}

	public static void enableLogger() {

		if (false == loggerInitialized.get()) {

			loggerInitialized.set(true);

			String log4jPropsFile = System.getProperty("log4j.configuration");
			if (System.getProperty("os.name").contains("Windows")) {
				String logProps = "src/main/resources/ci/conf/log4j.properties";
				if (log4jPropsFile == null) {
					System.setProperty("targetlog", "target/");
					log4jPropsFile = logProps;
				}

			}
			PropertyConfigurator.configureAndWatch(log4jPropsFile);

		}
	}

}
