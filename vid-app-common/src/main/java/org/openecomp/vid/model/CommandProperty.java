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

package org.openecomp.vid.model;

import java.util.Map;

/**
 * The Class Command Property.
 */
public class CommandProperty {
	
	/** The display name for this input */
	private String displayName;
	
	/** The command, "get_input" */
	private String command;
	
	/** The input name we refer to back under the inputs section */
	private String inputName;

	/**
	 * Gets the display name.
	 *
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * Gets the command.
	 *
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * Gets the inputName.
	 *
	 * @return the inputName
	 */
	public String getInputName() {
		return inputName;
	}
	/**
	 * Sets the display name value.
	 *
	 * @param i the new get_input value
	 */
	public void setDisplayName(String i) {
		this.displayName = i;
	}
	/**
	 * Sets the command value.
	 *
	 * @param i the new command value
	 */
	public void setCommand(String i) {
		this.command = i;
	}
	
	/**
	 * Sets the input name value.
	 *
	 * @param i the new input name value
	 */
	public void setInputName(String i) {
		this.inputName=i;
	}
	
	public String toString () {
		String result = "displayName=" + displayName + " command=" + command + " inputName" + inputName;
		return result;
	}
}
