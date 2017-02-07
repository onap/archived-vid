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

import java.lang.Character;

/**
 * The Class EncryptConvertor.
 */
public class EncryptConvertor {

  /** The Constant HEX_CHARS. */
  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

  /**
   * toHexString(String) - convert a string into its hex equivalent.
   *
   * @param buf the buf
   * @return the string
   */
  public final static String toHexString(String buf) {
    if (buf == null) return "";
    return toHexString(buf.getBytes());
  }

  /**
   * toHexString(byte[]) - convert a byte-string into its hex equivalent.
   *
   * @param buf the buf
   * @return the string
   */
  public final static String toHexString(byte[] buf) {

    if (buf == null) return "";
    char[] chars = new char[2 * buf.length];
    for (int i = 0; i < buf.length; ++i) {
      chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
      chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
    }
    return new String(chars);
  }   

    // alternate implementation that's slightly slower
// protected static final byte[] Hexhars = {
//	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
// };
// public static String encode(byte[] b) {
//	StringBuilder s = new StringBuilder(2 * b.length);
//	for (int i = 0; i < b.length; i++) {
//	    int v = b[i] & 0xff;
//	    s.append((char)Hexhars[v >> 4]);
//	    s.append((char)Hexhars[v & 0xf]);
//	}
//	return s.toString();
// }

  /**
     * Convert a hex string to its equivalent value.
     *
     * @param hexString the hex string
     * @return the string
     * @throws Exception the exception
     */
  public final static String stringFromHex(String hexString) throws Exception
  {
    if (hexString == null) return "";
    return stringFromHex(hexString.toCharArray());
  }

  /**
   * String from hex.
   *
   * @param hexCharArray the hex char array
   * @return the string
   * @throws Exception the exception
   */
  public final static String stringFromHex(char[] hexCharArray)
  throws Exception {
    if (hexCharArray == null) return "";
    return new String(bytesFromHex(hexCharArray));
  }

  /**
   * Bytes from hex.
   *
   * @param hexString the hex string
   * @return the byte[]
   * @throws Exception the exception
   */
  public final static byte[] bytesFromHex(String hexString) throws Exception
  {
    if (hexString == null) return new byte[0];
    return bytesFromHex(hexString.toCharArray());
  }

  /**
   * Bytes from hex.
   *
   * @param hexCharArray the hex char array
   * @return the byte[]
   * @throws Exception the exception
   */
  public final static byte[] bytesFromHex(char[] hexCharArray)
  throws Exception {
    if (hexCharArray == null) return new byte[0];
    int len = hexCharArray.length;
    if ((len % 2) != 0) throw new Exception("Odd number of characters: '" + String.valueOf(hexCharArray) + "'");
    byte [] txtInByte = new byte [len / 2];
    int j = 0;
    for (int i = 0; i < len; i += 2) {
      txtInByte[j++] = (byte)(((fromHexDigit(hexCharArray[i], i) << 4) | fromHexDigit(hexCharArray[i+1], i)) & 0xFF);
    }
    return txtInByte;
  }

  /**
   * From hex digit.
   *
   * @param ch the ch
   * @param index the index
   * @return the int
   * @throws Exception the exception
   */
  protected final static int fromHexDigit(char ch, int index) throws Exception
  {
    int digit = Character.digit(ch, 16);
    if (digit == -1) throw new Exception("Illegal hex character '" + ch + "' at index " + index);
      return digit;
  }

  // UNIT TESTS (same junit, but we want to run from command line

  /**
   * Check to hex string B.
   *
   * @param arg the arg
   * @param expected the expected
   * @return true, if successful
   */
  public static boolean checkToHexStringB(String arg, String expected) {
	String ret = toHexString(arg != null ? arg.getBytes() : null);
	System.out.println("toHexString(" + arg + ")=> " + ret);
	if (!ret.equals(expected)) System.out.println("\tWRONG, expected: " + expected);
	return ret.equals(expected);
  }

