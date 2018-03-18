package org.onap.portalapp.conf;

import java.util.List;

import org.junit.Test;
import org.onap.portalapp.scheduler.RegistryAdapter;
import org.onap.portalsdk.core.auth.LoginStrategy;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public class ExternalAppConfigTest {

    private ExternalAppConfig createTestSubject() {
        return new ExternalAppConfig();
    }

    @Test
    public void testViewResolver() throws Exception {
        ExternalAppConfig testSubject;
        ViewResolver result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.viewResolver();
    }



    @Test
    public void testDataAccessService() throws Exception {
        ExternalAppConfig testSubject;
        DataAccessService result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.dataAccessService();
    }

    @Test
    public void testAddTileDefinitions() throws Exception {
        ExternalAppConfig testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.addTileDefinitions();
    }

   

    @Test
    public void testCacheManager() throws Exception {
        ExternalAppConfig testSubject;
        //AbstractCacheManager result;

        // default test
        testSubject = createTestSubject();
        testSubject.cacheManager();
    }
   


    @Test
    public void testSetSchedulerRegistryAdapter() throws Exception {
        ExternalAppConfig testSubject;
        RegistryAdapter schedulerRegistryAdapter = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setSchedulerRegistryAdapter(schedulerRegistryAdapter);
    }

    @Test
    public void testLoginStrategy() throws Exception {
        ExternalAppConfig testSubject;
        LoginStrategy result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.loginStrategy();
    }
}