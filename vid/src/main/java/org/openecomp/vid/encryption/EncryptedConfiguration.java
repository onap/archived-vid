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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;


/**
 *  Class to manage encrypted configuration values.
 */
public class EncryptedConfiguration {
	
	/** The logger. */
	static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EncryptedConfiguration.class);


	  /** 	Our secret key and method. */
	  private String encryptionKey;
	  
  	/** The encryption method. */
  	private String encryptionMethod;

	  /**
  	 * 	Where to log when things go wrong.
  	 *
  	 * @param key the key
  	 * @param method the method
  	 */

	  public EncryptedConfiguration(String key, String method) {
	    encryptionKey = key.trim();
	    encryptionMethod = method;
	  } 

	  /**
  	 * Retrieve an encrypted string from the given configuration.
  	 * The name will have ".x" appended to it.
  	 * Decoded from hex, it will be "method:hexsalt:hexvalue".
  	 * The format of the value will be in hex.
  	 * Method will be "r" to begin with, for "rc4".
  	 *
  	 * @param f the f
  	 * @param name the name
  	 * @param deflt the deflt
  	 * @return the string
  	 * @throws Exception the exception
  	 */
	  public String getString(String f, String name, String deflt)
	  throws Exception {
	    logger.debug(EELFLoggerDelegate.debugLogger, "==> getString"); 
	    return getString(f, name, deflt, encryptionKey);
	  }

	  /**
  	 * Retrieve an encrypted string from the given configuration.
  	 * The name will have ".x" appended to it.
  	 * Decoded from hex, it will be "method:hexsalt:hexvalue".
  	 * The format of the value will be in hex.
  	 * Method will be "r" to begin with, for "rc4".
  	 *
  	 * @param f the f
  	 * @param name the name
  	 * @param deflt the deflt
  	 * @param key the key
  	 * @return the string
  	 * @throws Exception the exception
  	 */
	  public String getString(String f, String name, String deflt, String key)
	   throws Exception {

	    logger.debug(EELFLoggerDelegate.debugLogger, "==> getString"); 
	    logger.debug(EELFLoggerDelegate.debugLogger, "  key = " + key);
	    Properties prop = new Properties();
	    InputStream input = null;

	    try {
	      input = new FileInputStream(f);

	      prop.load(input);

	      /* for testing, a dump of all key-value pairs
	      Enumeration<?> e = prop.propertyNames();
	      while (e.hasMoreElements()) {
	        String k = (String) e.nextElement();
	        String value = prop.getProperty(k);
	        System.out.println("Key : " + k + ", Value : " + value);
	      }
	      */

	    } catch (IOException ex) {
	      ex.printStackTrace(); // TODO: fix
	    } finally {
	      input.close();
	    }

	    String str = prop.getProperty(name + ".x");
	    logger.debug(EELFLoggerDelegate.debugLogger, "str = " + str);

	    if (str == null) {
	      // not encrypted version
	      str = prop.getProperty(name);
	      if (str == null) {
	        return deflt;
	      }
	      return str;
	    }

	    String method = encryptionMethod;
	    logger.debug(EELFLoggerDelegate.debugLogger, "method = " + method);
	    String salt = EncryptedConfiguration.generateSalt();
	    logger.debug(EELFLoggerDelegate.debugLogger, "salt = " + salt);

	    return decrypt(str, key, method, salt);
	  }

	  /**
  	 * Decrypt a string in 'method:hexsalt:hexvalue' format.
  	 *
  	 * @param triple the triple
  	 * @param key the key
  	 * @param method the method
  	 * @param salt the salt
  	 * @return the string
  	 * @throws Exception the exception
  	 */
	  public static String decrypt(String triple, String key, String method, String salt) throws Exception {
	    /*
	    String[] strParts = triple.trim().split(":");
	    if (strParts.length != 3) throw new Exception("Encrypted value must look like 'x:y:z'");
	    return decrypt(strParts[0], Convert.stringFromHex(strParts[1]), key, Convert.bytesFromHex(strParts[2]));
	    */

	    return decrypt(method, salt, key, EncryptConvertor.bytesFromHex(triple)); 
	  }

	  /**
  	 * Decrypt a string 'method:hexsalt:hexvalue' format.
  	 *
  	 * @param method the method
  	 * @param salt the salt
  	 * @param key the key
  	 * @param bvalue the bvalue
  	 * @return the string
  	 * @throws Exception the exception
  	 */
	  public static String decrypt(String method, String salt, String key, byte[] bvalue) throws Exception {
	    logger.debug(EELFLoggerDelegate.debugLogger, "==> decrypt method");
	    logger.debug(EELFLoggerDelegate.debugLogger, "  method = " + method);
	    logger.debug(EELFLoggerDelegate.debugLogger, "  salt = " + salt);
	    logger.debug(EELFLoggerDelegate.debugLogger, "  key = " + key);
	    byte[] secretKey = runDigest(salt + "." + key);

	    SecretKeySpec skeySpec = new SecretKeySpec(secretKey, method);

	    Cipher cipher = Cipher.getInstance(method);	// "AES"
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec);

	    byte[] decrypted = cipher.doFinal(bvalue);
	    return new String(decrypted);
	  }

	  /**
  	 * Encrypt a string using the given method, salt and key.
  	 *
  	 * @param method the method
  	 * @param salt the salt
  	 * @param key the key
  	 * @param value the value
  	 * @return the byte[]
  	 * @throws Exception the exception
  	 */
	  public static byte[] encrypt(String method, String salt, String key, String value) throws Exception {
	    logger.debug(EELFLoggerDelegate.debugLogger, "==> encrypt() method");
	    byte[] bvalue = value.getBytes();
	    byte[] secretKey = runDigest(salt + "." + key);

	    SecretKeySpec skeySpec = new SecretKeySpec(secretKey, method);

	    Cipher cipher = Cipher.getInstance(method);	// "AES"
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

	    byte[] encrypted = cipher.doFinal(bvalue);
	    return encrypted;
	  }

	  /**
  	 * Prepare a secret key by running a digest on it.
  	 *
  	 * @param text the text
  	 * @return the byte[]
  	 * @throws Exception the exception
  	 */
	  private static byte[] runDigest(String text) throws Exception {
	    logger.debug(EELFLoggerDelegate.debugLogger, "==> runDigest() method");
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.reset();
	    md.update(text.getBytes(), 0, text.length());
	    return md.digest();
	  }

	  /**
  	 * Encrypt a string using the given method, salt and key, and return it as a hex-formated triple.
  	 *
  	 * @param method the method
  	 * @param salt the salt
  	 * @param key the key
  	 * @param value the value
  	 * @return the string
  	 * @throws Exception the exception
  	 */
	  public static String encryptToTriple(String method, String salt, String key, String value) throws Exception {
	    logger.debug(EELFLoggerDelegate.debugLogger, "==> Enter encryptToTriple()");
	    logger.debug(EELFLoggerDelegate.debugLogger, "method = " + method);
	    logger.debug(EELFLoggerDelegate.debugLogger, "salt = " + salt);
	    logger.debug(EELFLoggerDelegate.debugLogger, "key = " + key);
	    logger.debug(EELFLoggerDelegate.debugLogger, "valye = " + value);
	   
	    /* 
	    StringBuffer sb = new StringBuffer(method);
	      sb.append(":").append(Convert.toHexString(salt))
	      .append(":").append(Convert.toHexString(encrypt(method, salt, key, value)));
	    logger.debug("s = " + sb.toString());
	    */

	    StringBuffer sb2 = new StringBuffer("");
	    sb2.append(EncryptConvertor.toHexString(encrypt(method, salt, key, value)));
	    String s = sb2.toString();
	    logger.debug(EELFLoggerDelegate.debugLogger, "s = " + s);
	    return s;
	  }

	  /**
  	 * Create a value that can be used as a salt.
  	 *
  	 * @return the string
  	 */
	  public static String generateSalt() {
	    logger.debug(EELFLoggerDelegate.debugLogger, "==> generaltSalt");

	    // G2 wants to hard code the value for salt.
	    // set the value as 123456789
	    // return Long.toString(System.currentTimeMillis() % 1000) + Pid.getPidStr();
	    return Long.toString(123456789 % 10000);
	  }

	  /**
  	 * Usage.
  	 */
  	public static void usage() {
	    usage(null);
	  }

	  /**
  	 * Usage.
  	 *
  	 * @param msg the msg
  	 */
  	public static void usage(String msg) {
	    if (msg != null) System.out.println(msg);
	    System.out.println("Usage: java EncryptedConfiguration -D triple -k key\n" +
	      "java EncryptedConfiguration -d string -m method [-s salt | -S] -k key\n" +
	      "java EncryptedConfiguration -e string -m method [-s salt | -S] -k key\n" +
	      "-D\tdecrypt x:y:z triple\n" +
		    "-d\tdecrypt string (in hex)\n" +
		    "-e\tencrypt string\n" +
		    "-S\tgenerate a salt\n"
		    );
	     System.exit(1);
	  }



	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		Options options = new Options();
	    options.addOption("s", true, "salt");
	    options.addOption("S", false, "Generate salt");
	    options.addOption("k", true, "key");
	    options.addOption("m", true, "method");
	    options.addOption("e", true, "encryptString");
	    options.addOption("d", true, "decryptString");
	    options.addOption("D", true, "triple x:y:z");
	    options.addOption("h", false, "show help");
	    options.addOption("?", false, "show help");

	    String salt = null, key = null, method = null, encStr = null, decStr = null, triple = null;
	    boolean genSalt = false;

	    CommandLineParser parser = new DefaultParser();
	    CommandLine cmd = null;
	    
	    try {
	    	cmd = parser.parse(options, args);
	    	
	        System.out.println("You picked " + cmd.toString() + "\n");
	        if (cmd.hasOption("s")) {
	        	salt = cmd.getOptionValue("s");
	        }
	        if (cmd.hasOption("S")) {
	        	genSalt = true;
	        	System.out.println("here in S");	        
	        }
	        if (cmd.hasOption("k")) {
	        	key = cmd.getOptionValue("k");
	        }
	        if (cmd.hasOption("m")) {
	        	method = cmd.getOptionValue("m");
	        }
	        if (cmd.hasOption("e")) {
	        	encStr = cmd.getOptionValue("e");
	        }
	        if (cmd.hasOption("d")) {
	        	decStr = cmd.getOptionValue("d");
	        }
	        if (cmd.hasOption("D")) {
	        	triple = cmd.getOptionValue("D");
	        }
	        if (cmd.hasOption("?") || cmd.hasOption("h")) {
	        	usage();
	        	System.exit(0);
	        }

	    if (triple == null) {
	      if ((salt == null) && !genSalt) usage("one of -s or -S must be specified");
	      if ((salt != null) && genSalt) usage("only one of -s or -S must be specified");
	      if (key == null) usage("-k must be specified");
	      if (method == null) usage("-m must be specified");
	      if ((encStr == null) && (decStr == null)) usage("one of -d or -e must be specified");
	      if ((encStr != null) && (decStr != null)) usage("only one of -d or -e may be specified");
	      if (genSalt) {
	        salt = generateSalt();
	        System.out.println("salt = " + salt);
	      }

	      if (encStr != null)
	        System.out.println(encryptToTriple(method, salt, key, encStr));
	      if (decStr != null)
	        System.out.println(decrypt(method, salt, key, EncryptConvertor.bytesFromHex(decStr)));
	    } else {
	      if (key == null) {
	    	  usage("-k not specified");
	    	  System.exit(0);
	      }
		  System.out.println(decrypt(triple, key, method, salt));
	    }
	    
	    } catch (ParseException e) {
		    System.out.println("Failed to parse command line properties e="+e.toString());
	    } catch (Exception e) {
		    System.out.println("Failed to run EncryptedConfiguration main() e="+e.toString());
	    }

		// http://forums.sun.com/thread.jspa?threadID=5290983
		// try {
		//     String message = "Strong Versus Unlimited Strength Cryptography";
		//     SecretKeySpec skeySpec = new SecretKeySpec("0123456789ABCDEF".getBytes(), "AES"); //AES-128

		//     Cipher cipher = Cipher.getInstance("AES");	// "AES/ECB/NoPadding"
		//     cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		//     byte[] encrypted = cipher.doFinal(message.getBytes());
		//     System.out.println("encrypted string: " + encrypted); //storing into MySQL DB
		//     System.out.println("in hex: '" + Convert.toHexString(encrypted) + "'");

		//     cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		//     byte[] original = cipher.doFinal(encrypted);
		//     String originalString = new String(original);
		//     System.out.println("Original string: " + originalString);
		// } catch (Exception e) {
		//     System.err.println("Exception caught: " + e.toString());
		// }
    	System.exit(0);

	}

}