  /**
   * Check to hex string.
   *
   * @param arg the arg
   * @param expected the expected
   * @return true, if successful
   */
  public static boolean checkToHexString(String arg, String expected) {
	String ret = toHexString(arg);
	System.out.println("toHexString(" + arg + ")=> " + ret);
	if (!ret.equals(expected)) System.out.println("\tWRONG, expected: " + expected);
	return ret.equals(expected);
  }

  /**
   * Check from hex string.
   *
   * @param arg the arg
   * @param expected the expected
   * @return true, if successful
   */
  public static boolean checkFromHexString(String arg, String expected) {
	try {
	    String ret = stringFromHex(arg);
	    System.out.println("fromHexString(" + arg + ")=> " + ret);
	    if (!ret.equals(expected)) System.out.println("\tWRONG, expected: " + expected);
	    return ret.equals(expected);
	} catch (Exception e) {
		System.out.println("Caught exception: " + e.toString());
	    return false;
	}
  }

  /**
   * Check from hex string B.
   *
   * @param arg the arg
   * @param expected the expected
   * @return true, if successful
   */
  public static boolean checkFromHexStringB(String arg, String expected) {
	try {
	    byte[] ret = bytesFromHex(arg);
	    String sret = new String(ret);
	    System.out.println("fromHexString(" + arg + ")=> " + sret);
	    if (!sret.equals(expected)) System.out.println("\tWRONG, expected: " + expected);
	    return sret.equals(expected);
	} catch (Exception e) {
		System.out.println("Caught exception: " + e.toString());
	    return false;
	}
  }


  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
	// TODO Auto-generated method stub
    int pass = 0, fail = 0;
    if (checkToHexString("", "")) pass++; else fail++;
    if (checkToHexString(null, "")) pass++; else fail++;
    if (checkToHexString("0", "30")) pass++; else fail++;
    if (checkToHexString("abc", "616263")) pass++; else fail++;
    if (checkToHexString("!@#$%^&*()", "21402324255e262a2829")) pass++; else fail++;
    if (checkToHexStringB("", "")) pass++; else fail++;
    if (checkToHexStringB(null, "")) pass++; else fail++;
    if (checkToHexStringB("0", "30")) pass++; else fail++;
    if (checkToHexStringB("abc", "616263")) pass++; else fail++;
    if (checkToHexStringB("!@#$%^&*()", "21402324255e262a2829")) pass++; else fail++;
    if (checkFromHexString("", "")) pass++; else fail++;
    if (checkFromHexString(null, "")) pass++; else fail++;
    if (checkFromHexString("30", "0")) pass++; else fail++;
    if (checkFromHexString("616263", "abc")) pass++; else fail++;
    if (checkFromHexString("21402324255e262a2829", "!@#$%^&*()")) pass++; else fail++;
    if (checkFromHexStringB("", "")) pass++; else fail++;
    if (checkFromHexStringB(null, "")) pass++; else fail++;
    if (checkFromHexStringB("30", "0")) pass++; else fail++;
    if (checkFromHexStringB("616263", "abc")) pass++; else fail++;
    if (checkFromHexStringB("21402324255e262a2829", "!@#$%^&*()")) pass++; else fail++;
    System.out.println("Tests passed: " + Integer.toString(pass));
    System.out.println("Tests failed: " + Integer.toString(fail));
    System.out.println("=======");
    System.out.println("abc toHex = " + toHexString("abc"));
    System.out.println("123 toHex = " + toHexString("123"));
    try {
    System.out.println("616263 FromHex = " + stringFromHex("616263"));
    System.out.println("313233 FromHex = " + stringFromHex("313233"));
    //System.out.println("current key FromHex = " + stringFromHex("57ajqe{kJjjarj}G#(3)ea7"));
    } catch (Exception e) {
      System.out.println("exception: " + e.toString());
    }
  }

}
