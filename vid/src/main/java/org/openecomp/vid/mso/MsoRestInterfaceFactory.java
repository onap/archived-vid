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

package org.openecomp.vid.mso;

import org.openecomp.portalsdk.core.util.SystemProperties;

/**
 * A factory for creating MsoRestInterface objects.
 */
public class MsoRestInterfaceFactory {
	
	/**
	 * Gets the single instance of MsoRestInterfaceFactory.
	 *
	 * @return single instance of MsoRestInterfaceFactory
	 */
	public static MsoRestInterfaceIfc getInstance () {
		MsoRestInterfaceIfc obj = null;
		
		String mso_dme2_enabled = SystemProperties.getProperty(MsoProperties.MSO_DME2_ENABLED);
//		if ( (mso_dme2_enabled != null) && (mso_dme2_enabled.equalsIgnoreCase("true") ) ) {
//			obj = new MsoDme2RestInterface();
//		}
//		else {
			obj = new MsoRestInterface();
//		}
		return ( obj );
	}

}
