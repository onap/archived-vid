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

package org.onap.vid.aai.util;


import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.util.security.Password;


public class JettyObfuscationConversionCommandLineUtil {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		Options options = new Options();
		options.addOption("e", true, "obfuscate the given string");
		options.addOption("d", true, "deobfuscate the given string");
		
		CommandLineParser parser = new BasicParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			String toProcess = null;
			
			if (cmd.hasOption("e")){
				toProcess = cmd.getOptionValue("e");
				String encoded = Password.obfuscate(toProcess);
				System.out.println(encoded);
			} else if (cmd.hasOption("d")) {
				toProcess = cmd.getOptionValue("d");
				String decoded_str = Password.deobfuscate(toProcess);
				System.out.println(decoded_str);
			} else {
				usage();
			}
		} catch (ParseException e) {
			System.out.println("failed to parse input");
			System.out.println(e.toString());
			usage();
		} catch (Exception e) {
			System.out.println("exception:" + e.toString());
		}
	}
	
	/**
	 * Usage.
	 */
	private static void usage(){
		System.out.println("usage:");;
		System.out.println("-e [string] to obfuscate");
		System.out.println("-d [string] to deobfuscate");
		System.out.println("-h help");
	}
}
