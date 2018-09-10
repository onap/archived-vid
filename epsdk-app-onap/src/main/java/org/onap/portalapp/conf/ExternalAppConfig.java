/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.conf;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.onap.portalapp.login.LoginStrategyImpl;
import org.onap.portalapp.scheduler.RegistryAdapter;
import org.onap.portalsdk.core.auth.LoginStrategy;
import org.onap.portalsdk.core.conf.AppConfig;
import org.onap.portalsdk.core.conf.Configurable;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.objectcache.AbstractCacheManager;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.CacheManager;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * ONAP Portal SDK sample application. Extends core AppConfig class to
 * reuse interceptors, view resolvers and other features defined there.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.onap"})
@PropertySource(value = { "${container.classpath:}/WEB-INF/conf/app/test.properties" }, ignoreResourceNotFound = true)
@Profile("src")
@EnableAsync
@EnableScheduling
public class ExternalAppConfig extends AppConfig implements Configurable {

	private RegistryAdapter schedulerRegistryAdapter;
    /** The Constant LOG. */
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(ExternalAppConfig.class);
 
    /** The vid schema script. */
    @Value("classpath:vid-schema.sql")
    private Resource vidSchemaScript;

    /** The vid data script. */
    @Value("classpath:vid-data.sql")
    private Resource vidDataScript;
    
    /**
     * The Class InnerConfiguration.
     */
	@Configuration
	@Import(SystemProperties.class)
	static class InnerConfiguration {
	}

	/**
	 * @see org.onap.portalsdk.core.conf.AppConfig#viewResolver()
	 */
	@Override
	public ViewResolver viewResolver() {
		return super.viewResolver();
	}

	/**
	 * @see org.onap.portalsdk.core.conf.AppConfig#addResourceHandlers(ResourceHandlerRegistry)
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
	}

	/**
	 * @see org.onap.portalsdk.core.conf.AppConfig#dataAccessService()
	 */
	@Override
	public DataAccessService dataAccessService() {
		// Echo the JDBC URL to assist developers when starting the app.
		System.out.println("ExternalAppConfig: " + SystemProperties.DB_CONNECTIONURL + " is "
				+ SystemProperties.getProperty(SystemProperties.DB_CONNECTIONURL));
		return super.dataAccessService();
	}

	/**
	 * Creates a new list with a single entry that is the external app
	 * definitions.xml path.
	 * 
	 * @return List of String, size 1
	 */
	@Override
	public List<String> addTileDefinitions() {
		List<String> definitions = new ArrayList<>();
		definitions.add("/WEB-INF/defs/definitions.xml");
		return definitions;
	}

	/**
	 * Adds request interceptors to the specified registry by calling
	 * {@link AppConfig#addInterceptors(InterceptorRegistry)}, but excludes
	 * certain paths from the session timeout interceptor.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.setExcludeUrlPathsForSessionTimeout("/login_external", "*/login_external.htm", "login", "/login.htm",
				"/api*", "/single_signon.htm", "/single_signon");
		super.addInterceptors(registry);
	}

	/**
	 * Creates and returns a new instance of a {@link CacheManager} class.
	 * 
	 * @return New instance of {@link CacheManager}
	 */
	@Bean
	public AbstractCacheManager cacheManager() {
		return new CacheManager();
	}

	/**
	 * Creates and returns a new instance of a {@link SchedulerFactoryBean} and
	 * populates it with triggers.
	 * 
	 * @return New instance of {@link SchedulerFactoryBean}
	 * @throws Exception
	 */
	@Bean // ANNOTATION COMMENTED OUT
	// APPLICATIONS REQUIRING QUARTZ SHOULD RESTORE ANNOTATION
	@DependsOn("dataSourceInitializer")
	public SchedulerFactoryBean schedulerFactoryBean() throws Exception {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(schedulerRegistryAdapter.getTriggers());
		scheduler.setConfigLocation(appApplicationContext.getResource("WEB-INF/conf/quartz.properties"));
		scheduler.setDataSource(dataSource());
	    scheduler.setJobFactory(new SpringBeanJobFactory());
		return scheduler;
	}

	
	/**
     * Data source initializer.
     *
     * @param dataSource the data source
     * @return the data source initializer
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        
        LOG.info("Initializing VID data source");
        
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }
    
    /**
     * Database populator.
     *
     * @return the database populator
     */
    public DatabasePopulator databasePopulator() {
        LOG.info("Populating VID data source");
        
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(vidSchemaScript);
        populator.addScript(vidDataScript);
        return populator;
    }
	

	/*@Bean
	public SpringLiquibase liquibaseBean(DataSource dataSource) {
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog("classpath:db-master-changelog.xml");
		return springLiquibase;
	}*/
	
	/**
	 * Sets the scheduler registry adapter.
	 * 
	 * @param schedulerRegistryAdapter
	 */
	@Autowired
	public void setSchedulerRegistryAdapter(final RegistryAdapter schedulerRegistryAdapter) {
		this.schedulerRegistryAdapter = schedulerRegistryAdapter;
	}

	@Bean
	public LoginStrategy loginStrategy() {
		return new LoginStrategyImpl();
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver=new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

}
