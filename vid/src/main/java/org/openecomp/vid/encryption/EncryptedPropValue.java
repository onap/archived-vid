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

package org.openecomp.vid.encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;

/**
 * The Class EncryptedPropValue.
 */
public class EncryptedPropValue {

	/** The encrypted configuration. */
	private EncryptedConfiguration encryptedConfiguration;
	
	/** The encryption key. */
	private String encryptionKey;
	
	/** The encryption method. */
	private String encryptionMethod;

	/** The logger. */
	static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EncryptedPropValue.class);
	
	/**
	 * Instantiates a new encrypted prop value.
	 */
	public EncryptedPropValue() {
		    // encryptionKey = "57ajqe{kJjjarj}G#(3)ea7";
		    encryptionKey = "aa1adm1n";
		    encryptionMethod = "AES";
		    encryptedConfiguration = new EncryptedConfiguration(encryptionKey, encryptionMethod);
		  }

	/**
	 * Gets the encrypted string.
	 *
	 * @param f the f
	 * @param name the name
	 * @param deflt the deflt
	 * @return the encrypted string
	 * @throws Exception the exception
	 */
	public String getEncryptedString(String f, String name, String deflt) throws Exception {
		return encryptedConfiguration.getString(f, name, deflt);
	}

	/**
	 * Generate encrypted property.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public static void generateEncryptedProperty(String name, String value) {
		logger.debug(EELFLoggerDelegate.debugLogger, "==> generateEncryptedProperty");
		EncryptedPropValue aaiPropValue = new EncryptedPropValue();
	    try {
	      System.out.println(name + ".x=" +
	        EncryptedConfiguration.encryptToTriple(
	        		aaiPropValue.encryptionMethod,
	        		EncryptedConfiguration.generateSalt(),
	        		aaiPropValue.encryptionKey, value));
	    } catch (Exception e) {
	      System.err.println("Cannot encrypt '" + value + "' for property '" + name + "': "+ e.toString());
	    }
	}

	/**
	 * Extract property.
	 *
	 * @param f the f
	 * @param name the name
	 */
	public static void extractProperty(String f, String name) {
		EncryptedPropValue aaiPropValue = new EncryptedPropValue();
		String val = "";
		logger.debug(EELFLoggerDelegate.debugLogger, "==> extractProperty");
		try {
			val = aaiPropValue.getEncryptedString(f, name, "");
			System.out.println(val);
		} catch (Exception e) {
			System.err.println("Cannot extract '" + name + "' from '" + f + "': " + e.toString());
		}
	}

	/**
	 * Usage.
	 */
	public static void usage() {
		usage(null);
	}


	/**
	 * Decrypt triple.
	 *
	 * @param triple the triple
	 * @return the string
	 */
	public static String decryptTriple(String triple) {
		EncryptedPropValue aaiPropValue = new EncryptedPropValue();
		logger.debug(EELFLoggerDelegate.debugLogger, "==> descrptTriple");

		String out = "";
		try {
			//System.out.println(dragonPropValue.encryptedConfiguration.decrypt(triple, dragonPropValue.encryptionKey));
			logger.debug(EELFLoggerDelegate.debugLogger, "calling dragonPropValue.encryptedConfiguration.decrypt()");
			out = EncryptedConfiguration.decrypt(triple,
					aaiPropValue.encryptionKey,
					aaiPropValue.encryptionMethod,
					EncryptedConfiguration.generateSalt());
		      //System.out.println("out = " + out);
		} catch (Exception e) {
			System.err.println("Cannot decrypt '" + triple + "': " + e.toString());
		}
		    
		return out;
	}

	/**
	 * Encrypt input.
	 */
	public static void encryptInput() {
		String s;

		Pattern p = Pattern.compile("^ENCRYPTME[.]([A-Z]*)[.]([^= \t]*)[ \t]*=[ \t]*([^ \t]*)[ \t]*$");

		EncryptedPropValue aaiPropValue = null;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    
		try {
			while ((s = in.readLine()) != null) {
		        Matcher m = p.matcher(s);
		        if (m.matches()) {
		          if (aaiPropValue == null)
		        	  aaiPropValue = new EncryptedPropValue();
		          String method = m.group(1);
		          String name = m.group(2);
		          String value = m.group(3);
		          try {
		            System.out.println(name + ".x=" +
		            EncryptedConfiguration.encryptToTriple(method,
		              EncryptedConfiguration.generateSalt(),
		              aaiPropValue.encryptionKey, value));
		          } catch (Exception e) {
		            System.err.println("Error: Cannot encrypt '" + value + "', method '" + method + "' for property '" + name + "': " + e.toString());
		          } // end of try
		        } else {
		          System.out.println(s);
		        }
		      } // end of while
		    } catch (IOException e) {
		      System.err.println("Error: Cannot read from stdin: " + e.toString());
		    }

		  } 

		  /**
  		 * Usage.
  		 *
  		 * @param msg the msg
  		 */
  		public static void usage(String msg) {
		    if (msg != null) System.err.println(msg);
		    System.err.println("Usage: java EncryptedPropValue -n property -f property-file");
		    System.err.println("\tExtract the named value from the given property-file (or full pathname)");
		    System.err.println("Usage: java EncryptedPropValue -n property -v value");
		    System.err.println("\tEncrypt the given property with the given name and value");
		    System.err.println("Usage: java EncryptedPropValue -E");
		    System.err.println("\tEncrypt all lines that look like ENCRYPTME.METHOD.name=value");
		    System.err.println("Usage: java EncryptedPropValue -u value");
		    System.err.println("\tDecrypt the given value, expressed as a single HEXVAL");
		    System.exit(1);
		  }

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Options options = new Options();
	    options.addOption("n", true, "name");
	    options.addOption("f", true, "property-file");
	    options.addOption("v", true, "value");
	    options.addOption("E", false, "Encrypt all lines that look like ENCRYPTME.METHOD.name=value");
	    options.addOption("u", true, "Decrypt the given value, expressed as a single HEXVAL");
	    options.addOption("h", false, "show help");
	    options.addOption("?", false, "show help");
	    
	    String propfile = null, name = null, value = null, unencrypt = null;
	    boolean encryptStdin = false;

	    CommandLineParser parser = new DefaultParser();
	    CommandLine cmd = null;
	    
	    try {
	    	cmd = parser.parse(options, args);
	    	
	        System.out.println("You picked " + cmd.toString() + "\n");
	        if (cmd.hasOption("n")) {
	        	name = cmd.getOptionValue("n");
	        }
	        if (cmd.hasOption("f")) {
	        	propfile = cmd.getOptionValue("f");
	        }
	        if (cmd.hasOption("u")) {
	        	unencrypt = cmd.getOptionValue("u");
	        }
	        if (cmd.hasOption("E")) {
	        	encryptStdin = true;
	        }
	        if (cmd.hasOption("v")) {
	        	value = cmd.getOptionValue("v");
	        }
	        if (cmd.hasOption("?") || cmd.hasOption("h")) {
	        	usage();
	        	System.exit(0);
	        }

		    if (encryptStdin) {
		      if (name != null || propfile != null || value != null) {
		        usage("cannot use -E with other options");
		      }
		      encryptInput();
		    } else if (unencrypt == null) {
		        if (name == null) usage("-n is required");
		        if (propfile == null) {
		          if (value == null) usage("-v required"); 
		          if (value != null) {
		             generateEncryptedProperty(name, value);
		          }
		        } else {
		          extractProperty(propfile, name);
		        } 
		    } else {
		      String out = decryptTriple(unencrypt);
		      System.out.println(out);
		    }
	    } catch (ParseException e) {
		    System.out.println("Failed to parse command line properties e="+e.toString());
	    } catch (Exception e) {
		    System.out.println("Failed to run EncryptedConfiguration main() e="+e.toString());
	    }
	    
    	System.exit(0);
    	
	}

}
