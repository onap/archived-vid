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

package org.openecomp.fusion.core;

import java.io.IOException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.openecomp.portalsdk.core.conf.AppConfig;
import org.openecomp.portalsdk.core.objectcache.AbstractCacheManager;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.util.CacheManager;

/**
 * 
 * 
 * 
 * In order to write a unit test, 
 * 1. inherit this class - See SanityTest.java
 * 2. place the "war" folder on your test class's classpath
 * 3. run the test with the following VM argument; This is important because when starting the application from Container, the System Properties file (SystemProperties.java) can have the direct path
 *    but, when running from the Mock Junit container, the path should be prefixed with "classpath" to enable the mock container to search for the file in the classpath  
 *    -Dcontainer.classpath="classpath:"
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = {MockAppConfig.class})
@ActiveProfiles(value="test")
public class MockApplicationContextTestSuite {
		
	    /** The wac. */
    	@Autowired
	    public WebApplicationContext wac;

	    /** The mock mvc. */
    	private MockMvc mockMvc;

	    /**
    	 * Setup.
    	 */
    	@Before
	    public void setup() {
	    	if(mockMvc == null) {
	    		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	    		
	    	}
	    }
	    
	    /**
    	 * Gets the bean.
    	 *
    	 * @param name the name
    	 * @return the bean
    	 */
    	public Object getBean(String name) {
			return this.wac.getBean(name);
		}


		/**
		 * Gets the mock mvc.
		 *
		 * @return the mock mvc
		 */
		public MockMvc getMockMvc() {
			return mockMvc;
		}

		/**
		 * Sets the mock mvc.
		 *
		 * @param mockMvc the new mock mvc
		 */
		public void setMockMvc(MockMvc mockMvc) {
			this.mockMvc = mockMvc;
		}
		
		/**
		 * Gets the web application context.
		 *
		 * @return the web application context
		 */
		public WebApplicationContext getWebApplicationContext() {
			return wac;
		}
		
		
		
		
}
		

		@Configuration
		@ComponentScan(basePackages = "org.openecomp", 
				 excludeFilters = {
								 	// the following source configurations should not be scanned; instead of using Exclusion filter, we can use the @Profile annotation to exclude them
									// see AppConfig class
									//@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.openecomp.portalsdk.core.*AppConfig*")//,
								 	//@ComponentScan.Filter(type = FilterType.REGEX, pattern = org.openecomp.*.*AppConfig*")
								  }
		    	)
		@Profile("test")
		class MockAppConfig extends AppConfig {
			
			@Bean 
		    public SystemProperties systemProperties(){
		    	return new MockSystemProperties();
		    }
			
			@Bean
		    public AbstractCacheManager cacheManager() {
		        return new CacheManager() {
		        	
		        	public void configure() throws IOException {
		        		 
		        	}
		        };
		    }
			
			protected String[] tileDefinitions() {
				return new String[] {"classpath:/WEB-INF/fusion/defs/definitions.xml", "classpath:/WEB-INF/defs/definitions.xml"};
			}
			
			 @Override
			public void addInterceptors(InterceptorRegistry registry) {
			    //registry.addInterceptor(new SessionTimeoutInterceptor()).excludePathPatterns(getExcludeUrlPathsForSessionTimeout());
			    //registry.addInterceptor(resourceInterceptor());
			}
			 
			 public static class MockSystemProperties extends SystemProperties {
					
					public MockSystemProperties() {
					}
					
				}
					
		}
		
		


