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

package org.openecomp.vid.conf;

import java.util.Arrays;

import org.openecomp.portalsdk.core.conf.AppInitializer;

/**
 * The Class ExternalAppInitializer.
 */
public class ExternalAppInitializer extends  AppInitializer{
	
	
	/* (non-Javadoc)
	 * @see org.openecomp.portalsdk.core.conf.AppInitializer#getRootConfigClasses()
	 */
	@Override
    protected Class<?>[] getRootConfigClasses() {
    	return super.getRootConfigClasses();
    }
  
    /* (non-Javadoc)
     * @see org.openecomp.portalsdk.core.conf.AppInitializer#getServletConfigClasses()
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
//    	Class<?>[] configClasses =  super.getServletConfigClasses();
//    	Class<?>[] additionalConfigClasses  = Arrays.copyOf(configClasses, configClasses.length);
//    	addConfigClass(additionalConfigClasses, ExternalAppConfig.class);
//    	return additionalConfigClasses;
//    	
    	return new Class[] {ExternalAppConfig.class};
    }
    
    /**
     * Adds the config class.
     *
     * @param a the a
     * @param e the e
     * @return the class[]
     */
    static Class<?>[] addConfigClass(Class<?>[] a, Class<?> e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
  
    /* (non-Javadoc)
     * @see org.openecomp.portalsdk.core.conf.AppInitializer#getServletMappings()
     */
    /*
     * URL request will direct to the Spring dispatcher for processing
     */
    @Override
    protected String[] getServletMappings() {
       return super.getServletMappings();
    }
 
}


